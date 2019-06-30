package fr.irit.amak.dramas;

import java.awt.Color;

import fr.irit.smac.amak.ui.VUI;

/**
 * A DrawableArea is an Area with a DrawableRectangle component 
 *
 */

public class ActiveDrawableArea extends DrawableArea {
	public ActiveDrawableArea(int x, int y) {
		super(x,y);
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

}
