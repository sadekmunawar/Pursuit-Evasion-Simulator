import java.awt.Color;
import java.awt.Graphics;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Each road is a segment of the grid.
 */
public class Roads {

  public static int getEast(Coordinate c) {
    return -1 * ((((SimulationV3.X_MAX) * c.getY()) - 1) + c.getX()) - 1;
  }

  public static int getNorth(Coordinate c) {
    return ((c.getY() - 1) * (SimulationV3.X_MAX + 1)) + c.getX();
  }

  public static int getSouth(Coordinate c) {
    return getNorth(c) + (SimulationV3.X_MAX + 1);
  }

  public static int getWest(Coordinate c) {
    return -1 * ((((SimulationV3.X_MAX) * c.getY()) - 1) + c.getX());
  }

  private final int roadID;

  private int startWidth;

  private int startHeight;

  private int width;

  private int height;

  Roads(int roadNum, int numRows, int numCols) {
    this.roadID = roadNum;

    final int rW = SimulationV3.ROAD_WIDTH;


    final int wB = SimulationV3.H_SPACING;
    final int lB = SimulationV3.V_SPACING;


    if (roadNum < 0) {
      final int r = -1 * roadNum;

      this.startWidth = (r % SimulationV3.X_MAX) * (wB + rW);
      this.width = (2 * rW) + wB;

      this.startHeight = (r / SimulationV3.X_MAX) * (rW + lB);


      if (this.startHeight < 0) {
        this.startHeight = 0;
      }

      this.height = rW;

    } else if (roadNum > 0) {


      this.startWidth = ((roadNum % (SimulationV3.X_MAX + 1)) * (rW + wB));
      if (this.startWidth < 0) {
        this.startWidth = 0;
      }
      this.width = rW;

      this.startHeight = (roadNum / (SimulationV3.X_MAX + 1)) * (rW + lB);
      this.height = (2 * rW) + lB;

    } else {

    }

  }

  public void draw(Graphics g) {
    g.setColor(Color.green);
    if (this.roadID > 0) {
      g.fillRect(this.startWidth, this.startHeight, this.width, this.height);
    } else if (this.roadID < 0) {
      g.fillRect(this.startWidth, this.startHeight, this.width, this.height);
    } else {
      g.fillRect(0, 0, SimulationV3.H_SPACING + 2 * (SimulationV3.ROAD_WIDTH),
          SimulationV3.ROAD_WIDTH);
      g.fillRect(0, 0, SimulationV3.ROAD_WIDTH,
          SimulationV3.V_SPACING + 2 * (SimulationV3.ROAD_WIDTH));

    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj == null) || (getClass() != obj.getClass()))
      return false;
    final Roads other = (Roads) obj;
    if (roadID != other.roadID)
      return false;
    return true;
  }

  public int getEndHeight() {
    return height;
  }

  public int getEndWidth() {
    return width;
  }

  public int getRoadID() {
    return this.roadID;
  }

  public int getStartHeight() {
    return startHeight;
  }

  public int getStartWidth() {
    return startWidth;
  }


}
