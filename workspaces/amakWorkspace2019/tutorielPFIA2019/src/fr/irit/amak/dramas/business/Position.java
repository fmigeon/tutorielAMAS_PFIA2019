package fr.irit.amak.dramas.business;

public class Position {

	/**
	 * The X coordinate of the entity
	 */
	protected int x;
	/**
	 * The Y coordinate of the entity
	 */
	protected int y;
	
	/**
	 * Getter for the X coordinate
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for the Y coordinate
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	public void moveToward(Position coordinates) {
		if (x < coordinates.getX())
			x++;
		else if (x > coordinates.getX())
			x--;
		if (y < coordinates.getY())
			y++;
		else if (y > coordinates.getY())
			y--;		
	}



}
