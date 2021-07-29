import java.util.HashMap; 
import java.util.LinkedList;


public class Data {
	public enum Types{
		CoordinateList, CoordinateTimeArray, CoordinateTimeMap, CoordinateTimeSet
	}
	
	private int dataID;
	private int roboID;
	
	private Types type;
	private LinkedList<Coordinate> dList;
	
	private LinkedList<Integer> colList;
	
	private Seeker.Phase phase;
	
	private int[][] coordinateTimeLog;
	
	public Data(LinkedList<Coordinate> data, int id, int roboID) {
		this.type = Types.CoordinateList;
		this.dList = data;
		this.dataID = id;
		this.roboID = roboID;
	}
	
	public Data(HashMap<Coordinate, Integer> data) {
		this.type = Types.CoordinateTimeMap;
	}
	
	public Data(int[][] s, int id, int roboID) {
		this.dataID = id;
		this.roboID = roboID;
		this.coordinateTimeLog = s;
		this.type = Types.CoordinateTimeArray;
	}
	
	public Data(LinkedList<Integer> myCols, Seeker.Phase p, int id, int roboID) {
		this.dataID = id;
		this.roboID = roboID;
		colList = new LinkedList<Integer>();
		colList = myCols;
		this.phase = p;
	}
	
	public Types getDataType() {
		return this.type;
	}
	
	public LinkedList<Integer> getColList() {
		return this.colList;
	}
	
	public Seeker.Phase getPhase() {
		return this.phase;
	}
	
	public int[][] getCoordinateTimeArray() {
		return this.coordinateTimeLog;
	}
			
			
	public LinkedList<Coordinate> getCoordinateList() {
		return this.dList;
	}
	
	public int getDataID() {
		return this.dataID;
	}
	
	public int getRoboID() {
		return this.roboID;
	}

	
}
