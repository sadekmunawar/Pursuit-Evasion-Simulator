/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Class containing two Seeker robots in communication.
 */
public class Communicators {

  private final Seeker r1;
  private final Seeker r2;

  Communicators(Seeker r1, Seeker r2) {
    this.r1 = r1;
    this.r2 = r2;

  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj == null) || (getClass() != obj.getClass()))
      return false;
    final Communicators other = (Communicators) obj;
    if (r1 == null) {
      if (other.r1 != null)
        return false;
    } else if (!r1.equals(other.r1))
      return false;
    if (r2 == null) {
      if (other.r2 != null)
        return false;
    } else if (!r2.equals(other.r2))
      return false;
    return true;
  }

  public Seeker getR1() {
    return r1;
  }

  public Seeker getR2() {
    return r2;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((r1 == null) ? 0 : r1.hashCode());
    result = prime * result + ((r2 == null) ? 0 : r2.hashCode());
    return result;
  }

}
