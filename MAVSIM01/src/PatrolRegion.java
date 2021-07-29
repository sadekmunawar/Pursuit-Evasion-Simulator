import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class PatrolRegion {
	
	
	private int regionID;
	private LinkedList<Coordinate> regionCoordinates;
	private int numRobots;
	
	
	
	
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
		
		
		
		
		
		
	}
}
