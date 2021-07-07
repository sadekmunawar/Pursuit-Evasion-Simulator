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
	
	public Types getDataType() {
		return this.type;
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
