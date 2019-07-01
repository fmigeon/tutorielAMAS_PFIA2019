package fr.irit.amak.dramas;

import java.util.List;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;

public class Drone {

	/**
	 * Current coordinate of the drone
	 */
	protected int dx;
	/**
	 * Current coordinate of the drone
	 */
	protected int dy;
	/**
	 * View radius of the agent. The drone will be able to perceive drones and areas
	 * within a square of 3x3
	 */
	public static int VIEW_RADIUS = 1;
	/**
	 * The areas perceived by the agent during the perception phase
	 */
	protected ActiveDrawableArea[][] view = new ActiveDrawableArea[VIEW_RADIUS * 2 + 1][VIEW_RADIUS * 2 + 1];
	/**
	 * The drone perceived by the agent during the perception phase
	 */
	protected DroneAgent[][][] neighborsView = new DroneAgent[VIEW_RADIUS * 2 + 1][VIEW_RADIUS * 2 + 1][];
	/**
	 * The area the drone will try to reach during the action phase
	 */
	protected Area targetArea;
	/**
	 * The current area of the drone. Located at dx, dy
	 */
	protected Area currentArea;


	/**
	 * Getter for the x coordinate
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return dx;
	}

	/**
	 * Getter for the y coordinate
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return dy;
	}

	/**
	 * Find the closest drone from an area within a given list of drones
	 * 
	 * @param area
	 *            The concerned area
	 * @param drones
	 *            The list of drones
	 * @return the closest drone
	 */
	protected Drone closestDrone(Area area, List<Drone> drones) {
		double distance = Double.POSITIVE_INFINITY;
		Drone closest = this;
		for (Drone drone : drones) {
			if (drone.distanceTo(area) < distance) {
				distance = drone.distanceTo(area);
				closest = drone;
			}
		}
		return closest;
	}

	/**
	 * Distance from the drone to a specified area
	 * 
	 * @param area
	 *            The area
	 * @return the distance between the drone and the area
	 */
	private double distanceTo(Area area) {
		return Math.sqrt(Math.pow(area.getX() - dx, 2) + Math.pow(area.getY() - dy, 2));
	}

}