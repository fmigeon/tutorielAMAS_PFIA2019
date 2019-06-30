package fr.irit.amak.dramas;

public class Area {

	/**
	 * The amount of time (in cycles) since when the area hasn't been scanned
	 */
	protected double timeSinceLastSeen = DEFAULT_SCAN_INTERVAL_TIME;
	/**
	 * The X coordinate of the area
	 */
	protected int x;
	/**
	 * The Y coordinate of the area
	 */
	protected int y;
	/**
	 * The importance of the area. The higher this value, the more often this
	 * area must be scanned.
	 */
	protected double outdateFactor;
	protected double nextTimeSinceLastSeen = timeSinceLastSeen;
	private static final int NORMAL_PRIORITY_FACTOR = 1;

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

	protected void setHigherImportanceZone(int x, int y) {
		if (belongsToPriorityZone(x, y))
			this.outdateFactor = HIGH_PRIORITY_FACTOR;
		else
			this.outdateFactor = NORMAL_PRIORITY_FACTOR;
	}

	private boolean belongsToPriorityZone(int x, int y) {
		return x > PRIORITORY_ZONE_MIN_ABS && x < PRIORITORY_ZONE_MAX_ABS && y > PRIORITORY_ZONE_MIN_ORD && y < PRIORITORY_ZONE_MAX_ORD;
	}

	private static final int HIGH_PRIORITY_FACTOR = 10;
	private static final int PRIORITORY_ZONE_MAX_ORD = 30;
	private static final int PRIORITORY_ZONE_MIN_ORD = 10;
	private static final int PRIORITORY_ZONE_MAX_ABS = 20;
	private static final int PRIORITORY_ZONE_MIN_ABS = 10;
	protected static final int DEFAULT_SCAN_INTERVAL_TIME = 1000;

	public Area(int x, int y) {
		super();
		// Set the position
		this.x = x;
		this.y = y;
		setHigherImportanceZone(x, y);

	}

}