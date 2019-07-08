package fr.irit.amak.dramas.business;

import java.util.List;

import fr.irit.amak.dramas.ActiveDrawableArea;
import fr.irit.amak.dramas.DroneAgent;
import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;

public class Drone {

	protected Position currentPosition;
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

	public Area getTargetArea() {
		return targetArea;
	}

	/**
	 * The current area of the drone. Located at dx, dy
	 */
	protected Area currentArea;

	/**
	 * Getter for the coordinates
	 * 
	 * @return the coordinates
	 */
	public Position getPosition() {
		return currentPosition;
	}

	public Area getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(Area currentArea) {
		this.currentArea = currentArea;
	}

	/**
	 * Find the closest drone from an area within a given list of drones
	 * 
	 * @param area   The concerned area
	 * @param drones The list of drones
	 * @return the closest drone
	 */
	public Drone closestDrone(Area area, List<Drone> drones) {
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
	 * @param area The area
	 * @return the distance between the drone and the area
	 */
	private double distanceTo(Area area) {
		return Math.sqrt(Math.pow(area.getCoordinates().getX() - currentPosition.getX(), 2) + Math.pow(area.getCoordinates().getY() - currentPosition.getY(), 2));
	}

	public void initPosition(Position pos) {
		currentPosition = pos;
	}

	public ActiveDrawableArea[][] getView() {
		return view;
	}

	public DroneAgent[][][] getNeighborsView() {
		return neighborsView;
	}

	public void setTargetArea(Area a) {
		targetArea = a;
	}
	/**
	 * Move toward an area. A drone can only move at 1
	 * position per cycle so the target area may not be the one seen.
	 * 
	 * @param a
	 *            The target area
	 */
	public void moveToward(Area a) {
		currentPosition.moveToward(a.getCoordinates());
	}


}