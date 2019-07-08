package fr.irit.amak.dramas.business;

/**
 * An area represents a small part of the world that can be scanned by a drone
 * in one cycle
 *
 */
public class Area {

	/**
	 * The amount of time (in cycles) since when the area hasn't been scanned
	 */
	protected double timeSinceLastScan = DEFAULT_SCAN_INTERVAL_TIME;
	protected final Position coordinates;
	/**
	 * The importance of the area. The higher this value, the more often this area
	 * must be scanned.
	 */
	protected double outdateFactor;
	protected double nextTimeSinceLastScan = timeSinceLastScan;

	/**
	 * Constructor of the area
	 * 
	 * @param coordinates Coordinates of the area
	 */
	public Area(Position coordinates) {
		super();
		this.coordinates = coordinates;
		setHigherImportanceZone(coordinates);

	}

	public Position getCoordinates() {
		return coordinates;
	}

	/**
	 * } This method is called when the area is scanned
	 */
	public void scanned() {
		nextTimeSinceLastScan = 0;
	}

	/**
	 * Getter for the amount of time since last scan
	 * 
	 * @return the amount of time since last scan
	 */
	public double getTimeSinceLastSeen() {
		return timeSinceLastScan;
	}

	protected void setHigherImportanceZone(Position pos) {
		if (belongsToPriorityZone(pos))
			this.outdateFactor = HIGH_PRIORITY_FACTOR;
		else
			this.outdateFactor = NORMAL_PRIORITY_FACTOR;
	}

	private boolean belongsToPriorityZone(Position pos) {
		return pos.getX() > PRIORITORY_ZONE_MIN_ABS && pos.getX() < PRIORITORY_ZONE_MAX_ABS && pos.getY() > PRIORITORY_ZONE_MIN_ORD
				&& pos.getY() < PRIORITORY_ZONE_MAX_ORD;
	}

	/**
	 * Turns the area state into an urgent mode
	 */
	public void becomeUrgent() {
		nextTimeSinceLastScan = DEFAULT_SCAN_INTERVAL_TIME;
	}

	/**
	 * Compute the emergency of scanning the area based on the time since last scan
	 * 
	 * @return the relative emergency value of the area
	 */
	public double computeEmergencyValue() {
		return Math.min(timeSinceLastScan * outdateFactor / DEFAULT_SCAN_INTERVAL_TIME, 1);
	}

	private static final int HIGH_PRIORITY_FACTOR = 10;
	private static final int NORMAL_PRIORITY_FACTOR = 1;
	private static final int PRIORITORY_ZONE_MAX_ORD = 30;
	private static final int PRIORITORY_ZONE_MIN_ORD = 10;
	private static final int PRIORITORY_ZONE_MAX_ABS = 20;
	private static final int PRIORITORY_ZONE_MIN_ABS = 10;
	protected static final int DEFAULT_SCAN_INTERVAL_TIME = 1000;

}