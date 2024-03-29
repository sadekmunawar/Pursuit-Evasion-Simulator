import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.opencsv.CSVWriter;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: The simulator
 */
@SuppressWarnings("serial")
public class SimulationV3 extends JPanel {


  // constants
  public static final int COURT_WIDTH = 800; // 800 --> up to 1200

  public static final int COURT_HEIGHT = 550; // 550
  public static final int X_MAX = 8;

  public static final int Y_MAX = 6;

  public static final int ROAD_WIDTH = (int) (COURT_WIDTH * 0.0325);

  public static final int H_SPACING =
      (int) Math.round((((double) COURT_WIDTH - ((X_MAX + 1) * ROAD_WIDTH)) / X_MAX));

  public static final int V_SPACING =
      (int) Math.round(((double) COURT_HEIGHT - ((Y_MAX + 1) * ROAD_WIDTH)) / Y_MAX);

  public static final int commLimit = ROAD_WIDTH + H_SPACING + 2;
  public static final int catchLimit = V_SPACING;

  // Update interval for timer, in milliseconds
  public static final int INTERVAL = 2;
  private final JLabel status; // Current status text

  private int comSucc;

  private final double timedFailureProbability = 0.00007;
  private final boolean failure;


  private LinkedList<Seeker> infailure;

  public int[][] coordinateTimeLogGlobal;

  private List<Seeker> seekers;
  private List<Targets> targets;

  private boolean simDone;

  private GridType gType = GridType.Full;

  private int numTrials;

  private int time;

  private int targetsFound;

  private int interference;

  private HashSet<Roads> paths;
  private HashSet<Integer> pathIDs;

  private final HashSet<Obstacles> obs = new HashSet<>();


  private int successfulEvaders = 0;

  private int totalEvaders;

  private HashMap<Integer, Communicators> comm;

  private HashSet<Coordinate> allCoord;

  private Strategies seekerStrategy = Strategies.Random;
  private Strategies evaderStrategy = Strategies.Random;

  private int timedFailure;
  private int absoluteFailure;


  private CSVWriter cvwriter;


  //// private HashSet<Integer> targetsDiscovered;

  Timer timer = new Timer(INTERVAL, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      tick();
    }
  });



  private boolean seekerAdded = false;

  public SimulationV3(JLabel status) {

    this.numTrials = 0;

    this.failure = SimSettings.Failure;

    this.timedFailure = 0;
    this.absoluteFailure = 0;

    setBorder(BorderFactory.createLineBorder(Color.BLACK));


    final File file = new File(SimSettings.FilePath);
    try {
      // create FileWriter object with file as parameter
      final FileWriter outputfile = new FileWriter(file);

      // create CSVWriter object filewriter object as parameter
      this.cvwriter = new CSVWriter(outputfile);

      // adding header to csv
      final String[] header =
          {"Trials", "Caught", "Successful", "Interference", "Time", "Successful Comm", "TF", "AF"};
      this.cvwriter.writeNext(header);
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for (int i = 1; i < X_MAX * Y_MAX; i++) {

      final int col = i % X_MAX;
      final int row = i / X_MAX;

      final int offset = ROAD_WIDTH;
      final int offset2 = V_SPACING / 2;

      final int xLoc = col * (H_SPACING + ROAD_WIDTH); // + ROAD_WIDTH;
      final int yLoc = row * (V_SPACING + ROAD_WIDTH);


      if (i % 6 != 0) {
        if (i % 3 == 0) {
          this.obs.add(new Obstacles((xLoc) + offset, (yLoc) + offset, 20, 15, COURT_WIDTH,
              COURT_HEIGHT, Color.RED, true));

          this.obs.add(new Obstacles((xLoc) + offset + offset2, (yLoc) + offset + offset2, 20, 10,
              COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
        } else if (i % 4 == 0) {
          this.obs.add(new Obstacles((xLoc) + offset, (yLoc) + offset, 15, 15, COURT_WIDTH,
              COURT_HEIGHT, Color.RED, false));

          this.obs.add(new Obstacles((xLoc) + offset + offset2, (yLoc) + offset + offset2, 10, 20,
              COURT_WIDTH, COURT_HEIGHT, Color.RED, false));

          if (row != 0) {
            this.obs.add(new Obstacles((xLoc) + offset + offset2, (yLoc) + offset, 15, 20,
                COURT_WIDTH, COURT_HEIGHT, Color.RED, true));
          }
        } else if (i % 2 == 0) {
          this.obs.add(new Obstacles((xLoc) + offset, (yLoc) + offset, 10, 20, COURT_WIDTH,
              COURT_HEIGHT, Color.RED, false));

          this.obs.add(new Obstacles((xLoc) + offset + offset2, (yLoc) + offset + offset2, 15, 15,
              COURT_WIDTH, COURT_HEIGHT, Color.RED, false));
        } else {
          this.obs.add(new Obstacles((xLoc) + offset, (yLoc) + offset, 20, 10, COURT_WIDTH,
              COURT_HEIGHT, Color.RED, false));

          this.obs.add(new Obstacles((xLoc) + offset + offset2, (yLoc) + offset + offset2, 20, 15,
              COURT_WIDTH, COURT_HEIGHT, Color.RED, true));
        }
      }
    }

    setFocusable(true);
    final String m =
        "Time " + this.time + " ---------------- " + "Evaders Found: 0" + " ---------------- "
            + "Evaders Successful: 0" + " -------------- " + "Trial: " + this.numTrials;

    status.setText(m);

    this.status = status;
  }

  public void addRoads() {
    this.paths = new HashSet<>();
    this.pathIDs = new HashSet<>();

    for (int i = -1 * (X_MAX * (Y_MAX + 1) + 1); i < (X_MAX + 1) * Y_MAX; i++) {

      if (this.gType == GridType.Random) {
        if (Math.random() < 0.85) {
          paths.add(new Roads(i, 7, 9));
          pathIDs.add(i);
        }
      } else if (this.gType == GridType.Full) {
        paths.add(new Roads(i, 7, 9));
        pathIDs.add(i);
      }
    }
  }

  private void addSeeker() {
    if (this.time >= 2000 && !seekerAdded) {

      ThreadLocalRandom.current().nextInt(0, this.seekers.size());

      this.seekers.remove(1);

      seekerAdded = true;
    }
  }

  private Directions getARandomDirection(Coordinate c) {
    final LinkedList<Directions> l = new LinkedList<>();
    if (this.pathIDs.contains(Roads.getNorth(c))) {
      if (c.getY() != 0) {
        l.add(Directions.N);
      }
    }
    if (this.pathIDs.contains(Roads.getSouth(c))) {
      if (c.getY() != Y_MAX) {
        l.add(Directions.S);
      }
    }
    if (this.pathIDs.contains(Roads.getWest(c))) {
      if (c.getX() != 0) {
        l.add(Directions.W);
      }

    }
    if (this.pathIDs.contains(Roads.getEast(c))) {
      if (c.getX() != X_MAX) {
        l.add(Directions.E);
      }
    }
    final int randomNum = ThreadLocalRandom.current().nextInt(0, l.size());

    return l.get(randomNum);
  }

  // create a unique communication ID
  private int getCommunicationID(Seeker s1, Seeker s2) {
    return (s1.getID() * s1.getID()) + (s2.getID() * s2.getID());
  }

  private Coordinate getCoordinate(int pX, int pY, int i) {
    int x1 = (int) Math.round((double) pX / (H_SPACING + ROAD_WIDTH));
    int y1 = (int) Math.round((double) pY / (V_SPACING + ROAD_WIDTH));

    if (pX <= ROAD_WIDTH / 2) {
      x1 = 0;
    }

    if (pY <= ROAD_WIDTH / 2) {
      y1 = 0;
    }

    final Coordinate c = new Coordinate(x1, y1);

    int diff = 0;

    if ((this.evaderStrategy == Strategies.Strategy2 || this.evaderStrategy == Strategies.Strategy5)
        && i == 1) {
      diff = 1;
    }



    if (pX >= (c.getPx() - diff) && pX <= (c.getPx() + diff)) {
      if (pY >= (c.getPy() - diff) && pY <= (c.getPy() + diff)) {
        return c;
      }
    }

    return null;

  }

  private Coordinate getEarliest() {
    int min = Integer.MAX_VALUE;
    Coordinate corMin = null;
    final LinkedList<Coordinate> cList = new LinkedList<>();
    for (int i = 0; i < this.coordinateTimeLogGlobal.length; i++) {
      for (int j = 0; j < this.coordinateTimeLogGlobal[0].length; j++) {

        if (this.coordinateTimeLogGlobal[i][j] == min) {
          cList.add(new Coordinate(j, i));
        }

        if (this.coordinateTimeLogGlobal[i][j] < min) {
          min = this.coordinateTimeLogGlobal[i][j];
          corMin = new Coordinate(j, i);
        }

      }
    }

    if (!cList.isEmpty()) {
      final Random rndm = new Random();

      final int rndmNumber = rndm.nextInt(cList.size());
      corMin = cList.get(rndmNumber);
    }

    return corMin;
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(COURT_WIDTH, COURT_HEIGHT);
  }

  private Directions getSouthBiasedDirection(Coordinate c) {
    final HashSet<Directions> l = new HashSet<>();

    if (this.pathIDs.contains(Roads.getNorth(c))) {
      if (c.getY() != 0) {
        l.add(Directions.N);
      }
    }
    if (this.pathIDs.contains(Roads.getSouth(c))) {
      if (c.getY() != 6) {
        l.add(Directions.S);
      }
    }
    if (this.pathIDs.contains(Roads.getWest(c))) {
      if (c.getX() != 0) {
        l.add(Directions.W);
      }
    }
    if (this.pathIDs.contains(Roads.getEast(c))) {
      if (c.getX() != 8) {
        l.add(Directions.E);
      }
    }

    /// This will break if the grid is not full
    switch (l.size()) {
      case 2:
        if (Math.random() <= 0.60) {
          return Directions.S;
        } else {
          if (l.contains(Directions.E)) {
            return Directions.E;
          }
          return Directions.W;
        }

      case 3:
        final double p = Math.random();
        if (l.contains(Directions.E) && l.contains(Directions.W)) {
          if (p <= 0.40) {
            return Directions.S;
          }
          if (p <= 0.7) {
            return Directions.W;
          }
          return Directions.E;
        } else if (l.contains(Directions.E)) {
          if (p <= 0.40) {
            return Directions.S;
          }
          if (p <= 0.65) {
            return Directions.N;
          }
          return Directions.E;
        } else {
          if (p <= 0.40) {
            return Directions.S;
          }
          if (p <= 0.65) {
            return Directions.N;
          }
          return Directions.W;
        }

      case 4:
        final double q = Math.random();
        if (q <= 0.35) {
          return Directions.S;
        } else if (q <= 0.50) {
          return Directions.N;
        } else if (q <= 0.75) {
          return Directions.E;
        } else {
          return Directions.W;
        }

      default:
        break;
    }
    return null;
  }


  private Directions getTargetDirection(Coordinate cur, Coordinate tar) {

    final LinkedList<Directions> l = new LinkedList<>();

    if (tar.getX() > cur.getX()) {
      l.add(Directions.E);
    } else if (tar.getX() < cur.getX()) {
      l.add(Directions.W);
    }

    if (tar.getY() > cur.getY()) {
      l.add(Directions.S);
    } else if (tar.getY() < cur.getY()) {
      l.add(Directions.N);
    }

    final int randomNum = ThreadLocalRandom.current().nextInt(0, l.size());
    return l.get(randomNum);
  }

  private void handleCommunication() {
    this.comm = new HashMap<>();

    for (final Seeker r : this.seekers) {
      if (!r.sendStatus()) {

        boolean interf = false;

        for (final Seeker u : this.seekers) {
          if (!r.equals(u)) {

            if (r.distanceTo(u) < commLimit) {

              // check of obstacles
              boolean obsInPath = false;
              for (final Obstacles o : this.obs) {

                if (inPath(u.getPx(), u.getPy(), r.getPx(), r.getPy(),
                    o.getPx() + (o.getWidth() / 2), o.getPy() + (o.getHeight() / 2),
                    Math.max((o.getWidth() / 2), (o.getHeight() / 2)))) {
                  obsInPath = true;
                  break;
                }
              }
              // if no obstacles in the path
              if (!obsInPath) {
                if (u.sendStatus()) {
                  final int commID = getCommunicationID(r, u);

                  this.comm.put(commID, new Communicators(r, u));


                  if (!r.recieveData(u.toSendData())) {
                    r.setPartnerID(-1);
                    interf = true;
                  } else {
                    r.registerTime(this.time);
                    this.comSucc++;
                    r.setPartnerID(u.getID());
                    r.commSucc();
                  }

                }
              }
            }
          }
        }
        if (interf) {
          this.comSucc--;
          this.interference++;
          r.commJammed();
        }
      }
    }
  }

  private boolean inPath(int pX1, int pY1, int pX2, int pY2, int oX, int oY, int w) {
    final int a = -1 * (pY2 - pY1);
    final int b = pX2 - pX1;
    final int c = (pX1 * (pY2 - pY1)) - (pY1 * (pX2 - pX1));
    final double d1 = Math.abs((a * oX) + (b * oY) + c);
    final double d2 = Math.sqrt((a * a) + (b * b));
    final double d = d1 / d2;

    if (d < w + 2) {
      return true;
    }

    return false;
  }


  private Directions moveAway(int eX, int eY, int pX, int pY, Directions d, Coordinate c) {
    int y = -1;
    int x = -1;
    if (c != null) {
      x = c.getX();
      y = c.getY();
    }

    if (x == 0 && y == 0) {
      if (eX < pX) {
        return Directions.S;
      }
      return Directions.E;
    }
    if (x == X_MAX && y == 0) {
      if (eX > pX) {
        return Directions.S;
      } else {
        return Directions.W;
      }
    }
    if (y == 0) {
      if (pY > eY) {
        if (Math.random() < 0.50) {
          return Directions.W;
        } else {
          return Directions.E;
        }
      } else if (pX < eX) {
        if (Math.random() <= 0.70) {
          return Directions.S;
        } else {
          return Directions.E;
        }
      } else {
        if (Math.random() <= 0.70) {
          return Directions.S;
        } else {
          return Directions.W;
        }
      }
    } else if (x == X_MAX) {
      if (pX < eX) {
        if (Math.random() <= 0.70) {
          return Directions.S;
        } else {
          return Directions.W;
        }
      } else if (pY < eY) {
        return Directions.S;
      } else {
        return Directions.N;
      }

    } else {
      if (eX == pX) {
        if (eY > pY) {
          return Directions.S;
        } else {
          return Directions.N;
        }
      } else if (eY == pY) {
        if (eX > pX) {
          return Directions.E;
        } else {
          return Directions.W;
        }
        // above right
      } else if (pY < eY && pX > eX) {
        if (d == Directions.E || d == Directions.W) {
          return Directions.W;
        } else {
          return Directions.S;
        }
        // above left
      } else if (pY < eY && pX < eX) {
        if (d == Directions.E || d == Directions.W) {
          return Directions.E;
        } else {
          return Directions.S;
        }
        // below right
      } else if (pY > eY && pX > eX) {
        if (d == Directions.E || d == Directions.W) {
          return Directions.W;
        } else {
          return Directions.N;
        }
      } else {
        if (d == Directions.E || d == Directions.W) {
          return Directions.E;
        } else {
          return Directions.N;
        }
      }
    }

  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (final Roads r : this.paths) {
      // if (Math.random() < 0.90) {
      r.draw(g);
      // }
    }

    for (final Obstacles o : this.obs) {
      o.draw(g);
    }

    for (final Seeker s : this.seekers) {
      s.draw(g);
    }

    for (final Targets t : this.targets) {
      t.draw(g);
    }

    for (final Communicators c : this.comm.values()) {
      if (c.getR1().getPartnerID() == c.getR2().getID()
          || c.getR2().getPartnerID() == c.getR1().getID()) {
        g.setColor(Color.BLUE);
        g.drawLine(c.getR1().getPx(), c.getR1().getPy(), c.getR2().getPx(), c.getR2().getPy());
      }

    }

    for (final Seeker f : this.infailure) {
      f.draw(g);
    }
  }

  public void reset() {
    startTimer();


    this.absoluteFailure = 0;
    this.timedFailure = 0;

    this.seekerAdded = false;

    this.infailure = new LinkedList<>();

    this.coordinateTimeLogGlobal = new int[Y_MAX + 1][X_MAX + 1];

    this.comSucc = 0;

    this.numTrials++;

    this.interference = 0;

    this.allCoord = new HashSet<>();

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 7; j++) {
        this.allCoord.add(new Coordinate(i, j));
      }
    }

    this.comm = new HashMap<>();

    this.simDone = false;

    this.successfulEvaders = 0;

    this.targetsFound = 0;
    // targetsDiscovered = new HashSet<Integer>();

    this.time = 0;

    this.seekers = new LinkedList<>();

    final int numSeekers = SimSettings.NumPursuers;
    final int numEvaders = SimSettings.NumEvaders;

    for (int i = 0; i < numSeekers; i++) {
      final Random nu = new Random();
      final int snum = nu.nextInt(8); // 3
      final int snum2 = 6;// nu.nextInt(2);

      this.seekers.add(new Seeker(new Coordinate(snum, snum2), 0, 1, 5, 5, COURT_WIDTH,
          COURT_HEIGHT, Color.BLACK, i));

    }

    this.targets = new LinkedList<>();

    for (int i = 0; i < numEvaders; i++) {
      final Random n = new Random();

      final int num = n.nextInt(8); 
      final int num2 = 0; 

      this.targets
          .add(new Targets(new Coordinate(num, num2), 1, 0, 5, 5, COURT_WIDTH, COURT_HEIGHT, 1));
    }

    this.totalEvaders = this.targets.size();
  }


  public void setEvaderStrategy(Strategies s) {
    this.evaderStrategy = s;
  }


  public void setGridType(GridType g) {
    this.gType = g;
  }

  public void setSeekerStrategy(Strategies s) {
    this.seekerStrategy = s;
  }



  public void startTimer() {
    this.timer.start();
  }

  public void tick() {
    if (!simDone) {

      addSeeker();

      if (failure) {

        final LinkedList<Seeker> unfail = new LinkedList<>();

        for (final Seeker f : this.infailure) {
          f.updateFailureTime();
          if (f.getFailurePreiod() >= 900) {
            unfail.add(f);
            f.endFailure();
          }
        }

        this.infailure.removeAll(unfail);
        this.seekers.addAll(unfail);
      }

      this.time += INTERVAL;

      final HashSet<Targets> toRemove1 = new HashSet<>();

      for (final Targets t : this.targets) {

        final Coordinate c = getCoordinate(t.getPx(), t.getPy(), 1);

        if (c != null) {
          if (this.evaderStrategy == Strategies.Random) {
            t.go(getARandomDirection(c));
          } else if (this.evaderStrategy == Strategies.Strategy1
              || this.evaderStrategy == Strategies.Strategy4
              || this.evaderStrategy == Strategies.Strategy5) {
            t.go(getSouthBiasedDirection(c));
            if (this.evaderStrategy == Strategies.Strategy5) {
              t.move();
            }
          } else if (this.evaderStrategy == Strategies.Strategy2) {
            t.go(getARandomDirection(c));
            t.move();
          } else if (this.evaderStrategy == Strategies.Strategy3) {
            t.go(getARandomDirection(c));
          }
        }


        if (this.evaderStrategy == Strategies.Strategy2) {
          t.move();
        } else if (this.evaderStrategy == Strategies.Strategy3
            || this.evaderStrategy == Strategies.Strategy4
            || this.evaderStrategy == Strategies.Strategy5) {
          for (final Seeker y : this.seekers) {
            if (t.distanceTo(y) < catchLimit + 10) {
              t.go(moveAway(t.getPx(), t.getPy(), y.getPx(), y.getPy(), t.getCurrDirection(), c));
            }
          }
        }

        if (this.evaderStrategy == Strategies.Strategy5) {
          t.move();
        }

        t.move();

        // reached the bottom
        if (t.getPy() > (COURT_HEIGHT - ROAD_WIDTH) + (ROAD_WIDTH / 4)) {
          this.successfulEvaders++;
          toRemove1.add(t);
        }

      }

      this.targets.removeAll(toRemove1);

      final HashSet<Seeker> toRemoveSeekers = new HashSet<>();

      for (final Seeker s : this.seekers) {

        s.updateLastCommTime();

        if (failure) {

          final double p = Math.random();

          final double absoluteP = 0.5 * timedFailureProbability;

          if (p < absoluteP) {
            toRemoveSeekers.add(s);
            this.absoluteFailure++;
            continue;
          }
          if (p >= absoluteP && p < (absoluteP + timedFailureProbability)) {
            toRemoveSeekers.add(s);
            s.startFailure();
            this.infailure.add(s);
            this.timedFailure++;
            continue;
          }

        }

        s.sendInit(this.seekerStrategy);
        s.setPartnerID(-1);

        final Coordinate c = getCoordinate(s.getPx(), s.getPy(), 0);

        if (c != null) {

          if (this.seekerStrategy == Strategies.Random) {
            s.go(getARandomDirection(c));
          } else if (this.seekerStrategy == Strategies.Strategy2) {
            this.coordinateTimeLogGlobal[c.getY()][c.getX()] = this.time;
            s.moveTo(getEarliest());
            s.go(getTargetDirection(c, s.getTargetLoc()));

          } else {
            s.cuurCoordinate(c);
            s.recordTimeAndCoordinate(c, this.time);
            s.neigibhorIsDead(this.time);
            if (!s.hasReachedTarget() && s.getTargetLoc() != null) {
              if (c.equals(s.getTargetLoc())) {
                s.targetReached();
                Coordinate destination = null;
                if (this.seekerStrategy == Strategies.Strategy1) {
                  destination = s.getEarliest();
                } else if (this.seekerStrategy == Strategies.Strategy3) {
                  destination = s.getTaskedCoordinate();
                } else if (this.seekerStrategy == Strategies.Strategy4) {
                  if (c.getY() == 0) {
                    s.changePhase(Seeker.Phase.Phase3);
                  }
                  if (c.getY() == 5 && s.getPhase() == Seeker.Phase.Phase3) {
                    s.changePhase(Seeker.Phase.Phase2);
                  }
                  destination = s.getCircularNext(c);
                } else if (this.seekerStrategy == Strategies.Strategy5) {
                  destination = s.getCircularNextStrat5(c);
                } else if (this.seekerStrategy == Strategies.Strategy6) {
                  destination = s.getNextCoordinateSt6(c);
                }

                s.moveTo(destination);
                if (!c.equals(destination)) {
                  s.go(getTargetDirection(c, destination));
                } else {
                  s.stay();
                }

              } else {
                s.go(getTargetDirection(c, s.getTargetLoc()));
              }
            } else {
              Coordinate destination = null;

              if (this.seekerStrategy == Strategies.Strategy1) {
                destination = s.getEarliest();
              } else if (this.seekerStrategy == Strategies.Strategy3) {
                destination = s.getTaskedCoordinate();
              } else if (this.seekerStrategy == Strategies.Strategy4) {
                if (c.getY() == 0) {
                  s.changePhase(Seeker.Phase.Phase3);
                }
                if (c.getY() == 5 && s.getPhase() == Seeker.Phase.Phase3) {
                  s.changePhase(Seeker.Phase.Phase2);
                }
                destination = s.getCircularNext(c);
              } else if (this.seekerStrategy == Strategies.Strategy5) {
                destination = s.getCircularNextStrat5(c);
              } else if (this.seekerStrategy == Strategies.Strategy6) {
                destination = s.getNextCoordinateSt6(c);
              }

              s.moveTo(destination);
              if (!c.equals(destination)) {
                s.go(getTargetDirection(c, destination));
              } else {
                s.stay();
              }

            }
          }
        }

        final HashSet<Targets> toRemove = new HashSet<>();

        for (final Targets t : this.targets) {
          if (s.distanceTo(t) < catchLimit) {
            s.targetDiscovered(t.getID());
            this.targetsFound++;
            toRemove.add(t);
          }
        }

        this.targets.removeAll(toRemove);
      }

      this.seekers.removeAll(toRemoveSeekers);

      handleCommunication();


      for (final Seeker p : this.seekers) {
        p.processData(this.seekerStrategy);

        p.move();
      }

      final String m = "Time " + this.time + " ---------------- " + "Evaders Found: "
          + this.targetsFound + " ---------------- " + "Evaders Successful: "
          + this.successfulEvaders + " -------------- " + "Trial: " + this.numTrials
          + "------ Communication Interference: " + this.interference;

      status.setText(m);

      if (this.targetsFound + this.successfulEvaders == this.totalEvaders) {


        final String[] s = {this.numTrials + "", this.targetsFound + "",
            this.successfulEvaders + "", this.interference + "", this.time + "", this.comSucc + "",
            this.timedFailure + "", this.absoluteFailure + ""};

        this.cvwriter.writeNext(s);

        if (this.numTrials >= 50) {
          try {
            this.cvwriter.close();
          } catch (final IOException e) {
            e.printStackTrace();
          }
        }

        this.simDone = true;

      }
    }
    repaint();

    if (this.simDone) {
      if (this.numTrials < 50 || this.time > 120000) {
        reset();
      }
    }
  }



}
