import java.awt.Graphics;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Robots of the simulator
 */
public abstract class Robot {

  // position
  private int px;
  private int py;

  // velocity
  private int vx;
  private int vy;

  // width - height
  private final int wd;
  private final int ht;

  boolean stay;

  // boundary
  private final Directions lastDir;

  private final int maxX;
  private final int maxY;

  public Robot(int vx, int vy, int px, int py, int width, int height, int courtWidth,
      int courtHeight) {
    this.vx = vx;
    this.vy = vy;
    this.px = px;
    this.py = py;
    this.wd = width;
    this.ht = height;

    // take the width and height into account when setting the bounds for the upper left corner
    // of the object.
    this.maxX = courtWidth - width;
    this.maxY = courtHeight - height;

    this.lastDir = Directions.N;
  }

  // Change direction
  public void bounce(Directions d) {
    if (d == null) {
      return;
    }

    switch (d) {
      case N:
        this.vy = Math.abs(this.vy);
        break;
      case S:
        this.vy = -Math.abs(this.vy);
        break;
      case W:
        this.vx = Math.abs(this.vx);
        break;
      case E:
        this.vx = -Math.abs(this.vx);
        break;
      default:
        break;
    }
  }

  private void clip() {
    this.px = Math.min(Math.max(this.px, 0), this.maxX);
    this.py = Math.min(Math.max(this.py, 0), this.maxY);
  }

  public int distanceTo(Robot that) {
    final int dx = Math.abs(this.px - that.px);
    final int dy = Math.abs(this.py - that.py);

    return (int) Math.sqrt((dx * dx) + (dy * dy));
  }

  public abstract void draw(Graphics g);

  public Directions getCurrDirection() {

    if (this.vx > 0 && this.vy == 0) {
      return Directions.E;
    }

    if (this.vx < 0 && this.vy == 0) {
      return Directions.W;
    }

    if (this.vy > 0 && this.vx == 0) {
      return Directions.S;
    }

    if (this.vy < 0 && this.vx == 0) {
      return Directions.N;
    }

    if (this.vx > 0 && this.vy > 0) {
      return Directions.SE;
    }

    if (this.vx < 0 && this.vy < 0) {
      return Directions.NW;
    }

    if (this.vx > 0) {
      return Directions.NE;
    }
    return Directions.SW;
  }

  public int getHeight() {
    return this.ht;
  }

  public Directions getLastDir() {
    return this.lastDir;
  }

  public int getPx() {
    return this.px;
  }

  public int getPy() {
    return this.py;
  }

  public int getVx() {
    return this.vx;
  }

  public int getVy() {
    return this.vy;
  }

  public int getWidth() {
    return this.wd;
  }

  // Change direction
  public void go(Directions d) {
    if (d == null) {
      return;
    }

    switch (d) {
      case N:
        this.vy = -Math.abs(this.vy);
        if (this.vy == 0) {
          this.vy = -1;
        }
        this.vx = 0;

        break;
      case S:
        this.vy = Math.abs(this.vy);
        if (this.vy == 0) {
          this.vy = 1;
        }
        this.vx = 0;
        break;
      case W:
        this.vx = -Math.abs(this.vx);
        if (this.vx == 0) {
          this.vx = -1;
        }
        this.vy = 0;
        break;
      case E:
        this.vx = Math.abs(this.vx);
        if (this.vx == 0) {
          this.vx = 1;
        }
        this.vy = 0;
        break;
      default:
        break;
    }
  }

  public Directions hitObj(Robot that) {
    if (!this.willIntersect(that)) {
      return null;
    }
    final double dx = that.px + that.wd / 2 - (this.px + this.wd / 2);
    final double dy = that.py + that.ht / 2 - (this.py + this.ht / 2);

    final double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy * dy)));
    final double diagTheta = Math.atan2(this.ht / 2, this.wd / 2);

    if (theta <= diagTheta) {
      return Directions.E;
    }
    if (theta > diagTheta && theta <= Math.PI - diagTheta) {
      // Coordinate system for GUIs is switched
      if (dy > 0) {
        return Directions.S;
      } else {
        return Directions.N;
      }
    } else {
      return Directions.E;
    }
  }

  public Directions hitWall() {
    if (this.px + this.vx < 0) {
      return Directions.W;
    }
    if (this.px + this.vx > this.maxX) {
      return Directions.E;
    }

    if (this.py + this.vy < 0) {
      return Directions.N;
    }
    if (this.py + this.vy > this.maxY) {
      return Directions.S;
    }
    return null;
  }

  public boolean intersects(Robot that) {
    return (this.px + this.wd >= that.px && this.py + this.ht >= that.py
        && that.px + that.wd >= this.px && that.py + that.ht >= this.py);
  }

  /*** GETTERS **********************************************************************************/
  public int maxX() {
    return this.maxX;
  }

  public int maxY() {
    return this.maxY;
  }

  public void move() {
    if (!stay) {
      this.px += this.vx;
      this.py += this.vy;
    }

    stay = false;

    clip();
  }

  /*** SETTERS **********************************************************************************/
  public void setPx(int px) {
    this.px = px;
    clip();
  }

  public void setPy(int py) {
    this.py = py;
    clip();
  }

  public void setVx(int vx) {
    this.vx = vx;
  }


  public void setVy(int vy) {
    this.vy = vy;
  }

  public void stay() {
    this.stay = true;
  }

  public boolean willIntersect(Robot that) {
    final int thisNextX = this.px + this.vx;
    final int thisNextY = this.py + this.vy;
    final int thatNextX = that.px + that.vx;
    final int thatNextY = that.py + that.vy;

    return (thisNextX + this.wd >= thatNextX && thisNextY + this.ht >= thatNextY
        && thatNextX + that.wd >= thisNextX && thatNextY + that.ht >= thisNextY);
  }
}
