import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Simulation extends JPanel {
    
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 500;
    
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 10;
    
    private int cols = COURT_WIDTH / 100;
    private int rows = COURT_HEIGHT / 100;
    
    private Obstacles[] obs;
    
    private List<Seeker> seekers;
    private List<Targets> targets;
    
    private boolean simDone;
    
    private int time;
    
    private int targetsFound;
    
    private HashSet<Integer> targetsDiscovered = new HashSet<Integer>();
    
    
    
    public Simulation(JLabel status) {
        
        
        this.obs = new Obstacles[(COURT_WIDTH * COURT_HEIGHT) / 2500];
        
        int x = 20;
        int y = 20;
        
        for (int i = 0; i < this.obs.length; i++) {
            double r = Math.random();
            if (r < 0.55) {
                obs[i] = new Obstacles(x, y, 15, 15, COURT_WIDTH, COURT_HEIGHT, Color.RED);
            }
            
            if ((x + 75) < COURT_WIDTH) {
                x += 60;
            } else if ((y + 75) < COURT_HEIGHT) {
                y += 60;
                x = 20;
            } 
        }
        
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); 
        
        setFocusable(true);
        String m = "Time " + this.time + " ---------------- " + "Targets Found: 0";
        
        status.setText(m);
        
        this.status = status;

        
    }
    
    public void reset() {
        
        this.simDone = false;
        
        this.targetsFound = 0;
        
        this.time = 0;
        
        this.seekers = new LinkedList<Seeker>();
        
        this.seekers.add(new Seeker(1, -1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
        this.seekers.add(new Seeker(-1, 1, 5, 5, COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
        
        this.targets = new LinkedList<Targets>();
        this.targets.add(new Targets(-1, 1, 350, 350, 5, 5, COURT_WIDTH, COURT_HEIGHT, 1));
        this.targets.add(new Targets(1, -1, 350, 200, 5, 5, COURT_WIDTH, COURT_HEIGHT, 2));
    }
    
    
    public void tick() {
        if (!simDone) {
            
            this.time += 10;
            
            
        for (Targets t: this.targets) {
            t.move();
            t.bounce(t.hitWall());
            
            for (int i = 0; i < this.obs.length; i++) {
                if (obs[i] != null) {
                    Directions d = t.hitObj(obs[i]);
                    if (d != null) {
                        t.ObsNav(d);
                    }

                }
            }
            
            
        }
        
        for (Seeker s: this.seekers) {
            s.move();
            s.bounce(s.hitWall());
            s.justExplored(getAreaNum(s.getPx(), s.getPy()));
            
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
            
            for (Seeker r: this.seekers) {
                if (!s.equals(r)) {
                    // communication in range
                    if (s.distanceTo(r) < 50) {
                        
                        // check of obstacles 
                        boolean obsInPath = false;
                        for (int i = 0; i < this.obs.length; i++) {
                            if (this.obs[i] != null) {
                                if (inPath(s.getPx(), s.getPy(), r.getPx(), r.getPy(), this.obs[i].getPx(), this.obs[i].getPy())) {
                                    obsInPath = true;
                                    break;
                                }
                            }
                        }
                        
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
            }
            String m = "Time " + this.time + " ---------------- " + "Targets Found: " + this.targetsDiscovered.size();
            
            status.setText(m);
        }
        }
        repaint();
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 1; i < COURT_WIDTH / 100; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(i * 100, 0, COURT_WIDTH / 50, COURT_HEIGHT);
        }
        
        for (int i = 1; i < COURT_HEIGHT / 100; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(0, i * 100, COURT_WIDTH, COURT_HEIGHT / 50);
        }
        
        for (int i = 0; i < this.obs.length; i++) {

            if (obs[i] != null) {
                obs[i].draw(g);
            }
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
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}
