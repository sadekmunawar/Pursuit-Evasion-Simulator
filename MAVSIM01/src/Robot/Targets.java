import java.awt.Color;
import java.awt.Graphics;

public class Targets extends Robot {
    
    private int id;

    public Targets(Coordinate c, int vx, int vy, int width, int height, int courtWidth, int courtHeight, int id) {
        super(vx, vy, c.getPx(), c.getPy(), width, height, courtWidth, courtHeight);
        // TODO Auto-generated constructor stub
        
        this.id = id;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());  
    }
    
    public int getID() {
        return this.id;
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
    
    
    

}
