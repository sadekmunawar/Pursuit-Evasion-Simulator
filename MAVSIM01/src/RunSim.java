import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RunSim implements Runnable {


    @Override
    public void run() {
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(250, 25);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        
        final SimSettings set = new SimSettings();
        frame.add(set, BorderLayout.CENTER);
        
        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        final SimulationV2 sim = new SimulationV2(status);
        
        // listener to the reset button
        final JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(set);
                frame.add(sim, BorderLayout.CENTER);
                sim.reset();
            }
        });
        control_panel.add(start);
        
    //    frame.add(sim, BorderLayout.CENTER);



        // listener to the reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sim.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        //sim.reset();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunSim());
    }


}
