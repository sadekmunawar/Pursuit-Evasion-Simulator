import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class RunSim implements Runnable {
	

    @Override
    public void run() {
    	
    	
        final JFrame frame = new JFrame("Simulation");
        frame.setLocation(250, 25);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        /***********SIM PANEL*********/
        final SimulationV3 sim = new SimulationV3(status);
        
        
        JPanel set = new JPanel();
        set.setLayout(new GridLayout(5, 4));
        
        /*******************THE SETTINGS PANEL **********************************/
        
        //The grid
        JLabel grid = new JLabel("The Grid :");
        
        JRadioButton fullGrid = new JRadioButton("Full");
        fullGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setGridType(GridType.Full);
            	sim.addRoads();
            }
        });
        
        JRadioButton ranGrid = new JRadioButton("Randomly Modified");
        ranGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setGridType(GridType.Random);
            	sim.addRoads();
            }
        });
        
        
        
        ButtonGroup gridGroup = new ButtonGroup();
        gridGroup.add(fullGrid);
        gridGroup.add(ranGrid);
        
        
        JLabel spaceHolder = new JLabel("     ");
        
        set.add(grid);
        set.add(fullGrid);
        set.add(ranGrid);
        set.add(spaceHolder);
        
        /*******************Seeker Buttons **********************************/
        set.add(new JLabel("Seekers :"));
        
 
        JRadioButton seekerRandom = new JRadioButton("Random");
        seekerRandom.setToolTipText("The seeker will move randomly");
        seekerRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Random);
            }
        });
        
        
        JRadioButton seekerStr1 = new JRadioButton("Strategy 1");
        seekerStr1.setToolTipText("Communication: share the locations explored and move toward unexplored locations");
        seekerStr1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy1);
            }
        });
        
        JRadioButton seekerStr2 = new JRadioButton("Strategy 2");
        seekerStr2.setToolTipText(" ");
        seekerStr2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy2);
            }
        });
        
        
        JRadioButton seekerStr3 = new JRadioButton("Strategy 3");
        seekerStr3.setToolTipText(" ");
        seekerStr3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy3);
            }
        });
        
        JRadioButton seekerStr4 = new JRadioButton("Strategy 4");
        seekerStr4.setToolTipText(" ");
        seekerStr4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy4);
            }
        });
        
        JRadioButton seekerStr5 = new JRadioButton("Strategy 5");
        seekerStr5.setToolTipText(" ");
        seekerStr5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy5);
            }
        });
        
        JRadioButton seekerStr6 = new JRadioButton("Strategy 6");
        seekerStr6.setToolTipText(" ");
        seekerStr6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setSeekerStrategy(Strategies.Strategy6);
            }
        });
        
        ButtonGroup seekerAttr = new ButtonGroup();
        seekerAttr.add(seekerRandom);
        seekerAttr.add(seekerStr1);
        seekerAttr.add(seekerStr2);
        seekerAttr.add(seekerStr3);
        seekerAttr.add(seekerStr4);
        seekerAttr.add(seekerStr5);
        seekerAttr.add(seekerStr6);
        

        set.add(seekerRandom);
        set.add(seekerStr1);
        set.add(seekerStr2);
        set.add(seekerStr6);
        set.add(seekerStr3);
        set.add(seekerStr4);
        set.add(seekerStr5);
        
        
        
        /*******************Evader Buttons **********************************/
        set.add(new JLabel("Evaders :"));
        
        JRadioButton evaderRandom = new JRadioButton("Random");
        evaderRandom.setToolTipText("The evaders will move randomly");
        evaderRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Random);
            }
        });
        
        JRadioButton southBiased = new JRadioButton("South Biased");
        southBiased.setToolTipText("35% S, 15% N, 25% E, 25% W");
        southBiased.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Strategy1);
            }
        });
        
        JRadioButton doubleSpeed = new JRadioButton("Double Speed");
        doubleSpeed.setToolTipText("Evaders are twice as fast as pursuers");
        doubleSpeed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Strategy2);
            }
        });
        
        JRadioButton betterSight = new JRadioButton("Better Sight");
        betterSight.setToolTipText("Evaders can detect pursuers and reverses direction before they can be caught");
        betterSight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Strategy3);
            }
        });
        
        JRadioButton southBiasedNBetterSight = new JRadioButton("South Biased + Better Sight");
        southBiasedNBetterSight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Strategy4);
            }
        });
        
        
        JRadioButton doubleSpeedNBetterSight = new JRadioButton("Double Speed + South Biased + Better Sight");
        doubleSpeedNBetterSight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.setEvaderStrategy(Strategies.Strategy5);
            }
        });
        
        ButtonGroup evaderAttr = new ButtonGroup();
        evaderAttr.add(evaderRandom);
        evaderAttr.add(southBiased);
        evaderAttr.add(doubleSpeed);
        evaderAttr.add(betterSight);
        evaderAttr.add(southBiasedNBetterSight);
        evaderAttr.add(doubleSpeedNBetterSight);

        
        set.add(evaderRandom);
        set.add(southBiased);
        set.add(doubleSpeed);
        set.add(new JLabel("     "));
        set.add(betterSight);
        set.add(southBiasedNBetterSight);
        set.add(doubleSpeedNBetterSight);
        
        
        frame.add(set, BorderLayout.CENTER);
        
        /*******************THE SETTINGS ENDS **********************************/
        
        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // listener to the reset button
        final JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	frame.add(sim, BorderLayout.CENTER);
                frame.remove(set);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                //frame.add(sim, BorderLayout.CENTER);
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
