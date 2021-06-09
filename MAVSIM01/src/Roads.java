import java.awt.Color;
import java.awt.Graphics;

public class Roads {
	
	private int roadID;
	private int startWidth;
	private int startHeight;
	private int endWidth;
	private int endHeight;
	
	
	Roads(int roadNum, int numRows, int numCols) {
		this.roadID = roadNum;
		
		if (roadNum < 0) {
			int r = -1 * roadNum;
			
			this.startWidth = 100 * (r % 8) - 30;
			this.endWidth = this.startWidth + 130;
			
			this.startHeight = ((r / 8) * 90) - 30;
			
			
			if (this.startHeight < 0) {
				this.startHeight = -10;
			}
			
			this.endHeight = this.startHeight + 30;
			
		} else if (roadNum > 0) {
			
			this.startWidth = ((roadNum % numCols) * 100) - 30;
			if (this.startWidth < 0) {
				this.startWidth = -10;
			}
			this.endWidth = this.startWidth + 30;
			
			this.startHeight = (roadNum / numCols) * 90 - 30;
			//this.endHeight = this.startHeight + 90;
			this.endHeight = this.startHeight + 120;
			
			
		} else {
			
		}
	
	}
	
/*	public static int getRoadNum(int px, int py) {
		
	} */
	
	public int getRoadID() {
		return this.roadID;
	}
	
    public void draw(Graphics g) {
        g.setColor(Color.green);
        if (this.roadID > 0) {
        	g.fillRect(this.startWidth, this.startHeight, 30, 90 + 30);  
        } else if (this.roadID < 0) {
        	g.fillRect(this.startWidth, this.startHeight, 130, 30);  
        } else {
        	g.fillRect(-10, -10, 100, 30);  
        	g.fillRect(-10, -10, 30, 90);  
        	
        }
    }
    
    public static int getNorth(Coordinate c) {
    	return ((c.getY() - 1) * 9) + c.getX();
    	
    }
    
    public static int getSouth(Coordinate c) {
    	return getNorth(c) + 9;
    	
    }
    
    public static int getWest(Coordinate c) {
    	return -1 * (((8 * c.getY()) - 1) + c.getX());
    	
    }
    
	public static int getEast(Coordinate c) {
    	return -1 * (((8 * c.getY()) - 1) + c.getX()) - 1;
    	
    }
	


	public int getStartWidth() {
		return startWidth;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndWidth() {
		return endWidth;
	}

	public int getEndHeight() {
		return endHeight;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Roads other = (Roads) obj;
		if (roadID != other.roadID)
			return false;
		return true;
	}


}
