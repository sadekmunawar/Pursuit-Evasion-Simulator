import java.awt.Graphics;

public abstract class Robot {
    
    // position
    private int px; 
    private int py;
    
    //velocity 
    private int vx;
    private int vy;
    
    //width - height
    private int wd;
    private int ht;
    
    //boundary
    private Directions lastDir;
    
    private int maxX;
    private int maxY;

    public Robot (int vx, int vy, int px, int py, int width, int height, int courtWidth,
            int courtHeight) {
            this.vx = vx;
            this.vy = vy;
            this.px = px;
            this.py = py;
            this.wd  = width;
            this.ht = height;

            // take the width and height into account when setting the bounds for the upper left corner
            // of the object.
            this.maxX = courtWidth - width;
            this.maxY = courtHeight - height;
            
            this.lastDir = Directions.N;
        }
    
    /*** GETTERS **********************************************************************************/
    public int maxX() {
        return this.maxX;
    }
    
    public int maxY() {
        return this.maxY;
    }
    
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return this.vx;
    }
    
    public int getVy() {
        return this.vy;
    }
    
    public int getWidth() {
        return this.wd;
    }
    
    public int getHeight() {
        return this.ht;
    }
    
    public Directions getLastDir() {
        return this.lastDir;
    }

    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }
    
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }
    
    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        clip();
    }
    
    public boolean intersects(Robot that) {
        return (this.px + this.wd >= that.px
            && this.py + this.ht >= that.py
            && that.px + that.wd >= this.px 
            && that.py + that.ht >= this.py);
    }
    
    public boolean willIntersect(Robot that) {
        int thisNextX = this.px + this.vx;
        int thisNextY = this.py + this.vy;
        int thatNextX = that.px + that.vx;
        int thatNextY = that.py + that.vy;
    
        return (thisNextX + this.wd >= thatNextX
            && thisNextY + this.ht >= thatNextY
            && thatNextX + that.wd >= thisNextX 
            && thatNextY + that.ht >= thisNextY);
    }
    
    //Change direction
    public void bounce(Directions d) {
        if (d == null) {
            return;
        }
        
        switch (d) {
            case N:
                this.vy = Math.abs(this.vy);
                break;  
            case S:
                this.vy = -Math.abs(this.vy);
                break;
            case W:
                this.vx = Math.abs(this.vx);
                break;
            case E:
                this.vx = -Math.abs(this.vx);
                break;
            default:
                break;
        }
    }
    
    //Change direction
    public void go(Directions d) {
        if (d == null) {
            return;
        }
        
        switch (d) {
            case N:
                this.vy = -Math.abs(this.vy);
                if (this.vy == 0) {
                	this.vy = -1;
                }
                this.vx = 0;
                
                break;  
            case S:
                this.vy = Math.abs(this.vy);
                if (this.vy == 0) {
                	this.vy = 1;
                }
                this.vx = 0;
                break;
            case W:
                this.vx = -Math.abs(this.vx);
                if (this.vx == 0) {
                	this.vx = -1;
                }
                this.vy = 0;
                break;
            case E:
                this.vx = Math.abs(this.vx);
                if (this.vx == 0) {
                	this.vx = 1;
                }
                this.vy = 0;
                break;
            default:
                break;
        }
    }
    
    public Directions hitObj(Robot that) {
        if (this.willIntersect(that)) {
            double dx = that.px + that.wd / 2 - (this.px + this.wd / 2);
            double dy = that.py + that.ht / 2 - (this.py + this.ht / 2);

            double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy * dy)));
            double diagTheta = Math.atan2(this.ht / 2, this.wd / 2);

            if (theta <= diagTheta) {
                return Directions.E;
            } else if (theta > diagTheta && theta <= Math.PI - diagTheta) {
                // Coordinate system for GUIs is switched
                if (dy > 0) {
                    return Directions.S;
                } else {
                    return Directions.N;
                }
            } else {
                return Directions.E;
            }
        } else {
            return null;
        }
    }
    
    public Directions hitWall() {
        if (this.px + this.vx < 0) {
            return Directions.W;
        } else if (this.px + this.vx > this.maxX) {
            return Directions.E;
        }

        if (this.py + this.vy < 0) {
            return Directions.N;
        } else if (this.py + this.vy > this.maxY) {
            return Directions.S;
        } else {
            return null;
        }
    }

    
    public int distanceTo(Robot that) {
        int dx = Math.abs(this.px - that.px);
        int dy = Math.abs(this.py - that.py);
        
        return (int) Math.sqrt((dx * dx) + (dy * dy));
    }
    
    public Directions getCurrDirection() {
    	
    	if (this.vx > 0 && this.vy == 0) {
    		return Directions.E;
    	}
    	
    	if (this.vx < 0 && this.vy == 0) {
    		return Directions.W;
    	}
    	
    	if (this.vy > 0 && this.vx == 0) {
    		return Directions.S;
    	}
    	
    	if (this.vy < 0  && this.vx == 0) {
    		return Directions.N;
    	}
    		
        if (this.vx > 0 && this.vy > 0) {
            return Directions.SE;
        }
        
        if (this.vx < 0 && this.vy < 0) {
            return Directions.NW;
        }
        
        if (this.vx > 0) {
            return Directions.NE;
        } else {
            return Directions.SW;
        } 
    }
    
    
    
    public abstract void draw(Graphics g);
}
