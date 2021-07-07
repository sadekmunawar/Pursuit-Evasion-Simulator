import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.opencsv.CSVWriter;



@SuppressWarnings("serial")
public class SimulationV2 extends JPanel {
    
    private JLabel status; // Current status text
    
    // constants
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 550;
    
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 10;
    
    private int cols = COURT_WIDTH / 100;
    private int rows = COURT_HEIGHT / 100;
        
    private List<Seeker> seekers;
    private List<Targets> targets;
    
    private boolean simDone;
    
    private GridType gType = GridType.Full;
    
    private int numTrials;
    
    private int time;
    
    private int targetsFound;
    
    private int interference;
    
    private HashMap<Seeker, HashSet<Data>> data;
    
    private HashSet<Roads> paths;
    private HashSet<Integer> pathIDs;
    
    private HashSet<Obstacles> obs = new HashSet<Obstacles>();
    
    
    private int successfulEvaders = 0;
    
    private int totalEvaders;

    private HashSet<Communicators> comm;
    
    private HashMap<Integer, Integer> commTime;
    
    private HashSet<Coordinate> allCoord;
    
    private Strategies seekerStrategy = Strategies.Random;
    
    
    private CSVWriter cvwriter;
    
    
   //// private HashSet<Integer> targetsDiscovered;
    
    Timer timer =  new Timer(INTERVAL, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            tick();
        }
    });
    
    
    
    public SimulationV2(JLabel status) {
    	
    	this.numTrials = 0;
        
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
  /*	paths = new HashSet<Roads>();
		for(int i = -55; i < 54; i++) {
			if (Math.random() < 0.75) {
			paths.add(new Roads(i, 7, 9));
			pathIDs.add(i);
			}
		} */
        
        File file = new File("files/strat1_2.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
      
            // create CSVWriter object filewriter object as parameter
            this.cvwriter = new CSVWriter(outputfile);
      
            // adding header to csv
            String[] header = { "Trials", "Caught", "Successful", "Time" };
            this.cvwriter.writeNext(header);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		for (int i = 1; i < 49; i++) {
			
			int col = i % 8;
			int row = i / 8;
			
			int offset = 10;
			int offset2 = 30;
			if (row == 0 || col == 0) {
				offset = 20;
				offset2 = 15;
			}
			
			
			if (i % 6 != 0) {
				if (i % 3 == 0) {
					this.obs.add(new Obstacles((col * 100) + offset, (row * 90) + offset, 20, 15, COURT_WIDTH, COURT_HEIGHT, Color.RED, true));
					
					this.obs.add(new Obstacles((col * 100) + offset + offset2, (row * 90) + offset + offset2, 20 , 10, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
				} else if (i % 4 == 0) {
					this.obs.add(new Obstacles((col * 100) + offset, (row * 90) + offset, 15, 15, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
					
					this.obs.add(new Obstacles((col * 100) + offset + offset2, (row * 90) + offset + offset2, 10 , 20, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
					
					if (row != 0) {
					this.obs.add(new Obstacles((col * 100) + offset + 30, (row * 90), 15, 20, COURT_WIDTH, COURT_HEIGHT, Color.RED, true));
					}
				} else if (i % 2 == 0) {
					this.obs.add(new Obstacles((col * 100) + offset, (row * 90) + offset, 10 , 20, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
					
					this.obs.add(new Obstacles((col * 100) + offset + offset2, (row * 90) + offset + offset2, 15, 15, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
				} else {
					this.obs.add(new Obstacles((col * 100) + offset, (row * 90) + offset, 20 , 10, COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
					
					this.obs.add(new Obstacles((col * 100) + offset + offset2, (row * 90) + offset + offset2, 20, 15, COURT_WIDTH, COURT_HEIGHT, Color.RED, true));
				}
			}
		}
        
        setFocusable(true);
        String m = "Time " + this.time + " ---------------- " + "Evaders Found: 0" + " ---------------- " + "Evaders Successful: 0" + " -------------- "
        			+ "Trial: " + this.numTrials;
        
        status.setText(m);
        
        this.status = status;
    }
    
    public void startTimer() {
    	this.timer.start();
    }
    
    public void addRoads() {
		this.paths = new HashSet<Roads>();
		this.pathIDs = new HashSet<Integer>();
		
		for(int i = -55; i < 54; i++) {
			
			if (this.gType == GridType.Random) {
				if (Math.random() < 0.85) {
					paths.add(new Roads(i, 7, 9));
					pathIDs.add(i);
				}
			} else if (this.gType == GridType.Full) {
				paths.add(new Roads(i, 7, 9));
				pathIDs.add(i);
			}
		}
    }
    
    public void setGridType(GridType g) {
    	this.gType = g;
    }
    
    public void setSeekerStrategy(Strategies s) {
    	this.seekerStrategy = s;
    }
    
    public void reset() {
    	startTimer();
    	
    	this.numTrials++;
    	
    	this.interference = 0;
    	
    	this.allCoord = new HashSet<Coordinate>();
    	
    	for (int i = 0; i < 9; i++) {
    		for (int j = 0; j < 7; j++) {
    			this.allCoord.add(new Coordinate(i, j));
    		}
    	}
        
    	this.commTime = new HashMap<Integer, Integer>();
    	this.comm = new HashSet<Communicators>();
    	
        this.simDone = false;
        
        this.successfulEvaders = 0;
        
        this.targetsFound = 0;
       // targetsDiscovered = new HashSet<Integer>();
        
        this.time = 0;
        
        this.seekers = new LinkedList<Seeker>();
        
        this.seekers.add(new Seeker(new Coordinate (0, 0), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 1));
        this.seekers.add(new Seeker(new Coordinate (0, 1), 1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 2));
        this.seekers.add(new Seeker(new Coordinate (1, 0), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 3));
     //   this.seekers.add(new Seeker(new Coordinate (1, 1), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 4));
      //  this.seekers.add(new Seeker(new Coordinate (0, 2), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 5));
      //  this.seekers.add(new Seeker(new Coordinate (1, 2), -1, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 6));
        
        this.targets = new LinkedList<Targets>();
      //  this.targets.add(new Targets(new Coordinate (5, 1), 1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, 1));
      //  this.targets.add(new Targets(new Coordinate (6, 1), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, 2));
        this.targets.add(new Targets(new Coordinate (7, 0), -1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, 3));
        this.targets.add(new Targets(new Coordinate (8, 0), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, 4));
    //    this.targets.add(new Targets(new Coordinate (6, 0), 0, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, 5));
        this.targets.add(new Targets(new Coordinate (7, 1), 1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, 6));
        
        this.totalEvaders = this.targets.size();
    }
    
    
    public void tick() {
        if (!simDone) {
            
            this.time += INTERVAL;
            
        HashSet<Targets> toRemove1 = new HashSet<Targets>();  
        
        for (Targets t: this.targets) {
        	
            t.move();
            t.bounce(t.hitWall());

            Coordinate c = getCoordinate(t.getPx(), t.getPy());
            
            if (c != null) {
            	t.go(getARandomDirection(c));
            }
            
            if (t.getPy() > 520) {
            	this.successfulEvaders++;
            	toRemove1.add(t);
            }
        }
        
        this.targets.removeAll(toRemove1);
        
        this.commTime.replaceAll((k, v) -> v + 10);
        
        for (Seeker s: this.seekers) {
            if (!s.isCommunicating() && !s.isWaiting()) {
            	s.move();
            } else {
            	s.updateCommPeriod();
            	if(s.getcommPeriod() > 100) {
            		s.endCommunication();
            		s.setPartnerID(-1);
            		s.waiting(false);
            	}
            }
            s.bounce(s.hitWall());
            //  s.justExplored(getAreaNum(s.getPx(), s.getPy()));
            
            Coordinate c = getCoordinate(s.getPx(), s.getPy());
            
            if (c != null) {
            	
            	
            	s.justExplored(c);
            
	            if (this.seekerStrategy == Strategies.Random) {
	            	s.go(getARandomDirection(c));
	            } else if (this.seekerStrategy == Strategies.Strategy1) {
	            	if (!s.hasReachedTarget() && s.getTargetLoc() != null) {
		            	if (c.equals(s.getTargetLoc())) {
		            		s.targetReached();
		            		s.go(getARandomDirection(c));
		            	} else {
		            		s.go(getTargetDirection(c, s.getTargetLoc()));
		            	}
	            	} else {
	            		s.go(getARandomDirection(c));
	            	}
	            }
            }
            
            HashSet<Targets> toRemove = new HashSet<Targets>();
            
            for (Targets t: this.targets) {
                if (s.distanceTo(t) < 60) {
                    s.targetDiscovered(t.getID());
                    this.targetsFound++;
                   // this.targetsDiscovered.add(t.getID());
                    toRemove.add(t);
                }
            }
            
            this.targets.removeAll(toRemove);
            
            if (s.isCommunicating()) {
            	continue;
            }
            
            for (Seeker r: this.seekers) {
                if (!s.equals(r)) {
                	
                	
                    // communication in range
                    if (s.distanceTo(r) < 120) {
                        
                        // check of obstacles 
                        boolean obsInPath = false;
                        for (Obstacles o: this.obs) {
                            
                        	if (inPath(s.getPx(), s.getPy(), r.getPx(), r.getPy(), o.getPx() + (o.getWidth() / 2), o.getPy() + (o.getHeight() / 2))) {
                        		obsInPath = true;
                                	break;
                            }
                        }

                        
                        // if no obstacles in the path
                        if (!obsInPath) {

                        	
                        	int commID = getCommunicationID(s, r);
                        	if (this.commTime.containsKey(commID)) {
                        		if (this.commTime.get(commID) > 2500) {
                        			
                                	if (r.isCommunicating()) {
                                		if (!s.isWaiting()) {
                                			this.interference++;
                                		}
                                		s.waiting(true);
                                		r.waiting(true);
                                		continue;
                                	}
                                	
                                	s.waiting(false);
                                	r.waiting(false);
                                	
                                	s.setPartnerID(r.getID());
                                	r.setPartnerID(s.getID());
                                	
                        			s.beginCommunication();
                        			r.beginCommunication();
                        			
                        			// start of communication
                        			
                        			//communicate 
                        			if (this.seekerStrategy == Strategies.Strategy1) {
                        			HashSet<Coordinate> sExplored = s.getExplored();
                        			HashSet<Coordinate> rExplored = s.getExplored();
        
                        			
                        			sExplored.addAll(rExplored);
                        			
                        			s.exploredTotal(sExplored);
                        			r.exploredTotal(sExplored);
                        			
                        			HashSet<Coordinate> all = new HashSet<Coordinate>();
                        			all = this.allCoord;
                        			
                        			all.removeAll(sExplored);
                        			
                        			if (all.size() == 1) {
                        				ArrayList<Coordinate> alist = new ArrayList<Coordinate>(all);
	                        	        
	                        	        s.moveTo(getARandomCoordinate());
	                        	        r.moveTo(alist.get(0));
                        				
                        			} else if (all.size() == 0) {
                        			
	                        	        s.moveTo(getARandomCoordinate());
	                        	        r.moveTo(getARandomCoordinate());
                        	        
                        			} else {
                        				// more than two unexplored
                        				ArrayList<Coordinate> alist = new ArrayList<Coordinate>(all);
                        				
	                        	        Random rndm = new Random();
	                        	        
	                        	        // this will generate a random number between 0 and
	                        	        // HashSet.size - 1
	                        	        int rndmNumber = rndm.nextInt(alist.size());
	                        	        int rndmNumber2 = rndm.nextInt(alist.size());
	                        	        
	                        	        s.moveTo(alist.get(rndmNumber));
	                        	        r.moveTo(alist.get(rndmNumber2));
                        				
                        			}
                        			
                        			} else if (this.seekerStrategy == Strategies.Strategy2) {
                        				
                        			}

                        			this.commTime.put(commID, 0);
                        		
                        		} else {

                        		}
                        		
                        	} else {
                        		this.commTime.put(commID, 0);
                        		this.comm.add(new Communicators(s, r));
                        		
                            	if (r.isCommunicating()) {
                            		if (!s.isWaiting()) {
                            			this.interference++;
                            		}
                            		s.waiting(true);
                            		r.waiting(true);
                            		continue;
                            	}
                            	
                            	s.waiting(false);
                            	r.waiting(false);
                            	
                            	s.setPartnerID(r.getID());
                            	r.setPartnerID(s.getID());
                        		
                    			s.beginCommunication();
                    			r.beginCommunication();
                        		
                        		// communicate
                    			if (this.seekerStrategy == Strategies.Strategy1) {
                    			HashSet<Coordinate> sExplored = s.getExplored();
                    			HashSet<Coordinate> rExplored = s.getExplored();
    
                    			
                    			sExplored.addAll(rExplored);
                    			
                    			s.exploredTotal(sExplored);
                    			r.exploredTotal(sExplored);
                    			
                    			HashSet<Coordinate> all = new HashSet<Coordinate>();
                    			all = this.allCoord;
                    			
                    			all.removeAll(sExplored);
                    			
                    			if (all.size() == 1) {
                    				ArrayList<Coordinate> alist = new ArrayList<Coordinate>(all);
                        	        
                        	        s.moveTo(getARandomCoordinate());
                        	        r.moveTo(alist.get(0));
                    				
                    			} else if (all.size() == 0) {
                    			
                        	        s.moveTo(getARandomCoordinate());
                        	        r.moveTo(getARandomCoordinate());
                    	        
                    			} else {
                    				// more than two unexplored
                    				ArrayList<Coordinate> alist = new ArrayList<Coordinate>(all);
                    				
                        	        Random rndm = new Random();
                        	        
                        	        // this will generate a random number between 0 and
                        	        // HashSet.size - 1
                        	        int rndmNumber = rndm.nextInt(alist.size());
                        	        int rndmNumber2 = rndm.nextInt(alist.size());
                        	        
                        	        s.moveTo(alist.get(rndmNumber));
                        	        r.moveTo(alist.get(rndmNumber2));
                    				
                    			}
                    			
                    			}
                        		
                        	}
                        	
                        	
                        	}
  
                    }
                }
            } 
        }
        String m = "Time " + this.time + " ---------------- " + "Evaders Found: " + this.targetsFound + 
        		" ---------------- " + "Evaders Successful: " + this.successfulEvaders + " -------------- " + "Trial: " + this.numTrials + "------ Communication Interference: "
        		+ this.interference;
        
        status.setText(m);
        
        if (this.targetsFound + this.successfulEvaders == this.totalEvaders) {
       
        	
        	String[] s = {this.numTrials + "", this.targetsFound + "", this.successfulEvaders + "", this.time + ""};
        	
        	this.cvwriter.writeNext(s);
        	
        	if (this.numTrials >= 25) {
	        	try {
					this.cvwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	this.simDone = true;

        }
        
        }
        repaint();
        
        if (this.simDone) {
	        if (this.numTrials < 25 || this.time > 120000) {
	        	reset();
	        }
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for(Roads r: this.paths) {
        //	if (Math.random() < 0.90) {
        		r.draw(g);
        	//}
        }
        
        for (Obstacles o: this.obs) {
        	o.draw(g);
        }
        
        for (Seeker s: this.seekers) {
            s.draw(g);
        }
        
        for (Targets t: this.targets) {
            t.draw(g);
        }
        
        for (Communicators c: this.comm) {
        	if (c.getR1().isCommunicating() && c.getR2().isCommunicating() && c.getR1().getPartnerID() == c.getR2().getID()) {
        		g.setColor(Color.BLUE); 
        		g.drawLine(c.getR1().getPx(), c.getR1().getPy(), c.getR2().getPx(), c.getR2().getPy());
        	}
        	
        }
    }
    
    private int getAreaNum(int px, int py) {
        int col = px / 100;
        int row = py / 100;
        
        
        return (row * this.cols + col);
    }
    
    private boolean inPath(int pX1, int pY1, int pX2, int pY2, int oX, int oY) {
        int a = -1 * (pY2 -pY1);
        int b = pX2 - pX1;
        int c = (pX1 * (pY2 - pY1)) - (pY1 * (pX2 - pX1));
        double d1 = Math.abs((a * oX) + (b * oY) + c);
        double d2 = Math.sqrt((a * a) + (b * b));
        double d = d1 / d2;
        
        if (d < 15) {
            return true;
        }
        
        return false;
    }
    
    private boolean allTargetsFound(HashSet<Integer> h) {
      
      /*  for(Targets t: this.targets) {
            if (!h.contains(t.getID())) {
                return false;
            }
        } */
        
        return h.size() == this.targets.size();
    }
    
    private HashSet<Integer> unexplored(HashSet<Integer> h) {
        HashSet<Integer> k = new HashSet<Integer>();
        k.addAll(h);
        for (int i = 0; i < this.rows * this.cols; i++) {
            if (h.contains(i)) {
                k.remove(i);
            }
        }
        return k;
    }
    
    private int getX(int areaNum) {
        
        int colNo = areaNum % this.rows;
        
        return 50 + (100 * colNo);
        
        
    }
    
    private int getY(int areaNum) {
        
        int rowNo = areaNum / this.cols;
        
        return 50 + (100 * rowNo);
        
        
    }
    
    private Directions getARandomDirection(Coordinate c) {
    	LinkedList<Directions> l =  new LinkedList<Directions>();
    	if (this.pathIDs.contains(Roads.getNorth(c))) {
    		if (c.getY() != 0) {
    			l.add(Directions.N);
    		}
    	}
    	if (this.pathIDs.contains(Roads.getSouth(c))) {
    		if (c.getY() != 6) {
    			l.add(Directions.S);
    		}
    	}
    	if (this.pathIDs.contains(Roads.getWest(c))) {
    		if (c.getX() != 0) {
    			l.add(Directions.W);
    		}
    		
    	}
    	if (this.pathIDs.contains(Roads.getEast(c))) {
    		if (c.getX() != 8) {
    			l.add(Directions.E);
    		}
    	}
    	int randomNum = ThreadLocalRandom.current().nextInt(0, l.size());
    	
    	return l.get(randomNum);
    }
    private Directions getTargetDirection(Coordinate cur, Coordinate tar) {
    	LinkedList<Directions> l =  new LinkedList<Directions>();
    	
    	if (tar.getX() > cur.getX()) {
    		l.add(Directions.E);
    	} else if (tar.getX() < cur.getX()) {
    		l.add(Directions.W);
    	}
    	
    	if (tar.getY() > cur.getY()) {
    		l.add(Directions.S);
    	} else if (tar.getY() < cur.getY()) {
    		l.add(Directions.N);
    	}
    	
    	int randomNum = ThreadLocalRandom.current().nextInt(0, l.size());
    	return l.get(randomNum);
    }
    
    private Coordinate getCoordinate(int pX, int pY) {
		int x1 = (int)Math.round((double)pX / 100.0);
		int y1 = (int)Math.round((double)pY / 90.0);
		
		if (pX <= 12) {
			x1 = 0;
		}
		
		if (pY <= 12) {
			y1 = 0;
		}
		
		Coordinate c = new Coordinate(x1, y1);
		
		if (pX >= (c.getPx() - 0) && pX <= (c.getPx() + 0)) {
			if (pY >= (c.getPy() - 0) && pY <= (c.getPy() + 0)) {
				return c;
			}
		} 
		
		return null;
		
	}
    
    private Coordinate getARandomCoordinate() {
    	ArrayList<Coordinate> ran = new ArrayList<Coordinate>(this.allCoord);
		
        Random rndm = new Random();
        
        // this will generate a random number between 0 and
        // HashSet.size - 1
        int rndmNumber = rndm.nextInt(ran.size());
        return ran.get(rndmNumber);  
    }
    
    // use a hash function function
    private int getCommunicationID(Seeker s1, Seeker s2) {
    	return (s1.getID() * s1.getID()) + (s2.getID() * s2.getID());
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public static void writeDataLineByLine(String filePath)
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
      
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
      
            // adding header to csv
          //  String[] header = { "Name", "Class", "Marks" };
         //   writer.writeNext(header);
      
            // add data to csv
            String[] data1 = { "bb", "10", "620" };
            writer.writeNext(data1);
            String[] data2 = { "uu", "10", "630" };
            writer.writeNext(data2);
      
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}