import java.awt.Color;
import java.awt.Graphics;

public class Obstacles extends Robot {
    
    private Color color;
    private int px;
    private int py;
    private int wd;
    private int ht;

    public Obstacles(int px, int py, int width, int height, int courtWidth, int courtHeight, Color color) {
        super(0, 0, px, py, width, height, courtWidth, courtHeight);
        
        this.px = px;
        this.py = py;
        this.wd = width;
        this.ht = height;
        
        this.color = color;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.px, this.py, this.wd, this.ht);
    }

}
