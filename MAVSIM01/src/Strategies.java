
public enum Strategies {
	Random, Strategy1, Strategy2, Strategy3, Strategy4, Strategy5
}


/*
*****Evader Strategies******
*Strategy1: South Biased
*Strategy2: Double Speed
*Strateg3: See pursuer before getting caught and move in opposite direction; otherwise move randomly
*			with equal probability for each direction
*Strategy4: South Biased + Better Sight
*Strategy5: Double Speed + Better Sight + South Biased
*


************Seeker Strategies*********
*Strategy1: Keep a log of coordinate visited and the time when it's visited; communicate that info to
*other pursuers. Each pursuers goes the coordinate visited the earliest
*If there are coordinates that haven't been visited, then pick one randomly out of  those
*
*
*Strategy2: Strategy1 but with full view
*
*Strategy3: Strategy1 but coordinate age, distance, and location of other robots considered
*
*/