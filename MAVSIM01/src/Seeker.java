import java.awt.Color; 
import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Seeker extends Robot {
    
    private Color color;
    private int id;
    
    private HashSet<Integer> exploredInt = new HashSet<Integer>();
    private HashSet<Coordinate> explored = new HashSet<Coordinate>();
    private HashSet<Integer> targetFound = new HashSet<Integer>();
    
    private HashMap<Integer, HashSet<Integer>> dataRecieved;
    
    private Task currTask;
    
    
    private LinkedList<Integer> sendBuffer;
    
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
    
    private int succComm;
    
    private int dataID;
    
    private int[][] coordinateTimeLog;
    
    
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
        
        this.dataRecieved = new HashMap<Integer, HashSet<Integer>>();
        
        this.sendBuffer = new LinkedList<Integer>();
        
        this.dataID = 0;
        
        this.succComm = 0;
        
        this.howBusy = 0.60;
        
        this.failurePeriod = 0;
        
        this.coordinateTimeLog = new int[SimulationV3.Y_MAX + 1][SimulationV3.X_MAX + 1];
        
        this.currTask = new Task();
        
        getEarliest();
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(this.color);
        if (this.failurePeriod != 0) {
        	g.setColor(Color.YELLOW);
        }
        
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());  
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
    		} 
    	}
    	this.hasData = false;
    	this.d = null;
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
    
    public void commJammed() {
    	this.succComm--;
    	this.sendBuffer.removeFirst();
    	this.sendBuffer.addFirst(0);
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
