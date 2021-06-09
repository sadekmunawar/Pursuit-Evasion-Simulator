import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SimSettings extends JPanel {
	
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 550;
    
    private HashSet<Roads> h = new HashSet<Roads>();
	
	public SimSettings() {
		
		 h = new HashSet<Roads>();
		for(int i = -55; i < 54; i++) {
			h.add(new Roads(i, 7, 9));
		}
		
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.black);
        g.fillRect(300, 300, 50, 50);
        
        for(Roads r: this.h) {
       // 	if (Math.random() < 0.90) {
        		r.draw(g);
      //  	}
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}
