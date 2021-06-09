import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



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
    
    private int time;
    
    private int targetsFound;
    
    private HashSet<Roads> paths = new HashSet<Roads>();
    private HashSet<Integer> pathIDs = new HashSet<Integer>();
    
    private HashSet<Obstacles> obs = new HashSet<Obstacles>();

    
    
    private HashSet<Integer> targetsDiscovered = new HashSet<Integer>();
    
    Timer timer = new Timer(INTERVAL, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            tick();
        }
    });
    
    
    
    public SimulationV2(JLabel status) {
        
        
      
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
		paths = new HashSet<Roads>();
		for(int i = -55; i < 54; i++) {
		//	if (Math.random() < 0.75) {
			paths.add(new Roads(i, 7, 9));
			pathIDs.add(i);
		//	}
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
			
		//	if (i % 3 == 0) {
				
		//	} else if (i % 4 == 0) {
				
		//	} else if (i % 2 == 0) {
				
		//	} else {
				
		//	}
		}
        
        setFocusable(true);
        String m = "Time " + this.time + " ---------------- " + "Targets Found: 0";
        
        status.setText(m);
        
        this.status = status;

        
    }
    
    public void startTimer() {
    	this.timer.start();
    }
    
    public void reset() {
    	startTimer();
        
        this.simDone = false;
        
        this.targetsFound = 0;
        
        this.time = 0;
        
        this.seekers = new LinkedList<Seeker>();
        
        this.seekers.add(new Seeker(0, -1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
        this.seekers.add(new Seeker(1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
        
        this.targets = new LinkedList<Targets>();
        this.targets.add(new Targets(-1, 0, 485, 165, 5, 5, COURT_WIDTH, COURT_HEIGHT, 1));
        this.targets.add(new Targets(0, -1, 685, 75, 5, 5, COURT_WIDTH, COURT_HEIGHT, 2));
    }
    
    
    public void tick() {
        if (!simDone) {
            
            this.time += 10;
            
            
        for (Targets t: this.targets) {
        	
            
            
            t.move();
            t.bounce(t.hitWall());

            Coordinate c = getCoordinate(t.getPx(), t.getPy());
            
            if (c != null) {
            	t.go(getARandomDirection(c));
            }
            
        }
        
        for (Seeker s: this.seekers) {
            
            s.move();
            s.bounce(s.hitWall());
            s.justExplored(getAreaNum(s.getPx(), s.getPy()));
            
            Coordinate c = getCoordinate(s.getPx(), s.getPy());
            
            if (c != null) {
            	s.go(getARandomDirection(c));
            }
            
            for (Targets t: this.targets) {
                if (s.distanceTo(t) < 25) {
                    s.targetDiscovered(t.getID());
                    this.targetsDiscovered.add(t.getID());
                    
                }
            }
            
         /*   for (int i = 0; i < this.obs.length; i++) {
                if (obs[i] != null) {
                    Directions d = s.hitObj(obs[i]);
                    if (d != null) {
                        s.ObsNav(d);
                    }

                }
            }  */
            
        /*    for (Seeker r: this.seekers) {
                if (!s.equals(r)) {
                    // communication in range
                    if (s.distanceTo(r) < 50) {
                        
                        // check of obstacles 
                        boolean obsInPath = false;

                        
                        // if no obstacles in the path
                        if (!obsInPath) {
                            HashSet<Integer> sSet = s.getDiscoveredTargets();
                            HashSet<Integer> rSet = r.getDiscoveredTargets();
                            
                            sSet.addAll(rSet);
                            s.setDiscoverdTargets(sSet);
                            r.setDiscoverdTargets(sSet);
                            
                            this.targetsFound = sSet.size();
                            // if all targets found
                            if (allTargetsFound(sSet)) {
                                
                                this.simDone = true;
                                // simulation ends
                                
                            } else {
                                // if all targets not found
                                HashSet<Integer> sExplored = s.getExplored();
                                HashSet<Integer> rExplored = r.getExplored();
                                
                                sExplored.addAll(rExplored);
                                
                                HashSet<Integer> unexp = unexplored(sExplored);
                                
                                if (unexp.size() > this.seekers.size()) {
                                    Integer[] arr = unexp.toArray(new Integer[unexp.size()]);
                                    
                                    
                                    Random rndm = new Random();
                                    int rndmNumber = rndm.nextInt(unexp.size());
                                    int sTar = arr[rndmNumber];
                                    
                                    Random rndm2 = new Random();
                                    int rndmNumber2 = rndm2.nextInt(unexp.size());
                                    int rTar = arr[rndmNumber2];
                                    
                                    int sTarx = getX(sTar);
                                    int sTary = getY(sTar);
                                    
                                    s.sendTo(s.getPx(), s.getPy(), sTarx, sTary);
                                                    
                                    
                                    int rTarx = getX(rTar);
                                    int rTary = getY(rTar);
                                    r.sendTo(r.getPx(), r.getPy(), rTarx, rTary);
                                    
                                       
                                } else {
                                    Integer[] arr = sExplored.toArray(new Integer[sExplored.size()]);
                                    
                                    Random rndm = new Random();
                                    int rndmNumber = rndm.nextInt(sExplored.size());
                                    int sTar = arr[rndmNumber];
                                    
                                    Random rndm2 = new Random();
                                    int rndmNumber2 = rndm2.nextInt(sExplored.size());
                                    int rTar = arr[rndmNumber2];
                                    
                                    int sTarx = getX(sTar);
                                    int sTary = getY(sTar);
                                    
                                    s.sendTo(s.getPx(), s.getPy(), sTarx, sTary);
                                    
                                    
                                    
                                    int rTarx = getX(rTar);
                                    int rTary = getY(rTar);
                                    r.sendTo(r.getPx(), r.getPy(), rTarx, rTary);
                                    
                                }
                                
                                // pick two unexplored location randomly and send both in different directions
                                
                                
                                
                            }
                            
  
                        }
                        // if not share the areas explored and the targets found
                        // if both targets are found 
                        // seeker makes decision based on the new info
                    }
                }
            } */
            String m = "Time " + this.time + " ---------------- " + "Targets Found: " + this.targetsDiscovered.size();
            
            status.setText(m);
        }
        }
        repaint();
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
    		l.add(Directions.N);
    	}
    	if (this.pathIDs.contains(Roads.getSouth(c))) {
    		l.add(Directions.S);
    	}
    	if (this.pathIDs.contains(Roads.getWest(c))) {
    		l.add(Directions.W);
    	}
    	if (this.pathIDs.contains(Roads.getEast(c))) {
    		l.add(Directions.E);
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
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}