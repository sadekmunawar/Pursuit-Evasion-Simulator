import java.awt.Color;
import java.awt.Graphics;

public class Roads {
	
	private int roadID;
	private int startWidth;
	private int startHeight;
	private int width;
	private int height;
	
	
	
	Roads(int roadNum, int numRows, int numCols) {
		this.roadID = roadNum;
				
		int rW = SimulationV3.ROAD_WIDTH;
		

		int wB = SimulationV3.H_SPACING;
		int lB = SimulationV3.V_SPACING;
		
		
		if (roadNum < 0) {
			int r = -1 * roadNum;
			
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
	
/*	public static int getRoadNum(int px, int py) {
		
	} */
	
	public int getRoadID() {
		return this.roadID;
	}
	
    public void draw(Graphics g) {
        g.setColor(Color.green);
        if (this.roadID > 0) {
        	g.fillRect(this.startWidth, this.startHeight, this.width, this.height);  
        } else if (this.roadID < 0) {
        	g.fillRect(this.startWidth, this.startHeight, this.width, this.height);  
       }
    else {
        	g.fillRect(0, 0, SimulationV3.H_SPACING + 2 * (SimulationV3.ROAD_WIDTH), SimulationV3.ROAD_WIDTH);  
        	g.fillRect(0, 0, SimulationV3.ROAD_WIDTH, SimulationV3.V_SPACING + 2 * (SimulationV3.ROAD_WIDTH));  
        	
        }
    }
    
    public static int getNorth(Coordinate c) {
    //	if (c.getY() <= 0) {
    //		return 1000;
    //	}
    	return ((c.getY() - 1) * (SimulationV3.X_MAX + 1)) + c.getX();
    	
    }
    
    public static int getSouth(Coordinate c) {
   // 	if (c.getY() >= 6) {
  //  		return 1000;
 //   	}
    	return getNorth(c) + (SimulationV3.X_MAX + 1);
    	
    }
    
    public static int getWest(Coordinate c) {
  // 	if (c.getX() <= 0) {
  //  		return 1000;
//   	}
    	return -1 * ((((SimulationV3.X_MAX) * c.getY()) - 1) + c.getX());
    	
    }
    
	public static int getEast(Coordinate c) {
//		if (c.getX() >= 8) {
	//		return 1000;
//		}
    	return -1 * ((((SimulationV3.X_MAX) * c.getY()) - 1) + c.getX()) - 1;
    	
    }
	


	public int getStartWidth() {
		return startWidth;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndWidth() {
		return width;
	}

	public int getEndHeight() {
		return height;
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
