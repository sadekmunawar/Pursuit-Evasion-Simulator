/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Regions in the grid
 */
public class RobotRegionTime {

  private int regionID;
  private int time;
  private boolean isDead;


  public RobotRegionTime(int regID, int t) {
    this.regionID = regID;
    this.time = t;
    this.isDead = false;
  }

  public int getRegionID() {
    return regionID;
  }

  public int getTime() {
    return time;
  }

  public boolean isDead() {
    return isDead;
  }

  public void setDead(boolean isDead, int time) {
    this.isDead = isDead;
    this.time = time;
    this.regionID = -1;
  }

  public void setRegionID(int regionID) {
    this.regionID = regionID;
  }

  public void setTime(int time) {
    this.time = time;
  }

}
