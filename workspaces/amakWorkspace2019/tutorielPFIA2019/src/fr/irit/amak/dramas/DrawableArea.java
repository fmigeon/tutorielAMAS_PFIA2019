package fr.irit.amak.dramas;

import java.awt.Color;

import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

/**
 * An area represents a small part of the world that can be scanned by a drone
 * in one cycle
 *
 */
public class DrawableArea extends Area {
	private DrawableRectangle drawable;

	/**
	 * Constructor of the area
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public DrawableArea(int x, int y) {
		super(x,y);

		drawable = VUI.get().createRectangle(x*10, y*10, 10,10);
		drawable.setLayer(0);
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
