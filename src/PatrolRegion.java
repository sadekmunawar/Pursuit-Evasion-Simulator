import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Strategy for the Seeker robot to patrol a certain region of the grid.
 */
public class PatrolRegion {


  private int regionID;
  private LinkedList<Coordinate> regionCoordinates;
  private int numRobots;


  public PatrolRegion(int numRobots, int regionID) {
    resetCoordinates(numRobots, regionID);

  }

  public Coordinate getNextCoordinate(Coordinate curr) {
    final Random rndm = new Random();
    final int rndmNumber = rndm.nextInt(this.regionCoordinates.size());
    Coordinate x = this.regionCoordinates.get(rndmNumber);

    if (this.regionCoordinates.size() >= 3 && x.equals(curr)) {
      while (x.equals(curr)) {

        final Random rndm2 = new Random();
        final int rndmNumber2 = rndm2.nextInt(this.regionCoordinates.size());
        x = this.regionCoordinates.get(rndmNumber2);

      }
    }

    return x;
  }

  public int getNumRobots() {
    return numRobots;
  }

  public LinkedList<Coordinate> getRegionCoordinates() {
    return regionCoordinates;
  }

  public int getRegionID() {
    return regionID;
  }

  // Region id cannot be greater than numRobots - 1
  public void resetCoordinates(int numRobots, int regionID) {
    int rID = regionID;

    if (rID == -1) {
      rID = ThreadLocalRandom.current().nextInt(0, numRobots);
    }

    this.numRobots = numRobots;
    this.regionID = regionID;

    int y = 0;

    if (rID % 4 == 0) {
      y = SimulationV3.Y_MAX;
    } else if (rID % 4 == 1 || (rID % 4 == 3)) {
      y = SimulationV3.Y_MAX - 1;
    } else {
      y = SimulationV3.Y_MAX - 2;
    }
    this.regionCoordinates = new LinkedList<>();

    final int z = numRobots + 1;
    final int l = SimulationV3.X_MAX;

    final int start = (int) (regionID * (Math.round((double) l / (double) z)));

    final int a = (int) Math.round((2.0 * l) / z);

    final int end = start + a - 1;


    if (numRobots == 5 && l == 8) {
      switch (regionID) {
        case 0:
          this.regionCoordinates.add(new Coordinate(0, y));
          this.regionCoordinates.add(new Coordinate(1, y));
          this.regionCoordinates.add(new Coordinate(2, y));
          break;
        case 1:
          this.regionCoordinates.add(new Coordinate(1, y));
          this.regionCoordinates.add(new Coordinate(2, y));
          this.regionCoordinates.add(new Coordinate(3, y));
          break;
        case 2:
          this.regionCoordinates.add(new Coordinate(2, y));
          this.regionCoordinates.add(new Coordinate(3, y));
          this.regionCoordinates.add(new Coordinate(4, y));
          break;
        case 3:
          this.regionCoordinates.add(new Coordinate(3, y));
          this.regionCoordinates.add(new Coordinate(4, y));
          this.regionCoordinates.add(new Coordinate(5, y));
          this.regionCoordinates.add(new Coordinate(6, y));
          break;
        case 4:
          this.regionCoordinates.add(new Coordinate(5, y));
          this.regionCoordinates.add(new Coordinate(6, y));
          this.regionCoordinates.add(new Coordinate(7, y));
          this.regionCoordinates.add(new Coordinate(8, y));
          break;
        default:

      }

    } else if (numRobots == 6 && regionID == 4 && l == 8) {
      this.regionCoordinates.add(new Coordinate(5, y));
      this.regionCoordinates.add(new Coordinate(6, y));
      this.regionCoordinates.add(new Coordinate(7, y));

    } else if (numRobots == 6 && regionID == 5 && l == 8) {
      this.regionCoordinates.add(new Coordinate(6, y));
      this.regionCoordinates.add(new Coordinate(7, y));
      this.regionCoordinates.add(new Coordinate(8, y));
    } else if (numRobots == 7 && regionID == 6 & l == 8) {
      this.regionCoordinates.add(new Coordinate(6, y));
      this.regionCoordinates.add(new Coordinate(7, y));
      this.regionCoordinates.add(new Coordinate(8, y));
    } else {
      for (int i = start; i <= end; i++) {
        if (i <= SimulationV3.X_MAX) {
          this.regionCoordinates.add(new Coordinate(i, y));
        }
      }
    }

  }

  public void setNumRobots(int numRobots) {
    this.numRobots = numRobots;
  }


  public void setRegionCoordinates(LinkedList<Coordinate> regionCoordinates) {
    this.regionCoordinates = regionCoordinates;
  }

  public void setRegionID(int regionID) {
    this.regionID = regionID;
  }
}
