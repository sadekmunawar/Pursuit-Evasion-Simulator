import java.awt.Color;
import java.awt.Graphics;

/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Obstacles in the grid that block sight and communication.
 */
public class Obstacles extends Robot {

  private final Color color;
  private final int px;
  private final int py;
  private final int wd;
  private final int ht;

  private final boolean isOval;

  public Obstacles(int px, int py, int width, int height, int courtWidth, int courtHeight,
      Color color, boolean isOval) {
    super(0, 0, px, py, width, height, courtWidth, courtHeight);

    this.px = px;
    this.py = py;
    this.wd = width;
    this.ht = height;

    this.color = color;
    this.isOval = isOval;
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(this.color);
    if (this.isOval) {
      g.fillOval(this.px, this.py, this.wd, this.ht);
    } else {
      g.fillRect(this.px, this.py, this.wd, this.ht);
    }

  }

}
