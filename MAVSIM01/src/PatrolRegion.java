import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PatrolRegion {
	
	
	private int regionID;
	private LinkedList<Coordinate> regionCoordinates;
	private int numRobots;
	
	
	public PatrolRegion(int numRobots, int regionID) {
		resetCoordinates(numRobots, regionID); 
		
	}
	
	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public LinkedList<Coordinate> getRegionCoordinates() {
		return regionCoordinates;
	}

	public void setRegionCoordinates(LinkedList<Coordinate> regionCoordinates) {
		this.regionCoordinates = regionCoordinates;
	}

	public int getNumRobots() {
		return numRobots;
	}

	public void setNumRobots(int numRobots) {
		this.numRobots = numRobots;
	}
	
	
	public Coordinate getNextCoordinate(Coordinate curr) {
		Random rndm = new Random();
        int rndmNumber = rndm.nextInt(this.regionCoordinates.size());
        Coordinate x = this.regionCoordinates.get(rndmNumber);
        
        if (this.regionCoordinates.size() >= 3 && x.equals(curr)) {
        	while (x.equals(curr)) {
        		
        		Random rndm2 = new Random();
                int rndmNumber2 = rndm2.nextInt(this.regionCoordinates.size());
                x = this.regionCoordinates.get(rndmNumber2);
        		
        	} 
        }
        
        return x;
		
	}

	// Region id cannot be greater than numRobots - 1
	public void resetCoordinates(int numRobots, int regionID) {
		int rID = regionID;
		
		if (rID == -1) {
			rID = ThreadLocalRandom.current().nextInt(0, numRobots);
		}
		
		this.numRobots = numRobots;
		this.regionID = regionID;
		
		int y = 0;
		
		if (rID % 3 == 0) {
			y = SimulationV3.Y_MAX - 1;
		} else if (rID % 3 == 1) {
			y = SimulationV3.Y_MAX - 2;
		} else {
			y = SimulationV3.Y_MAX;
		}
		this.regionCoordinates = new LinkedList<Coordinate>();
		
		int z = numRobots + 1;
		int l = SimulationV3.X_MAX;
		
		int start = (int) (regionID * (Math.round( (double) l / (double) z)));
		
		int a = (int) Math.round((2.0 * l) / (double) z);
		
		int end = start + a - 1;
		
		if (numRobots == 5 && l == 8) {
			switch(regionID) {
				case 0:
					this.regionCoordinates.add(new Coordinate(0,y));
					this.regionCoordinates.add(new Coordinate(1,y));
					this.regionCoordinates.add(new Coordinate(2,y));
					break;
				case 1:
					this.regionCoordinates.add(new Coordinate(1,y));
					this.regionCoordinates.add(new Coordinate(2,y));
					this.regionCoordinates.add(new Coordinate(3,y));
					break;
				case 2:
					this.regionCoordinates.add(new Coordinate(2,y));
					this.regionCoordinates.add(new Coordinate(3,y));
					this.regionCoordinates.add(new Coordinate(4,y));
					break;
				case 3:
					this.regionCoordinates.add(new Coordinate(4,y));
					this.regionCoordinates.add(new Coordinate(5,y));
					this.regionCoordinates.add(new Coordinate(6,y));
					break;
				case 4:
					this.regionCoordinates.add(new Coordinate(6,y));
					this.regionCoordinates.add(new Coordinate(7,y));
					this.regionCoordinates.add(new Coordinate(8,y));
					break;
				default:
					
			}

		} else if (numRobots == 6 && regionID == 4 && l == 8) {
			this.regionCoordinates.add(new Coordinate(5,y));
			this.regionCoordinates.add(new Coordinate(6,y));
			this.regionCoordinates.add(new Coordinate(7,y));

		} else if (numRobots == 6 && regionID == 5 && l == 8) {
			this.regionCoordinates.add(new Coordinate(6,y));
			this.regionCoordinates.add(new Coordinate(7,y));
			this.regionCoordinates.add(new Coordinate(8,y));
		} else if (numRobots == 7 && regionID == 6 & l == 8) {
			this.regionCoordinates.add(new Coordinate(6,y));
			this.regionCoordinates.add(new Coordinate(7,y));
			this.regionCoordinates.add(new Coordinate(8,y));
		} else {
			for (int i = start; i <= end; i++) {
				if (i <= SimulationV3.X_MAX) {
					this.regionCoordinates.add(new Coordinate(i, y));
				}
			}
		}
	}
}
