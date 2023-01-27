/*
 * Author: Sadek Munawar
 * Date: June 2021
 * 
 * Description: Strategies for the robot.
 */
public enum Strategies {
  Random, Strategy1, Strategy2, Strategy3, Strategy4, Strategy5, Strategy6
}

/*
 ***** Evader Strategies****** Strategy1: South Biased Strategy2: Double Speed Strategy3: See pursuer
 * before getting caught and move in opposite direction; otherwise move randomly with equal
 * probability for each direction Strategy4: South Biased + Better Sight Strategy5: Double Speed +
 * Better Sight + South Biased
 *
 ************
 *
 * Seeker Strategies********* Strategy1: Keep a log of coordinate visited and the time when it's
 * visited; communicate that info to other pursuers. Each pursuers goes the coordinate visited the
 * earliest If there are coordinates that haven't been visited, then pick one randomly out of those
 *
 *
 * Strategy2: Strategy1 but with full view
 *
 * Strategy3: Strategy1 but coordinate age, distance, and location of other robots considered
 *
 * Strategy4: Form a chain and make a circle Each pick starting location randomly Once at if two
 * colliding starting location then the listener changes The pursuer on the sides have a higher
 * chance of moving up Those in the middle move side to side and slowly move up
 *
 * In the begining, go straight to the south border, 25% E, 25%W, 0%N, 60%S. Once at the bottom,
 * travel horizontally to find to communicate with other robots and do initial configuration
 *
 *
 *
 *
 */
