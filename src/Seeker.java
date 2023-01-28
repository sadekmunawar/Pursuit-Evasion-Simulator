import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Seeker robot.
 */
public class Seeker extends Robot {

  public enum Phase {
    Phase1, Phase2, Phase3, Phase4
  }

  private final Color color;
  private final int id;


  private final HashSet<Integer> exploredInt = new HashSet<>();
  private HashSet<Coordinate> explored = new HashSet<>();
  private HashSet<Integer> targetFound = new HashSet<>();

  private final HashMap<Integer, RobotRegionTime> roboTime;

  private final HashMap<Integer, HashSet<Integer>> dataRecieved;

  private final HashMap<Integer, Integer> lastCommTime;

  private final Task currTask;
  private Phase currPhase;

  private int nodeVisited;

  private final int ran;

  private final CricularMotion moveC;

  private final LinkedList<Integer> sendBuffer;

  private LinkedList<Integer> myCols;

  private final PatrolRegion pr;

  private boolean isSending;

  private double howBusy;
  private final double alpha = 0.20;

  private Coordinate targetLoc;
  private boolean targetReached;
  private boolean waiting;

  private boolean inCommunication;
  private int commPeriod;
  private int commParterID;

  private boolean hasData;

  private int pOneCounter;

  private int dataID;

  private Coordinate currC;

  private final int[][] coordinateTimeLog;

  private int currTime;


  private Data d;

  private Data ds;

  private int failurePeriod;

  public Seeker(Coordinate c, int vx, int vy, int width, int height, int courtWidth,
      int courtHeight, Color color, int id) {
    super(vx, vy, c.getPx(), c.getPy(), width, height, courtWidth, courtHeight);
    // TODO Auto-generated constructor stub
    this.id = id;
    this.color = color;

    this.commPeriod = 0;
    this.inCommunication = false;

    this.currTime = 0;

    this.lastCommTime = new HashMap<>();

    this.dataRecieved = new HashMap<>();

    this.sendBuffer = new LinkedList<>();

    this.dataID = 0;

    ran = ThreadLocalRandom.current().nextInt(10, 25);

    this.howBusy = 0.60;

    this.failurePeriod = 0;

    this.pOneCounter = 0;

    this.nodeVisited = 0;

    this.currC = c;

    this.roboTime = new HashMap<>();

    this.pr = new PatrolRegion(1, 0);

    this.moveC = new CricularMotion(Phase.Phase1);

    this.coordinateTimeLog = new int[SimulationV3.Y_MAX + 1][SimulationV3.X_MAX + 1];

    this.currTask = new Task();


    this.currPhase = Phase.Phase1;

    this.myCols = new LinkedList<>();

    this.moveC.assignColsStrat5(SimSettings.NumPursuers, id);

    this.myCols = this.moveC.getCols();



  }

  public void beginCommunication() {
    this.inCommunication = true;
    this.commPeriod = 0;
  }


  public void changePhase(Phase p) {
    this.moveC.changePhase(p);
    this.currPhase = p;
  }

  public void clearFilter() {
    this.howBusy = 0.0;
  }

  public void commJammed() {
    this.sendBuffer.removeFirst();
    this.sendBuffer.addFirst(0);
  }

  public void commSucc() {
    this.sendBuffer.addFirst(1);

    if (this.sendBuffer.size() > SimulationV3.ROAD_WIDTH + SimulationV3.H_SPACING) {
      if (this.sendBuffer.getLast() == 1) {
        this.sendBuffer.removeLast();
      }
    }
  }


  private boolean containsData(Data d) {
    final int roboID = d.getRoboID();
    final int dataID = d.getDataID();

    if (!this.dataRecieved.containsKey(roboID)) {
      return false;
    }
    if (this.dataRecieved.get(roboID).contains(dataID)) {
      return true;
    }
    return false;
  }

  private int countAlive() {
    int alive = 0;
    for (final Integer i : this.roboTime.keySet()) {
      if (!this.roboTime.get(i).isDead()) {
        alive++;
      }
    }
    return alive + 1;
  }

  public void cuurCoordinate(Coordinate c) {
    this.currC = c;
  }

  @Override
  public void draw(Graphics g) {
    // TODO Auto-generated method stub
    g.setColor(this.color);
    if (this.failurePeriod != 0 || this.currPhase == Phase.Phase2) {
      g.setColor(Color.YELLOW);
    }

    g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
  }


  public void endCommunication() {
    this.inCommunication = false;
    this.commPeriod = 0;
  }

  public void endFailure() {
    this.failurePeriod = 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj == null) || (getClass() != obj.getClass()))
      return false;
    final Seeker other = (Seeker) obj;
    if (id != other.id)
      return false;
    return true;
  }

  public void exploredTotal(HashSet<Coordinate> h) {
    this.explored = h;
  }

  public void fillFilter() {
    this.howBusy = 1.0;
  }

  public Coordinate getCircularNext(Coordinate c) {
    return this.moveC.getNextCoordinate(c);
  }

  public Coordinate getCircularNextStrat5(Coordinate c) {
    return this.moveC.getNextCoordinateStrat5(c);
  }

  public int getcommPeriod() {
    return this.commPeriod;
  }

  public Task getCurrTask() {
    return this.currTask;
  }

  public HashSet<Integer> getDiscoveredTargets() {
    return this.targetFound;
  }

  public Coordinate getEarliest() {
    int min = Integer.MAX_VALUE;
    Coordinate corMin = null;
    final LinkedList<Coordinate> cList = new LinkedList<>();
    for (int i = 0; i < this.coordinateTimeLog.length; i++) {
      for (int j = 0; j < this.coordinateTimeLog[0].length; j++) {

        if (this.coordinateTimeLog[i][j] < min) {
          min = this.coordinateTimeLog[i][j];
          corMin = new Coordinate(j, i);
        }

        if (this.coordinateTimeLog[i][j] == 0) {
          cList.add(new Coordinate(j, i));
        }

      }
    }

    if (!cList.isEmpty()) {
      final Random rndm = new Random();

      final int rndmNumber = rndm.nextInt(cList.size());
      corMin = cList.get(rndmNumber);
    }

    moveTo(corMin);

    return corMin;

  }

  public HashSet<Coordinate> getExplored() {
    return this.explored;
  }

  public HashSet<Integer> getExploredInt() {
    return this.exploredInt;
  }

  public int getFailurePreiod() {
    return this.failurePeriod;
  }

  public int getID() {
    return this.id;
  }

  public Coordinate getNextCoordinateSt6(Coordinate c) {
    return this.pr.getNextCoordinate(c);

  }


  public int getPartnerID() {
    return this.commParterID;
  }


  public Seeker.Phase getPhase() {
    return this.currPhase;
  }

  public Coordinate getTargetLoc() {
    return this.targetLoc;
  }

  public Coordinate getTaskedCoordinate() {
    final Coordinate c = this.currTask.getNextCoordinate();
    moveTo(c);
    return c;
  }

  public Coordinate getTaskLocation() {

    return null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  public boolean hasReached() {
    return this.targetReached;
  }

  public boolean hasReachedTarget() {
    return this.targetReached;
  }

  public boolean isCommunicating() {
    return this.inCommunication;
  }

  public boolean isWaiting() {
    return this.waiting;
  }

  public void justExplored(Coordinate c) {
    this.explored.add(c);
  }

  public void justExploredInt(int c) {
    this.exploredInt.add(c);
  }

  public void moveTo(Coordinate c) {
    this.targetLoc = c;
    this.targetReached = false;

  }

  public void neigibhorIsDead(int time) {

    this.nodeVisited++;

    if (this.nodeVisited >= ran) {


      final int r = this.pr.getRegionID();

      if (r == 0) {
        for (final Integer i : this.roboTime.keySet()) {
          if (this.roboTime.get(i).getRegionID() == 1) {
            int t = 5000;

            if (this.lastCommTime.containsKey(i)) {
              t = this.lastCommTime.get(i);
            }

            if (t >= 600) {
              this.roboTime.get(i).setDead(true, time);


              this.pr.resetCoordinates(countAlive(), 0);
              this.moveTo(getNextCoordinateSt6(this.currC));
              this.nodeVisited = 0;
            }
            break;
          }
        }

        /*
         * } else if (r == countAlive() - 2) { for (Integer i: this.roboTime.keySet()) { int rr;
         *
         * if (Math.random() < 0.5) { rr = r - 1; } else { rr = r + 1; }
         *
         * if (this.roboTime.get(i).getRegionID() == rr) { this.roboTime.get(i).setDead(true, time);
         *
         *
         * if (rr == r - 1) { this.pr.resetCoordinates(countAlive(), r - 1); } else {
         * this.pr.resetCoordinates(countAlive(), r); }
         *
         * } }
         */

      } else {
        for (final Integer i : this.roboTime.keySet()) {
          if (this.roboTime.get(i).getRegionID() == r - 1) {

            int t = 5000;

            if (this.lastCommTime.containsKey(i)) {
              t = this.lastCommTime.get(i);
            }

            if (t >= 600) {

              this.roboTime.get(i).setDead(true, time);


              this.pr.resetCoordinates(countAlive(), r - 1);



            }
            break;
          }
        }
      }
      this.nodeVisited = 0;
    }

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

  public void processData(Strategies s) {
    if (s == Strategies.Random) {
      this.hasData = false;
      return;
    }
    if (this.d == null) {
      this.hasData = false;
    } else if (containsData(this.d)) {
      this.d = null;
      this.hasData = false;
    } else {

      this.lastCommTime.put(this.d.getRoboID(), 0);


      if (this.dataRecieved.containsKey(this.d.getRoboID())) {
        final HashSet<Integer> h = this.dataRecieved.get(this.d.getRoboID());
        h.add(this.d.getDataID());
        this.dataRecieved.put(this.d.getRoboID(), h);
      } else {
        final HashSet<Integer> h = new HashSet<>();
        h.add(this.d.getRoboID());
        this.dataRecieved.put(this.d.getRoboID(), h);
      }
      // do something with the data
      if (s == Strategies.Strategy1) {
        final int[][] newData = this.d.getCoordinateTimeArray();
        int min = Integer.MAX_VALUE;
        final LinkedList<Coordinate> cList = new LinkedList<>();
        Coordinate corMin = null;
        for (int i = 0; i < this.coordinateTimeLog.length; i++) {
          for (int j = 0; j < this.coordinateTimeLog[0].length; j++) {
            if (newData[i][j] > this.coordinateTimeLog[i][j]) {
              this.coordinateTimeLog[i][j] = newData[i][j];
            }
            if (this.coordinateTimeLog[i][j] < min) {
              min = this.coordinateTimeLog[i][j];
              corMin = new Coordinate(j, i);
            }

            if (this.coordinateTimeLog[i][j] == 0) {
              cList.add(new Coordinate(j, i));
            }

          }
        }

        if (!cList.isEmpty()) {
          final Random rndm = new Random();

          final int rndmNumber = rndm.nextInt(cList.size());
          corMin = cList.get(rndmNumber);
        }
        moveTo(corMin);
      } else if (s == Strategies.Strategy4) {

        if (this.currPhase == Phase.Phase1) {
          // doPhaseOne(this.d.getColList());



          if (currC.getY() == SimulationV3.Y_MAX - 1) {

            this.pOneCounter++;

          }

          if (this.pOneCounter > 5 || this.d.getPhase() == Seeker.Phase.Phase2) {
            this.currPhase = Phase.Phase2;
            this.moveC.changePhase(Phase.Phase2);
          }

        } else if (this.d.getPhase() == Seeker.Phase.Phase3) {
          if (this.currPhase == Phase.Phase2) {
            this.currPhase = Phase.Phase3;
            this.moveC.changePhase(Phase.Phase3);

          }
        } else if (this.d.getPhase() == Seeker.Phase.Phase2) {
          if (this.currPhase == Phase.Phase3) {
            this.currPhase = Phase.Phase2;
            this.moveC.changePhase(Phase.Phase2);

          }
        }
      } else if (s == Strategies.Strategy6) {
        final int j = this.d.getRegionID();

        this.roboTime.put(this.d.getRoboID(), new RobotRegionTime(j, this.currTime));



        updateRoboTime(this.d.getRobotTime());

        final int alive = countAlive();

        final int r = this.pr.getRegionID();

        // int[] regArr = new int[this.roboTime.size() + 1];
        final int[] regArr = new int[this.roboTime.size() + 1];

        int nr = 0;

        if (r != j) {
          nr = r;
        } else {
          for (final Integer m : this.roboTime.keySet()) {
            if (!this.roboTime.get(m).isDead()) {
              regArr[this.roboTime.get(m).getRegionID()] = 1;
            }
          }

          int c = 1;

          boolean regFound = false;

          while (((r + c) < alive) || ((r - c) >= 0)) {
            if ((r + c) < alive) {
              if (regArr[r + c] == 0) {
                nr = r + c;
                regFound = true;
                break;
              }
            } else if ((r - c) >= 0) {
              if (regArr[r - c] == 0) {
                nr = r + c;
                regFound = true;
                break;
              }
            }
            c++;
          }

          if (!regFound) {
            // nr = ThreadLocalRandom.current().nextInt(0, this.roboTime.size() + 1);
            nr = ThreadLocalRandom.current().nextInt(0, alive);
          }
        }


        if (alive == 0) {
          throw new IllegalArgumentException("WTF");
        }

        this.pr.resetCoordinates(alive, nr);

        this.moveTo(getNextCoordinateSt6(this.currC));

        /*
         * if (i < this.roboTime.size()) { this.pr.resetCoordinates(this.roboTime.size() + 1, nr); }
         * else if (i < this.roboTime.size()) {
         *
         * } else { this.pr.resetCoordinates(this.roboTime.size() + 1, nr); }
         */

      }
    }
    this.hasData = false;
    this.d = null;
  }

  public void reachedTarget() {
    this.targetReached = true;
  }

  public boolean recieveData(Data dd) {
    this.howBusy = this.howBusy + this.alpha;
    if (this.hasData) {
      this.d = null;
      // this.sendBuffer.removeFirst();
      // this.sendBuffer.addFirst(0);
      // this.sendProb = this.sendProb - ((1 / 50)); // (1 / 50)
      return false;
    }
    this.d = dd;
    this.hasData = true;
    // this.sendBuffer.addFirst(1);
    // this.sendProb = this.sendProb + ((1 / 50)); // (1 / 50)
    return true;
  }

  public void recordTimeAndCoordinate(Coordinate c, int time) {
    this.coordinateTimeLog[c.getY()][c.getX()] = time;
    this.dataID++;
  }

  public void registerTime(int i) {
    this.currTime = i;
  }

  public void sendDone() {
    this.isSending = false;
  }

  public void sendInit(Strategies s) {

    if (this.howBusy <= 0) {
    } else if (this.howBusy >= 1) {
    } else {
    }

    if (Math.random() < 0.40) {
      this.isSending = true;
      this.howBusy = this.howBusy + this.alpha;

      if (s == Strategies.Strategy1) {
        this.ds = new Data(this.coordinateTimeLog, dataID, id);
      } else if (s == Strategies.Strategy4) {
        this.dataID++;
        this.ds = new Data(this.myCols, this.currPhase, dataID, id);
      } else if (s == Strategies.Strategy6) {
        this.dataID++;
        this.ds = new Data(this.roboTime, this.pr.getRegionID(), this.dataID, this.id);
      }
      // this.ds = something
    } else {
      this.isSending = false;
      clearFilter();
    }

  }

  public boolean sendStatus() {
    return this.isSending;
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

  public void setDiscoverdTargets(HashSet<Integer> h) {
    this.targetFound = h;
  }

  public void setPartnerID(int i) {
    this.commParterID = i;
  }

  public void startFailure() {
    this.failurePeriod = 1;
  }

  public void targetDiscovered(int id) {
    this.targetFound.add(id);
  }

  public void targetReached() {
    this.targetReached = true;
  }

  public Data toSendData() {
    return this.ds;
  }

  public void updateCommPeriod() {
    this.commPeriod += 10;
  }

  public void updateFailureTime() {
    this.failurePeriod++;
  }

  public void updateLastCommTime() {
    this.lastCommTime.replaceAll((k, v) -> v + 1);
  }

  private void updateRoboTime(HashMap<Integer, RobotRegionTime> h) {
    for (final Integer i : h.keySet()) {
      if (this.roboTime.containsKey(i)) {
        if (this.roboTime.get(i).getTime() < h.get(i).getTime()) {

          this.roboTime.put(i, h.get(i));
        }
      } else {
        if (i != this.id) {
          this.roboTime.put(i, h.get(i));

        }
      }

    }
  }

  public void waiting(boolean w) {
    this.waiting = w;
  }
}
