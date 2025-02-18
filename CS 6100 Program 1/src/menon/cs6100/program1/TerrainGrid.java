package menon.cs6100.program1;

import java.util.ArrayList;

public class TerrainGrid {

	private ArrayList<PointOnTerrain> grid;
	private int terrainWidth;
	private int terrainHeight;
	
	//Constructor takes dimensions of the terrain
	public TerrainGrid(int width, int height) {
		
		this.terrainWidth = width;
		this.terrainHeight = height;
		
		grid = new ArrayList<PointOnTerrain>(width * height);
		PointOnTerrain pointOnTerrain = null;
		for (int gridOffset = 0; gridOffset < this.terrainWidth * this.terrainHeight; ++gridOffset) {
			pointOnTerrain = new PointOnTerrain(getXCoordinate(gridOffset), getYCoordinate(gridOffset));
			this.grid.add(pointOnTerrain);
		}
	}
	
	//Get the point corresponding to the neighbor to the north
	public PointOnTerrain getNeighborToNorth(PointOnTerrain pointOnTerrain) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate())) {
			return null;
		}

		//Check if current point is on the border of the terrain
		if (pointOnTerrain.getyCoordinate() == 0) {
			return null;
		}
		
		int currentPointOffset = getTerrainOffset(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate());
		
		PointOnTerrain neighbor = grid.get(currentPointOffset - this.terrainWidth);
		
		//Check if point is inside a barrier
		if (neighbor.isAccessible()) {
			return neighbor;
		} else {
			return null;
		}
		
	}
	
	//Get the point corresponding to the neighbor to the south
	public PointOnTerrain getNeighborToSouth(PointOnTerrain pointOnTerrain) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate())) {
			return null;
		}
		
		//Check if current point is on the border of the terrain
		if (pointOnTerrain.getyCoordinate() == this.terrainHeight - 1) {
			return null;
		}
		
		int currentPointOffset = getTerrainOffset(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate());
		
		PointOnTerrain neighbor = grid.get(currentPointOffset + this.terrainWidth);
		
		//Check if point is inside a barrier
		if (neighbor.isAccessible()) {
			return neighbor;
		} else {
			return null;
		}
		
	}
	
	//Get the point corresponding to the neighbor to the East
	public PointOnTerrain getNeighborToEast(PointOnTerrain pointOnTerrain) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate())) {
			return null;
		}
		
		//Check if current point is on the border of the terrain
		if (pointOnTerrain.getxCoordinate() == this.terrainWidth - 1) {
			return null;
		}
		
		int currentPointOffset = getTerrainOffset(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate());
		
		PointOnTerrain neighbor = grid.get(currentPointOffset + 1);
		
		//Check if point is inside a barrier
		if (neighbor.isAccessible()) {
			return neighbor;
		} else {
			return null;
		}
		
	}
	
	//Get the point corresponding to the neighbor to the East
	public PointOnTerrain getNeighborToWest(PointOnTerrain pointOnTerrain) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate())) {
			return null;
		}
		
		//Check if current point is on the border of the terrain
		if (pointOnTerrain.getxCoordinate() == 0) {
			return null;
		}
		
		int currentPointOffset = getTerrainOffset(pointOnTerrain.getxCoordinate(), pointOnTerrain.getyCoordinate());
		
		PointOnTerrain neighbor = grid.get(currentPointOffset - 1);
		
		//Check if point is inside a barrier
		if (neighbor.isAccessible()) {
			return neighbor;
		} else {
			return null;
		}
		
	}
	
	//Set up rectangular barrier on terrain
	public void setRectangularBarrier(int topLeftXCoordinate, int topLeftYCoordinate, int rectangleWidth, int rectangleHeight) {
		
		if (!isValidCoordinate(topLeftXCoordinate, topLeftYCoordinate)) {
			return;
		}
		
		//Compute maximum X and Y allowed coordinates as the rectangle may not fit in the terrain
		int maximumXCoordinate = topLeftXCoordinate + rectangleWidth - 1 < this.terrainWidth ? topLeftXCoordinate + rectangleWidth - 1 : this.terrainWidth -1 ;
		int maximumYCoordinate = topLeftYCoordinate + rectangleHeight - 1 < this.terrainHeight ? topLeftYCoordinate + rectangleHeight - 1 : this.terrainHeight -1 ;
				
		//Mark area covered by the rectangle as inaccessible, i.e. has a barrier
		for (int rectangleXCoordinate = topLeftXCoordinate; rectangleXCoordinate <= maximumXCoordinate; ++rectangleXCoordinate) {
			for (int rectangleYCoordinate = topLeftYCoordinate; rectangleYCoordinate <= maximumYCoordinate; ++rectangleYCoordinate) {
				grid.get(getTerrainOffset(rectangleXCoordinate, rectangleYCoordinate)).setAccessible(false);
			}
		}
	}
	
	public int getWidth() {
		return this.terrainWidth;
	}
	
	public int getHeight() {
		return this.terrainHeight;
	}
	
	//Return x coordinate given an offset
	private int getXCoordinate(int gridOffset) {
		return gridOffset % this.terrainWidth;
	}
	
	//Return y coordinate given an offset
	private int getYCoordinate(int gridOffset) {
		return gridOffset / this.terrainWidth;
	}
	
	//Check if the coordinates passed in are valid for the terrain
	private boolean isValidCoordinate(int xCoordinate, int yCoordinate) {
		
		if (xCoordinate > this.terrainWidth - 1 || yCoordinate > this.terrainHeight - 1 || xCoordinate < 0 || yCoordinate < 0) {
			return false;
		} else {
			return true;
		}
				
	}
	
	//Return terrain offset in array list given x and y coordinates 
	private int getTerrainOffset(int xCoordinate, int yCoordinate) {
		
		return yCoordinate * this.terrainWidth + xCoordinate;
		
	}
	
}
