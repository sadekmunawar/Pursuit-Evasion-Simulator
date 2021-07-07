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
		
		
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
      //  g.drawString("Seeker Attributes", COURT_WIDTH / 2, 20);
        

    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}
