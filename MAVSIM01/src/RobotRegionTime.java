
public class RobotRegionTime {

	private int regionID;
	private int time;
	
	
	public RobotRegionTime(int regID, int t) {
		this.regionID = regID;
		this.time = t;
	}
	
	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
