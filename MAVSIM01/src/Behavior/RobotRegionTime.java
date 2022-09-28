
public class RobotRegionTime {

	private int regionID;
	private int time;
	private boolean isDead;
	
	
	public RobotRegionTime(int regID, int t) {
		this.regionID = regID;
		this.time = t;
		this.isDead = false;
	}
	
	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
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
