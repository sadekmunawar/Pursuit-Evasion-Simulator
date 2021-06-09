
public class Coordinate {
	int x;
	int y;
	
	
	Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getPx() {
		if (this.x == 0) {
			return 12;
		}
		return (this.x * 100) - 15;
		
	}
	
	public int getPy() {
		if (this.y == 0) {
			return 12;
		}
		return (this.y * 90) - 15;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	
	
	/*public static Coordinate getCoordinate(int pX, int pY) {
		int x1 = Math.round(pX / 100);
		int y1 = Math.round(pY / 90);
		
		Coordinate c = new Coordinate(x1, y1);
		
		if (pX >= (c.getPx() - 15) && pX <= (c.getPx() + 15)) {
			if (pY >= (c.getPy() - 15) && pY <= (c.getPy() + 15)) {
				return c;
			}
		} 
		
		return null;
		
	} */



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	} 
	
	
	
	

}
