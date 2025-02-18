package menon.cs6100.program1;

//Class for storing scores associated with point on terrain
public class PointOnTerrain {
	
	private double gScore;
	private double fScore;
	private boolean isAccessible;
	private int xCoordinate;
	private int yCoordinate;
	
	//Constructor
	public PointOnTerrain(int xCoordinate, int yCoordinate) {
		this.gScore = 0.0;
		this.fScore = 0.0;
		this.isAccessible = true;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	
	//Get distance between point locations
	public double getDistanceToPoint(PointOnTerrain destinationPoint) {
		
		return Math.sqrt(Math.pow(this.getxCoordinate() - destinationPoint.getxCoordinate(), 2) + Math.pow(this.getyCoordinate() - destinationPoint.getyCoordinate(), 2));
		
	}
	
	//Getters and setters
	public double getgScore() {
		return gScore;
	}
	public void setgScore(double gScore) {
		this.gScore = gScore;
	}
	public double getfScore() {
		return fScore;
	}
	public void setfScore(double fScore) {
		this.fScore = fScore;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}
	
}
