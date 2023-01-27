import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: GUI for pursuit-evasion simulation.
 */
public class RunSim implements Runnable {


  public static void main(String[] args) {
    SwingUtilities.invokeLater(new RunSim());
  }

  @Override
  public void run() {


    final JFrame frame = new JFrame("Simulation");
    frame.setLocation(100, 25);

    // Status panel
    final JPanel status_panel = new JPanel();
    frame.add(status_panel, BorderLayout.SOUTH);
    final JLabel status = new JLabel("Running...");
    status_panel.add(status);

    /*********** SIM PANEL *********/
    final SimulationV3 sim = new SimulationV3(status);


    final JPanel set = new JPanel();
    set.setLayout(new BorderLayout(10, 10));
    // set.setLayout(new GridLayout(5, 4));

    /******************* THE SETTINGS PANEL **********************************/
    final JPanel theGrid = new JPanel();
    theGrid.setLayout(new BoxLayout(theGrid, BoxLayout.Y_AXIS));

    // The grid
    final JLabel grid = new JLabel("The Grid :");

    final JRadioButton fullGrid = new JRadioButton("Full");
    fullGrid.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setGridType(GridType.Full);
        sim.addRoads();
      }
    });

    final JRadioButton ranGrid = new JRadioButton("Randomly Modified");
    ranGrid.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setGridType(GridType.Random);
        sim.addRoads();
      }
    });


    final ButtonGroup gridGroup = new ButtonGroup();
    gridGroup.add(fullGrid);
    gridGroup.add(ranGrid);


    theGrid.add(grid);
    theGrid.add(fullGrid);
    theGrid.add(ranGrid);

    theGrid.add(Box.createRigidArea(new Dimension(30, 30)));

    /******************* Seeker Buttons **********************************/
    final JPanel theSeekers = new JPanel();
    theSeekers.setLayout(new BoxLayout(theSeekers, BoxLayout.Y_AXIS));

    theSeekers.add(new JLabel("Seekers :"));


    final JRadioButton seekerRandom = new JRadioButton("Random");
    seekerRandom.setToolTipText("The seeker will move randomly");
    seekerRandom.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Random);
      }
    });


    final JRadioButton seekerStr1 = new JRadioButton("Strategy 1");
    seekerStr1.setToolTipText(
        "Communication: share the locations explored and move toward unexplored locations");
    seekerStr1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy1);
      }
    });

    final JRadioButton seekerStr2 = new JRadioButton("Strategy 2");
    seekerStr2.setToolTipText(" ");
    seekerStr2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy2);
      }
    });


    final JRadioButton seekerStr3 = new JRadioButton("Strategy 3");
    seekerStr3.setToolTipText(" ");
    seekerStr3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy3);
      }
    });

    final JRadioButton seekerStr4 = new JRadioButton("Strategy 4");
    seekerStr4.setToolTipText(" ");
    seekerStr4.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy4);
      }
    });

    final JRadioButton seekerStr5 = new JRadioButton("Strategy 5");
    seekerStr5.setToolTipText(" ");
    seekerStr5.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy5);
      }
    });

    final JRadioButton seekerStr6 = new JRadioButton("Strategy 6");
    seekerStr6.setToolTipText(" ");
    seekerStr6.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setSeekerStrategy(Strategies.Strategy6);
      }
    });

    final ButtonGroup seekerAttr = new ButtonGroup();
    seekerAttr.add(seekerRandom);
    seekerAttr.add(seekerStr1);
    seekerAttr.add(seekerStr2);
    seekerAttr.add(seekerStr3);
    seekerAttr.add(seekerStr4);
    seekerAttr.add(seekerStr5);
    seekerAttr.add(seekerStr6);

    theSeekers.add(seekerRandom);
    theSeekers.add(seekerStr1);
    theSeekers.add(seekerStr2);
    theSeekers.add(seekerStr3);
    theSeekers.add(seekerStr4);
    theSeekers.add(seekerStr5);
    theSeekers.add(seekerStr6);

    theSeekers.add(Box.createRigidArea(new Dimension(15, 0)));


    /******************* Evader Buttons **********************************/
    final JPanel theEv = new JPanel();
    theEv.setLayout(new BoxLayout(theEv, BoxLayout.Y_AXIS));

    theEv.add(new JLabel("Evaders :"));

    final JRadioButton evaderRandom = new JRadioButton("Random");
    evaderRandom.setToolTipText("The evaders will move randomly");
    evaderRandom.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Random);
      }
    });

    final JRadioButton southBiased = new JRadioButton("South Biased");
    southBiased.setToolTipText("35% S, 15% N, 25% E, 25% W");
    southBiased.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Strategy1);
      }
    });

    final JRadioButton doubleSpeed = new JRadioButton("Double Speed");
    doubleSpeed.setToolTipText("Evaders are twice as fast as pursuers");
    doubleSpeed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Strategy2);
      }
    });

    final JRadioButton betterSight = new JRadioButton("Better Sight");
    betterSight.setToolTipText(
        "Evaders can detect pursuers and reverses direction before they can be caught");
    betterSight.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Strategy3);
      }
    });

    final JRadioButton southBiasedNBetterSight = new JRadioButton("South Biased + Better Sight");
    southBiasedNBetterSight.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Strategy4);
      }
    });


    final JRadioButton doubleSpeedNBetterSight =
        new JRadioButton("Double Speed + South Biased + Better Sight");
    doubleSpeedNBetterSight.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.setEvaderStrategy(Strategies.Strategy5);
      }
    });

    final ButtonGroup evaderAttr = new ButtonGroup();
    evaderAttr.add(evaderRandom);
    evaderAttr.add(southBiased);
    evaderAttr.add(doubleSpeed);
    evaderAttr.add(betterSight);
    evaderAttr.add(southBiasedNBetterSight);
    evaderAttr.add(doubleSpeedNBetterSight);


    theEv.add(evaderRandom);
    theEv.add(southBiased);
    theEv.add(doubleSpeed);
    theEv.add(betterSight);
    theEv.add(southBiasedNBetterSight);
    theEv.add(doubleSpeedNBetterSight);


    final Border raisedbevel =
        BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.GREEN, Color.GRAY);
    theGrid.setBorder(raisedbevel);
    theSeekers
        .setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.GRAY));
    theEv.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.GRAY));

    set.add(theGrid, BorderLayout.LINE_START);
    set.add(theSeekers, BorderLayout.CENTER);
    set.add(theEv, BorderLayout.LINE_END);


    frame.add(set, BorderLayout.CENTER);

    /******************* THE SETTINGS ENDS **********************************/

    // Reset button
    final JPanel control_panel = new JPanel();
    frame.add(control_panel, BorderLayout.NORTH);

    // listener to the reset button
    final JButton start = new JButton("Start");
    start.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.add(sim, BorderLayout.CENTER);
        frame.remove(set);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        sim.reset();
      }
    });
    control_panel.add(start);

    // listener to the reset button
    final JButton reset = new JButton("Reset");
    reset.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sim.reset();
      }
    });
    control_panel.add(reset);

    // Put the frame on the screen
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }
  
}
