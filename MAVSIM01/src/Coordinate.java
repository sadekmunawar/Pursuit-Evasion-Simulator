/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Coordinates in the grid - locations of the intersection of 
 * horizontal and vertical lines.
 */
public class Coordinate {
  int x;
  int y;


  Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj == null) || (getClass() != obj.getClass()))
      return false;
    final Coordinate other = (Coordinate) obj;
    if ((x != other.x) || (y != other.y))
      return false;
    return true;
  }

  public int getPx() {
    if (this.x == 0) {
      return (SimulationV3.ROAD_WIDTH / 2);
    }

    return (this.x * (SimulationV3.ROAD_WIDTH + SimulationV3.H_SPACING)
        + (SimulationV3.ROAD_WIDTH / 2));

  }

  public int getPy() {
    if (this.y == 0) {
      return (SimulationV3.ROAD_WIDTH / 2);
    }

    return (this.y * (SimulationV3.ROAD_WIDTH + SimulationV3.V_SPACING)
        + (SimulationV3.ROAD_WIDTH / 2));
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

}
