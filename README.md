# Pursuit-Evasion-Simulator
A Java simulator for a robotics model of pursuit-evasion

## Description
A simulator to develop and test algorithms governing a multi-robot persuit-evasion system. The simulator is scalable. It is capable of simulating temoporary and permament robot failure. It also provides visulazion for communication and collaboration between robots.

## How To Run
Run RunSim.java with Java 14

## Simulator
![image](https://user-images.githubusercontent.com/76756708/215230402-404c2f5b-c09f-49a8-ba1f-17bb972ff71b.png)
The red circles are Evader robots and black are Seeker robots. Lines between Seekers represents communication. Communications get blocked by the red obstacles. One iteration ends after all the Evader robots get captured. Capture occurs when an Evader is close to a Seeker and the direct sight of the Seeker is not blocked by an obstacle.
