import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

public class Seeker extends Robot {
    
    private Color color;
    
    private HashSet<Integer> explored = new HashSet<Integer>();
    private HashSet<Integer> targetFound = new HashSet<Integer>();
    
    private boolean inCommunication;

    public Seeker(int vx, int vy, int width, int height, int courtWidth, int courtHeight, Color color) {
        super(vx, vy, 285, 75, width, height, courtWidth, courtHeight);
        // TODO Auto-generated constructor stub
        
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(this.color);
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
    
    public boolean isCommunicating() {
    	return this.inCommunication;
    }
    
    public void beginCommunication() {
    	this.inCommunication = true;
    }
    
    public void endCommunication() {
    	this.inCommunication = false;
    }
    
    public void justExplored(int a) {
        this.explored.add(a);
    }

    public HashSet<Integer> getExplored() {
        return this.explored;
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

}
