package fr.irit.amak.dramas;

import java.awt.Color;

import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

/**
 * An area represents a small part of the world that can be scanned by a drone
 * in one cycle
 *
 */
public class Area {
	private static final int DEFAULT_SCAN_INTERVAL_TIME = 1000;
	/**
	 * The amount of time (in cycles) since when the area hasn't been scanned
	 */
	private double timeSinceLastSeen = DEFAULT_SCAN_INTERVAL_TIME;
	/**
	 * The X coordinate of the area
	 */
	private int x;
	/**
	 * The Y coordinate of the area
	 */
	private int y;
	/**
	 * The importance of the area. The higher this value, the more often this
	 * area must be scanned.
	 */
	private double outdateFactor;
	private double nextTimeSinceLastSeen = timeSinceLastSeen;
	private DrawableRectangle drawable;

	/**
	 * Constructor of the area
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public Area(int x, int y) {
		// Set the position
		this.x = x;
		this.y = y;
		// Set a high importance for a specific set of areas
		if (x > 10 && x < 20 && y > 10 && y < 30)
			this.outdateFactor = 10;
		else
			this.outdateFactor = 1;
		

		drawable = VUI.get().createRectangle(x*10, y*10, 10,10);
		drawable.setLayer(0);
	}

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

	/**
	 * This method is called when the drone scans the area
	 * 
	 * @param drone
	 *            The drone which scans the area
	 */
	public void seen(Drone drone) {
		nextTimeSinceLastSeen = 0;
	}

	/**
	 * Getter for the amount of time since last scan
	 * 
	 * @return the amount of time since last scan
	 */
	public double getTimeSinceLastSeen() {
		return timeSinceLastSeen;
	}

	/**
	 * Update the time since last scan at each cycle
	 */
	public void cycle() {
		nextTimeSinceLastSeen++;
		timeSinceLastSeen = nextTimeSinceLastSeen;

		if (timeSinceLastSeen > DEFAULT_SCAN_INTERVAL_TIME)
			timeSinceLastSeen = DEFAULT_SCAN_INTERVAL_TIME;
		drawable.setColor(new Color((float) timeSinceLastSeen / DEFAULT_SCAN_INTERVAL_TIME, 1 - (float) timeSinceLastSeen / DEFAULT_SCAN_INTERVAL_TIME, 0f));
	}

	/**
	 * Manually set a hgh criticality to request a scan on a specific area
	 */
	public void setCritical() {
		nextTimeSinceLastSeen  = DEFAULT_SCAN_INTERVAL_TIME;
	}

	/**
	 * Compute the criticality of the area based on the time since last scan
	 * 
	 * @return the criticality of the area
	 */
	public double computeCriticality() {
		return Math.min(timeSinceLastSeen * outdateFactor / DEFAULT_SCAN_INTERVAL_TIME, 1);
	}
}
