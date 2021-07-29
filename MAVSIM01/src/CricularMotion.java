import java.util.LinkedList;
import java.util.Random;

public class CricularMotion {
	
	private Seeker.Phase phase;
	
	
	private int rID;
	private LinkedList<Integer> cols;
	
	
	public CricularMotion(Seeker.Phase p) {
		this.phase = p;
		this.cols  = new LinkedList<Integer>();
	}
	
	public LinkedList<Integer> getCols() {
		return this.cols;
	}
	
	public void assignCols(int numRobots, int roboID) {
		this.cols  = new LinkedList<Integer>();
		int d = (SimulationV3.X_MAX + 1) / numRobots;
		
		if (d >= 2) {
		
			for (int i = (d * roboID); i < (d * roboID) + d; i++) {
				this.cols.add(i);
			}
			
			if (cols.getLast() == SimulationV3.X_MAX - 1) {
				this.cols.add(SimulationV3.X_MAX);
			}
		
		} else if (d == 1) {
		/*	int i = 1;
			
			while ((SimulationV3.X_MAX - i) / (numRobots - i) < 2) {
				i++;
			}
			
			i--; */
			if (roboID == 0) {
				if (numRobots == 5) {
					this.cols.add(0);
					this.cols.add(1);
				} else if (numRobots == 6) {
					this.cols.add(0);
					this.cols.add(1);
				} else if (numRobots == 7) {
					this.cols.add(0);
					this.cols.add(1);
				} else if (numRobots == 8) {
					this.cols.add(0);
					this.cols.add(1);
				}
			} else if (roboID == 1) {
				if (numRobots == 5) {
					this.cols.add(2);
					this.cols.add(3);
				} else if (numRobots == 6) {
					this.cols.add(2);
					this.cols.add(3);
				} else if (numRobots == 7) {
					this.cols.add(1);
					this.cols.add(2);
					
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 2) {
				if (numRobots == 5) {
					this.cols.add(3);
					this.cols.add(4);
				} else if (numRobots == 6) {
					this.cols.add(3);
					this.cols.add(4);
				} else if (numRobots == 7) {
					this.cols.add(3);
					this.cols.add(4);
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 3) {
				if (numRobots == 5) {
					this.cols.add(5);
					this.cols.add(6);					
				} else if (numRobots == 6) {
					this.cols.add(4);
					this.cols.add(5);
					
				} else if (numRobots == 7) {
					this.cols.add(4);
					this.cols.add(5);
					
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 4) {
				if (numRobots == 5) {
					this.cols.add(7);
					this.cols.add(8);
					
				} else if (numRobots == 6) {
					this.cols.add(6);
					this.cols.add(7);
					
				} else if (numRobots == 7) {
					this.cols.add(5);
					this.cols.add(6);
					
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 5) {
				if (numRobots == 6) {
					this.cols.add(7);
					this.cols.add(8);
				} else if (numRobots == 7) {
					this.cols.add(6);
					this.cols.add(7);
					
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 6) {
				if (numRobots == 7) {
					this.cols.add(7);
					this.cols.add(8);
					
				} else if (numRobots == 8) {
					
				}
				
			} else if (roboID == 7) {
				if (numRobots == 8) {
					
				}
				
			} 	
		}
	}
	
	public void assignColsStrat5(int numRobots, int roboID) {
		this.cols  = new LinkedList<Integer>();
		this.rID = roboID;
		
		if (roboID == 0) {
			if (numRobots == 4) {
				this.cols.add(0);
				this.cols.add(1);
				this.cols.add(2);
				this.cols.add(3);
			} else if (numRobots == 5) {
				this.cols.add(0);
				this.cols.add(1);
				this.cols.add(2);
			} else if (numRobots == 6) {
				this.cols.add(0);
				this.cols.add(1);
				this.cols.add(2);
			} else if (numRobots == 7) {
				this.cols.add(0);
				this.cols.add(1);
				this.cols.add(2);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
		} else if (roboID == 1) {
			if (numRobots == 4) {
				this.cols.add(1);
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
			} else if (numRobots == 5) {
				this.cols.add(1);
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
			} else if (numRobots == 6) {
				this.cols.add(1);
				this.cols.add(2);
				this.cols.add(3);
			} else if (numRobots == 7) {
				this.cols.add(1);
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 2) {
			if (numRobots == 4) {
				this.cols.add(3);
				this.cols.add(4);
				this.cols.add(5);
				this.cols.add(6);
				this.cols.add(7);
			} else if (numRobots == 5) {
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
				this.cols.add(5);
				this.cols.add(6);
			} else if (numRobots == 6) {
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
			} else if (numRobots == 7) {
				this.cols.add(2);
				this.cols.add(3);
				this.cols.add(4);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 3) {
			if (numRobots == 4) {
				this.cols.add(4);
				this.cols.add(5);
				this.cols.add(6);
				this.cols.add(7);
				this.cols.add(8);
			} else if (numRobots == 5) {
				this.cols.add(5);
				this.cols.add(6);
				this.cols.add(7);
			} else if (numRobots == 6) {
				this.cols.add(3);
				this.cols.add(4);
				this.cols.add(5);
			} else if (numRobots == 7) {
				this.cols.add(3);
				this.cols.add(4);
				this.cols.add(5);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 4) {
			if (numRobots == 5) {
				this.cols.add(6);
				this.cols.add(7);
				this.cols.add(8);
			} else if (numRobots == 6) {
				this.cols.add(4);
				this.cols.add(5);
				this.cols.add(6);
			} else if (numRobots == 7) {
				this.cols.add(4);
				this.cols.add(5);
				this.cols.add(6);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 5) {
			if (numRobots == 6) {
				this.cols.add(5);
				this.cols.add(6);
				this.cols.add(7);
				this.cols.add(8);
			} else if (numRobots == 7) {
				this.cols.add(5);
				this.cols.add(6);
				this.cols.add(7);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 6) {
			if (numRobots == 7) {
				this.cols.add(6);
				this.cols.add(7);
				this.cols.add(8);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} else if (roboID == 7) {
			if (numRobots == 4) {
				this.cols.add(0);
				this.cols.add(1);
			} else if (numRobots == 5) {
				this.cols.add(0);
				this.cols.add(1);
			} else if (numRobots == 6) {
				this.cols.add(0);
				this.cols.add(1);
			} else if (numRobots == 7) {
				this.cols.add(0);
				this.cols.add(1);
			} else if (numRobots == 8) {
				this.cols.add(0);
				this.cols.add(1);
			}
			
		} 	
		
		
	}
	
	public Coordinate getNextCoordinate(Coordinate curr) {
		
		Random rndm = new Random();
        int rndmNumber = rndm.nextInt(cols.size());
        int x = cols.get(rndmNumber);
        
        if (cols.size() == 1) {
        	
        	
        } else 
	        if (x == curr.getX()) {
	        	while (x == curr.getX()) {
	
	        		Random rndm2 = new Random();
	                int rndmNumber2 = rndm2.nextInt(cols.size());
	                x = cols.get(rndmNumber2);
	        		
	        	} 
        } 
        
        
		if (this.phase == Seeker.Phase.Phase1) {
	        
	        return new Coordinate(x, SimulationV3.Y_MAX - 1);

		} else if (this.phase == Seeker.Phase.Phase2) {
			
			if (cols.size() == 1) {
				if (Math.random() < 0.001) {
					return new Coordinate(curr.getX(), curr.getY() - 1);
				} else {
					return curr;
				}
			}
			int i = cols.get(cols.size() /2);
			
			int j = Math.abs((SimulationV3.X_MAX / 2) - i);
						
			if (j <= 0) {
				if (Math.random() < 0.10) {
					return new Coordinate(x, curr.getY() - 1);
				} else {
					return new Coordinate(x, curr.getY());
				}
				
			} else if (j <= 2) {
				if (Math.random() < 0.30) {
					return new Coordinate(x, curr.getY() - 1);
				} else {
					return new Coordinate(x, curr.getY());
				}
				
			} else if (j <= 4) {
				if (Math.random() < 0.60) {
					return new Coordinate(x, curr.getY() - 1);
				} else {
					return new Coordinate(x, curr.getY());
				}
			} else {
				if (Math.random() < 0.80) {
					return new Coordinate(x, curr.getY() - 1);
				} else {
					return new Coordinate(x, curr.getY());
				}
				
			}
				
			} else if (this.phase == Seeker.Phase.Phase3) {
				if (cols.size() == 1) {
					if (Math.random() < 0.001) {
						return new Coordinate(curr.getX(), curr.getY() + 1);
					} else {
						return curr;
					}
				}
				int i = cols.get(cols.size() /2);
				
				int j = Math.abs((SimulationV3.X_MAX / 2) - i);
							
				if (j == 0) {
					if (Math.random() < 0.10) {
						return new Coordinate(x, curr.getY() + 1);
					} else {
						return new Coordinate(x, curr.getY());
					}
					
				} else if (j <= 2) {
					if (Math.random() < 0.30) {
						return new Coordinate(x, curr.getY() + 1);
					} else {
						return new Coordinate(x, curr.getY());
					}
					
				} else if (j <= 4) {
					if (Math.random() < 0.60) {
						return new Coordinate(x, curr.getY() + 1);
					} else {
						return new Coordinate(x, curr.getY());
					}
				
				} else {
					if (Math.random() < 0.80) {
						return new Coordinate(x, curr.getY() + 1);
					} else {
						return new Coordinate(x, curr.getY());
					}
				}
			}
		
		return null;
	}
	
	public Coordinate getNextCoordinateStrat5(Coordinate curr) {
		
		Random rndm = new Random();
        int rndmNumber = rndm.nextInt(cols.size());
        int x = cols.get(rndmNumber);
        
        if (cols.size() == 1) {
        	
        	
        } else 
	        if (x == curr.getX()) {
	        	while (x == curr.getX()) {
	
	        		Random rndm2 = new Random();
	                int rndmNumber2 = rndm2.nextInt(cols.size());
	                x = cols.get(rndmNumber2);
	        		
	        	} 
        } 
        
        int y = 0;
        
        if (rID == 0) {
        	y = 5;
        } else if(rID == 1) {
        	y = 4;
        } else if(rID == 2) {
        	y = 6;
        } else if(rID == 3) {
        	y = 5;
        } else if(rID == 4) {
        	y = 4;
        } else if(rID == 5) {
        	y = 6;
        } else if(rID == 6) {
        	y = 5;
        } else if(rID == 7) {
        	y = 4;
        }
        
        
        return new Coordinate(x, y);
	}
        
	
	
	public void setCols(LinkedList<Integer> l) {
		this.cols = l;
	}
	
	public void changePhase(Seeker.Phase p) {
		this.phase = p;
		
		if (p == Seeker.Phase.Phase3) {
			if (this.cols.size() != 1) {
				if (this.cols.getFirst() < SimulationV3.X_MAX / 2) {
					int j = this.cols.getLast();
					if (j + 1 <= 8) {
						this.cols.add(j+1);
					}

				} else {
					int j = this.cols.getLast();
					if (j - 1 >= 0) {
						this.cols.add(j-1);
					}

				}
			}
		}
	}
	
}
