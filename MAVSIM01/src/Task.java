import java.util.HashSet;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Tasks for a robot.
 */
public class Task {
  public enum PatrolArea {
    Top, Bottom, Right, Left
  }

  public enum Type {
    Patrol, Sweep, Corner
  }

  private Type t;

  private PatrolArea patrolRegion;

  private PatrolArea cornerRegion;

  private int sweepRow;

  private Coordinate cornerTarget;



  Task() {
    this.t = Type.Patrol;
    final double p = Math.random();

    if (p < 0.25) {
      this.patrolRegion = PatrolArea.Top;
    } else if (p < 0.50) {
      this.patrolRegion = PatrolArea.Bottom;
    } else if (p < 0.75) {
      this.patrolRegion = PatrolArea.Right;
    } else if (p < 1) {
      this.patrolRegion = PatrolArea.Left;
    }

  }

  public void changePatrolRegion(PatrolArea p) {
    this.patrolRegion = p;
  }

  public void changeType(Type t) {
    this.t = t;

    if (t == Type.Sweep) {
      new HashSet<>();
    }
  }

  public PatrolArea getCornerRegion() {
    return this.cornerRegion;
  }

  public Coordinate getCornerTarget() {
    return this.cornerTarget;
  }

  public Coordinate getNextCoordinate() {
    if (this.t == Type.Sweep) {

    } else if (this.t == Type.Patrol) {


    } else if (this.t == Type.Corner) {

    }
    return null;
  }

  public PatrolArea getPatrolRegion() {
    return this.patrolRegion;
  }

  public int getSweepRow() {
    return this.sweepRow;
  }

  public Type getTaskType() {
    return this.t;
  }

  public void setCornerRegion(PatrolArea p) {
    this.cornerRegion = p;
  }

  public void setCornerTarget(Coordinate c) {
    this.cornerTarget = c;

    final double p = Math.random();

    if (p < 0.25) {
      this.cornerRegion = PatrolArea.Top;
    } else if (p < 0.50) {
      this.cornerRegion = PatrolArea.Bottom;
    } else if (p < 0.75) {
      this.cornerRegion = PatrolArea.Right;
    } else if (p < 1) {
      this.cornerRegion = PatrolArea.Left;
    }
  }

  public void setSweepRow(int r) {
    this.sweepRow = r;
  }
}
