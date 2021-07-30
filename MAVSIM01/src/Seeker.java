import java.awt.Color; 
import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Seeker extends Robot {
	
	public enum Phase {
		Phase1, Phase2, Phase3, Phase4
	}
    
    private Color color;
    private int id;
    
    private HashSet<Integer> exploredInt = new HashSet<Integer>();
    private HashSet<Coordinate> explored = new HashSet<Coordinate>();
    private HashSet<Integer> targetFound = new HashSet<Integer>();
    
    private HashMap<Integer, RobotRegionTime> roboTime;
    
    private HashMap<Integer, HashSet<Integer>> dataRecieved;
    
    private Task currTask;
    private Phase currPhase;
        
    
    private CricularMotion moveC;
    
    private LinkedList<Integer> sendBuffer;
    
    private LinkedList<Integer> myCols;
    
    private PatrolRegion pr;
    
    private boolean isSending;
    
    private double howBusy;
    private double alpha = 0.20;
    
    private Coordinate targetLoc;
    private boolean targetReached;
    private boolean waiting;
    
    private boolean inCommunication;
    private int commPeriod;
    private int commParterID;
    
    private boolean hasData;
    
    private double sendProb;
    
    private int pOneCounter;
    
    private int succComm;
    
    private int dataID;
    
    private Coordinate currC;
    
    private int[][] coordinateTimeLog;
    
    private int currTime;
    
    
    private Data d;
    
    private Data ds;
    
    private int failurePeriod;
    
    public Seeker(Coordinate c, int vx, int vy, int width, int height, int courtWidth, int courtHeight, Color color, int id) {
        super(vx, vy, c.getPx(), c.getPy(), width, height, courtWidth, courtHeight);
        // TODO Auto-generated constructor stub
        this.id = id;
        this.color = color;
        
        this.commPeriod = 0;
        this.inCommunication = false;
        
        this.sendProb = 0.5;
        
        this.currTime = 0;
        
        
        this.dataRecieved = new HashMap<Integer, HashSet<Integer>>();
        
        this.sendBuffer = new LinkedList<Integer>();
        
        this.dataID = 0;
        
        this.succComm = 0;
        
        this.howBusy = 0.60;
        
        this.failurePeriod = 0;
        
        this.pOneCounter = 0;
        
        this.currC = c;
        
        this.roboTime = new HashMap<Integer, RobotRegionTime>();
        
        this.pr = new PatrolRegion(1,0);
        
        this.moveC = new CricularMotion(Phase.Phase1);
        
        this.coordinateTimeLog = new int[SimulationV3.Y_MAX + 1][SimulationV3.X_MAX + 1];
        
        this.currTask = new Task();
        
        this.currPhase = Phase.Phase1;
        
        this.myCols = new LinkedList<Integer>();
        
        this.moveC.assignColsStrat5(SimSettings.NumPursuers, id);;
        
        this.myCols = this.moveC.getCols();
        
        
        
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(this.color);
        if (this.failurePeriod != 0 || this.currPhase == Phase.Phase2) {
        	g.setColor(Color.YELLOW);
        }
        
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());  
    }
    
    
    public void cuurCoordinate(Coordinate c) {
    	this.currC = c;
    }
    
    public void changePhase(Phase p) {
    	this.moveC.changePhase(p);
    	this.currPhase = p;
    }
    public void ObsNav(Directions d) {
        
        switch (d) {
        case N:
            this.setVy(Math.abs(this.getVy()));
            break;  
        case S:
            this.setVy(-Math.abs(this.getVy()));
            break;
        case W:
            this.setVx(Math.abs(this.getVx()));
            break;
        case E:
            this.setVx(-Math.abs(this.getVx()));
            break;
        default:
            break;
        }
           
    }
    
    public void sendTo(int curPx, int curPy, int tarPx, int tarPy) {
        if (tarPx > curPx) {
            this.setVx(Math.abs(this.getVx()));
            
            if (tarPy > curPy) {
                this.setVy(Math.abs(this.getVy()));
                
            } else {
                this.setVy(-Math.abs(this.getVy()));
            }
               
        } else {
            this.setVx(-Math.abs(this.getVx()));
            
            if (tarPy > curPy) {
                this.setVy(Math.abs(this.getVy()));
                
            } else {
                this.setVy(-Math.abs(this.getVy()));
            }
        }
    }

        
    public void sendInit(Strategies s) {
    	
    /*	double sendP = (double) this.succComm / (double) this.sendBuffer.size();
    	if (sendP < 0.10 || this.sendBuffer.size() == 0) {
    		sendP = 0.10;
    	} */
    	double sendP = 0;
    	
    	if (this.howBusy <= 0) {
    		sendP = 0.30;
    	} else if (this.howBusy >= 1) {
    		sendP = 0.15;
    	} else {
    		sendP = 1.0 - this.howBusy;
    	}
 
    	if (Math.random() < sendP) {
    		this.isSending = true;
    		this.howBusy = this.howBusy + this.alpha;
    		
    		if (s == Strategies.Strategy1) {
    			this.ds = new Data(this.coordinateTimeLog, dataID, id);
    		} else if (s == Strategies.Strategy4) {
    			this.dataID++;
    			this.ds = new Data(this.myCols, this.currPhase, dataID, id);
    		} else if (s == Strategies.Strategy6) {
    			this.dataID++;
    			this.ds = new Data(this.roboTime, this.pr.getRegionID(), this.dataID, this.id);
    		}
    		// this.ds = something
    	} else {
    		this.isSending = false;
    		clearFilter();
    	}

    }
    
    public void startFailure() {
    	this.failurePeriod = 1;
    }
    
    public void endFailure() {
    	this.failurePeriod = 0;
    }
    
    public void updateFailureTime() {
    	this.failurePeriod++;
    }
    
    public int getFailurePreiod() {
    	return this.failurePeriod;
    }
    
    
    public Seeker.Phase getPhase() {
    	return this.currPhase;
    }
    public boolean sendStatus() {
    	return this.isSending;
    }
    
    public void sendDone() {
    	this.isSending = false;
    }
    
    public Data toSendData() {
    	return this.ds;
    }
    
    public void moveTo(Coordinate c) {
    	this.targetLoc = c;
    	this.targetReached = false;

    }
    
    public Coordinate getTargetLoc() {
    	return this.targetLoc;
    }
    
    public void reachedTarget() {
    	this.targetReached = true;
    }
    
    public boolean hasReachedTarget() {
    	return this.targetReached;
    }
    
    public boolean recieveData(Data dd) {
    	this.howBusy = this.howBusy + this.alpha;
    	if (this.hasData) {
    		this.d = null;
    	//	this.sendBuffer.removeFirst();
    	//	this.sendBuffer.addFirst(0);
    	//	this.sendProb = this.sendProb - ((1 / 50)); // (1 / 50)
    		return false;
    	} else {
    		this.d = dd;
    		this.hasData = true;
    	//	this.sendBuffer.addFirst(1);
    	//	this.sendProb = this.sendProb + ((1 / 50)); // (1 / 50)
    		return true;
    	}
    }
    
    public void clearFilter() {
    	this.howBusy = 0.0;
    }
    
    public void fillFilter() {
    	this.howBusy = 1.0;
    }
    
    public void registerTime(int i) {
    	this.currTime = i;
    }
    
    private boolean containsData(Data d) {
    	int roboID = d.getRoboID();
    	int dataID = d.getDataID();
    	
    	if (this.dataRecieved.containsKey(roboID)) {
    		if (this.dataRecieved.get(roboID).contains(dataID)) {
    			return true;
    		} else {
    			return false;
    		}	
    	} else {
    		return false;
    	}
    }
    
    public void processData(Strategies s) {
    	if (s == Strategies.Random) {
    		this.hasData = false;
    		return;
    	}
    	if (this.d == null) {
    		this.hasData = false;
    	} else if (containsData(this.d)) {
    		this.d = null;
    		this.hasData = false;
    	} else {
    		if (this.dataRecieved.containsKey(this.d.getRoboID())) {
    			HashSet<Integer> h = this.dataRecieved.get(this.d.getRoboID());
    			h.add(this.d.getDataID());
    			this.dataRecieved.put(this.d.getRoboID(), h);
    		} else {
    			HashSet<Integer> h = new HashSet<Integer>();
    			h.add(this.d.getRoboID());
    			this.dataRecieved.put(this.d.getRoboID(), h);
    		}
    		// do something with the data
    		if (s == Strategies.Strategy1) { 
    			int[][] newData = this.d.getCoordinateTimeArray();
    			int min = Integer.MAX_VALUE;
    			LinkedList<Coordinate> cList = new LinkedList<Coordinate>();
    			Coordinate corMin = null;
    			for (int i = 0; i < this.coordinateTimeLog.length; i++) {
    				for (int j = 0; j < this.coordinateTimeLog[0].length; j++) {
    					if (newData[i][j] > this.coordinateTimeLog[i][j]) {
    						this.coordinateTimeLog[i][j] = newData[i][j];
    					}
    					if (this.coordinateTimeLog[i][j] < min) {
    						min = this.coordinateTimeLog[i][j];
    						corMin = new Coordinate(j, i);
    					}
    					
    					if (this.coordinateTimeLog[i][j] == 0) {
    						cList.add(new Coordinate(j, i));
    					}
    					
    				}
    			}
    			
    			if (!cList.isEmpty()) {
    				Random rndm = new Random();

        	        int rndmNumber = rndm.nextInt(cList.size());
        	        corMin = cList.get(rndmNumber);
    			}
    			moveTo(corMin); 
    		} else if (s == Strategies.Strategy4) {
    			
    			if (this.currPhase == Phase.Phase1) {
    				//doPhaseOne(this.d.getColList());
    				
    				
    				
    				if (currC.getY() == SimulationV3.Y_MAX - 1) {
    					
    					this.pOneCounter++;

    				}
    				
    				if (this.pOneCounter > 5 || this.d.getPhase() == Seeker.Phase.Phase2) {
    					this.currPhase = Phase.Phase2;
    					this.moveC.changePhase(Phase.Phase2);
    				}
    				
    			} else if (this.d.getPhase() == Seeker.Phase.Phase3) {
    				if (this.currPhase == Phase.Phase2) {
    					this.currPhase = Phase.Phase3;
    					this.moveC.changePhase(Phase.Phase3);
    					
    				}
    			} else if (this.d.getPhase() == Seeker.Phase.Phase2) {
    				if (this.currPhase == Phase.Phase3) {
    					this.currPhase = Phase.Phase2;
    					this.moveC.changePhase(Phase.Phase2);
    					
    				}
    			}
    		} else if (s == Strategies.Strategy6) {
    			int i = this.roboTime.size();
    			int j = this.d.getRegionID();
    			
    			this.roboTime.put(this.d.getRoboID(), new RobotRegionTime(j, this.currTime));    			
    			
    			
    			updateRoboTime(this.d.getRobotTime());  
    			
    			int r = this.pr.getRegionID();
    			
    			int[] regArr = new int[this.roboTime.size() + 1];
    			
    			int nr = 0;
    			
    			if (r != j) {
    				nr = r;
    			} else {
    				for (Integer m: this.roboTime.keySet()) {
    					regArr[this.roboTime.get(m).getRegionID()] = 1;
    				}
    				
    				int c = 1;
    				
    				boolean regFound = false;
    				
    				while (((r + c) <= this.roboTime.size()) || ((r - c) >= 0)) {
    					if ((r + c) <= this.roboTime.size()) {
    						if (regArr[r + c] == 0) {
    							nr = r + c;
    							regFound = true;
    							break;
    						}
    					} else if ((r - c) >= 0) {
    						if (regArr[r - c] == 0) {
    							nr = r + c;
    							regFound = true;
    							break;
    						}
    					}
    					c++;
    				}
    				
    				if (!regFound) {
    					nr = ThreadLocalRandom.current().nextInt(0, this.roboTime.size() + 1);
    				}
    			}
    			
    			if (i < this.roboTime.size()) {
    				this.pr.resetCoordinates(this.roboTime.size() + 1, nr);
    			} else if (i < this.roboTime.size()) {
    				
    			}
    			
    		}
    	}
    	this.hasData = false;
    	this.d = null;
    }
    
    
    private void updateRoboTime(HashMap<Integer, RobotRegionTime> h) {
    	for(Integer i: h.keySet()) {
    		if (this.roboTime.containsKey(i)) {
    			if (this.roboTime.get(i).getTime() < h.get(i).getTime()) {
    				this.roboTime.put(i, h.get(i));
    			}
    		} else {
    			if (i != this.id) {
    				this.roboTime.put(i, h.get(i));
    			}
    		}
    		
    	}
    }
    
    
    public Coordinate getCircularNext(Coordinate c) {
    	return this.moveC.getNextCoordinate(c);
    }
    
    public Coordinate getCircularNextStrat5(Coordinate c) {
    	return this.moveC.getNextCoordinateStrat5(c);
    }
    
    public void commSucc() {
    	this.succComm++;
    	this.sendBuffer.addFirst(1);
    	
    	if (this.sendBuffer.size() > SimulationV3.ROAD_WIDTH + SimulationV3.H_SPACING) {
    		if (this.sendBuffer.getLast() == 1) {
    			this.succComm--;
    			this.sendBuffer.removeLast();
    		}
    	}
    }
    
    public Coordinate getNextCoordinateSt6(Coordinate c) {
    	return this.pr.getNextCoordinate(c);
    	
    }
    
    public void commJammed() {
    	this.succComm--;
    	this.sendBuffer.removeFirst();
    	this.sendBuffer.addFirst(0);
    }
    
    public Coordinate getTaskedCoordinate() {
    	Coordinate c = this.currTask.getNextCoordinate();
    	moveTo(c);
    	return c;
    }
    
    public Coordinate getEarliest() {
    	int min = Integer.MAX_VALUE;
		Coordinate corMin = null;
		LinkedList<Coordinate> cList = new LinkedList<Coordinate>();
		for (int i = 0; i < this.coordinateTimeLog.length; i++) {
			for (int j = 0; j < this.coordinateTimeLog[0].length; j++) {

				if (this.coordinateTimeLog[i][j] < min) {
					min = this.coordinateTimeLog[i][j];
					corMin = new Coordinate(j, i);
				}
				
				if (this.coordinateTimeLog[i][j] == 0) {
					cList.add(new Coordinate(j, i));
				}
				
			}
		}
		
		if (!cList.isEmpty()) {
			Random rndm = new Random();

	        int rndmNumber = rndm.nextInt(cList.size());
	        corMin = cList.get(rndmNumber);
		}
		
		moveTo(corMin);
		
		return corMin;
    	
    }
    
    public void recordTimeAndCoordinate(Coordinate c, int time) {
    	this.coordinateTimeLog[c.getY()][c.getX()] = time;
    	this.dataID++;
    }
    
    public int getPartnerID() {
    	return this.commParterID;
    }
    
    public void setPartnerID(int i) {
    	this.commParterID = i;
    }
    
    public boolean hasReached() {
    	return this.targetReached;
    }
    
    public void targetReached() {
    	this.targetReached = true;
    }
    
    public boolean isCommunicating() {
    	return this.inCommunication;
    }
    
    public void beginCommunication() {
    	this.inCommunication = true;
    	this.commPeriod = 0;
    }
    
    public void endCommunication() {
    	this.inCommunication = false;
    	this.commPeriod = 0;
    }
    
    public void updateCommPeriod() {
    	this.commPeriod += 10;
    }
    
    public int getcommPeriod() {
    	return this.commPeriod;
    }
    
    public void justExplored(Coordinate c) {
        this.explored.add(c);
    }
    
    public void exploredTotal(HashSet<Coordinate> h) {
    	this.explored = h;
    }

    public void justExploredInt(int c) {
        this.exploredInt.add(c);
    }
    public HashSet<Coordinate> getExplored() {
        return this.explored;
    }
    
    public HashSet<Integer> getExploredInt() {
        return this.exploredInt;
    }
    
    public void targetDiscovered(int id) {
        this.targetFound.add(id);
    }
    
    public HashSet<Integer> getDiscoveredTargets() {
        return this.targetFound;
    }
    
    public void setDiscoverdTargets(HashSet<Integer> h) {
        this.targetFound = h;
    }
    
    public int getID() {
    	return this.id;
    }
    
    public boolean isWaiting() {
    	return this.waiting;
    }
    
    public void waiting(boolean w) {
    	this.waiting = w;
    }
    
    public Task getCurrTask() {
    	return this.currTask;
    }
    
    public Coordinate getTaskLocation() {
    	
    	return null;
    }
    
    private void doPhaseOne(LinkedList<Integer> colList) {
    	LinkedList<Integer> toremove = new LinkedList<Integer>();
    	if (this.myCols.size() == colList.size()) {
    		if (this.myCols.getFirst() == colList.getFirst() && this.myCols.getLast() == colList.getLast()) {
    			int l = this.myCols.size();
    			
    			if (Math.random() < 0.5) {
	    			for (int i = l /2; i < l; i++) {
	    				toremove.add(this.myCols.get(i));
	    			}
    			} else {
	    			for (int i = 0; i < l / 2; i++) {
	    				toremove.add(this.myCols.get(i));
	    			}
    			}
    			
    		}
    	} else if (this.myCols.size() > colList.size()) {
    		
    		if (((2 * colList.size()) <= this.myCols.size() + 1)) {
    		
    			this.myCols.removeAll(colList);
    		
    		}

    	} else if (this.myCols.size() < colList.size() - 1) {
    		
    		if (((2 * this.myCols.size()) < colList.size() - 1) || (!(colList.contains(myCols.getFirst())) && !(colList.contains(myCols.getLast())))) {
				this.myCols.addAll(colList);
	
	    		if (this.myCols.getFirst() < colList.getFirst()) {
	    			int l = this.myCols.size();
	    			// remove the last half
	    			for (int i = l /2; i < l; i++) {
	    				toremove.add(this.myCols.get(i));
	    			}
	    		} else {
	    			// remove the first half
	    			int l = this.myCols.size();
	    			for (int i = 0; i < l / 2; i++) {
	    				toremove.add(this.myCols.get(i));
	    			}
	    		}
    		}
    		
    	}
    	this.dataID++;
    	this.myCols.removeAll(toremove);

    	moveC.setCols(this.myCols);
    	
    	moveTo(moveC.getNextCoordinate(currC));
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seeker other = (Seeker) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
