import java.util.HashMap;
import java.util.LinkedList;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Data associated with a robot.
 */
public class Data {
  public enum Types {
    CoordinateList, CoordinateTimeArray, CoordinateTimeMap, CoordinateTimeSet
  }

  private int dataID;
  private int roboID;

  private int regionID;


  private HashMap<Integer, RobotRegionTime> robotTime;


  private Types type;
  private LinkedList<Coordinate> dList;

  private LinkedList<Integer> colList;

  private Seeker.Phase phase;

  private int[][] coordinateTimeLog;

  public Data(HashMap<Coordinate, Integer> data) {
    this.type = Types.CoordinateTimeMap;
  }

  public Data(HashMap<Integer, RobotRegionTime> roboTime, int regionID, int dataID, int robotID) {
    this.dataID = dataID;
    this.roboID = robotID;
    this.regionID = regionID;
    this.robotTime = roboTime;
  }

  public Data(int[][] s, int id, int roboID) {
    this.dataID = id;
    this.roboID = roboID;
    this.coordinateTimeLog = s;
    this.type = Types.CoordinateTimeArray;
  }

  public Data(LinkedList<Coordinate> data, int id, int roboID) {
    this.type = Types.CoordinateList;
    this.dList = data;
    this.dataID = id;
    this.roboID = roboID;
  }

  public Data(LinkedList<Integer> myCols, Seeker.Phase p, int id, int roboID) {
    this.dataID = id;
    this.roboID = roboID;
    colList = new LinkedList<>();
    colList = myCols;
    this.phase = p;
  }

  public LinkedList<Integer> getColList() {
    return this.colList;
  }

  public LinkedList<Coordinate> getCoordinateList() {
    return this.dList;
  }


  public int[][] getCoordinateTimeArray() {
    return this.coordinateTimeLog;
  }

  public int getDataID() {
    return this.dataID;
  }

  public Types getDataType() {
    return this.type;
  }

  public Seeker.Phase getPhase() {
    return this.phase;
  }


  public int getRegionID() {
    return regionID;
  }

  public int getRoboID() {
    return this.roboID;
  }

  public HashMap<Integer, RobotRegionTime> getRobotTime() {
    return robotTime;
  }


}
