package fr.irit.amak.dramas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

/**
 * This class represent an agent of the system DrAmas
 *
 */
public class Drone extends Agent<DrAmas, World> {
	/**
	 * Current coordinate of the drone
	 */
	private int dx, dy;

	/**
	 * View radius of the agent. The drone will be able to perceive drones and areas
	 * within a square of 3x3
	 */
	public static int VIEW_RADIUS = 1;

	/**
	 * The areas perceived by the agent during the perception phase
	 */
	private DrawableArea[][] view = new DrawableArea[VIEW_RADIUS * 2 + 1][VIEW_RADIUS * 2 + 1];
	/**
	 * The drone perceived by the agent during the perception phase
	 */
	private Drone[][][] neighborsView = new Drone[VIEW_RADIUS * 2 + 1][VIEW_RADIUS * 2 + 1][];

	/**
	 * The area the drone will try to reach during the action phase
	 */
	private Area targetArea;

	/**
	 * The current area of the drone. Located at dx, dy
	 */
	private Area currentArea;

	private DrawableRectangle drawable;

	/**
	 * Constructor of the drone
	 * 
	 * @param amas
	 *            The AMAS the drone belongs to
	 * @param startX
	 *            The initial x coordinate of the drone
	 * @param startY
	 *            The initial y coordinate of the drone
	 */
	public Drone(DrAmas amas, int startX, int startY) {
		super(amas, startX, startY);
	}

	@Override
	protected void onInitialization() {

		dx = (int) params[0];
		dy = (int) params[1];
	}
	@Override
	protected void onRenderingInitialization() {
		drawable = VUI.get().createRectangle(dx*10, dy*10, 10,10);
		drawable.setLayer(1);
		drawable.setColor(Color.WHITE);
	}

	/**
	 * Initialize the first area of the drone
	 */
	@Override
	protected void onReady() {
		currentArea = amas.getEnvironment().getAreaByPosition(dx, dy);
	}

	/**
	 * Compute the criticality of the drone (if any)
	 */
	@Override
	protected double computeCriticality() {
		return 0;
	}

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
	 * Perception phase of the agent
	 */
	@Override
	protected void onPerceive() {
		// Clear the last set neighbors list
		clearNeighbors();
		// Check areas in a range of 3x3
		for (int x = -VIEW_RADIUS; x <= VIEW_RADIUS; x++) {
			for (int y = -VIEW_RADIUS; y <= VIEW_RADIUS; y++) {
				DrawableArea areaByPosition = amas.getEnvironment().getAreaByPosition(dx + x, dy + y);
				// store the seen areas
				view[y + VIEW_RADIUS][x + VIEW_RADIUS] = areaByPosition;
				Drone[] agentsInArea = amas.getAgentsInArea(areaByPosition);
				// store the seen agents
				neighborsView[y + VIEW_RADIUS][x + VIEW_RADIUS] = agentsInArea;
				// Set seen agents as neighbors
				addNeighbor(agentsInArea);
			}
		}
	}

	/**
	 * Clear the neighbors list
	 */
	private void clearNeighbors() {
		neighborhood.clear();
	}

	/**
	 * Agent action phase. Move toward a specified area
	 */
	@Override
	protected void onAct() {
		if (targetArea != null) {
			moveToward(targetArea);
		}
	}

	@Override
	protected void onUpdateRender() {
		drawable.move(dx*10, dy*10);
	}

	/**
	 * Decision phase. This method must be completed. In the action phase, the drone
	 * will move toward the area in the attribute "targetArea"
	 * 
	 * Examples: Move the drone to the right targetArea =
	 * amas.getEnvironment().getAreaByPosition(dx+1, dy);
	 * 
	 * Move the drone toward another drone targetArea = otherDrone.getCurrentArea();
	 */
	@Override
	protected void onDecide() {
		// TODO Fill
		/* DEBUT DU CODE A COLLER */

		//Création de listes pour faciliter le tri
		        List<DrawableArea> areas = new ArrayList<>();
		        List<Drone> visibleDrones = new ArrayList<>();

		        for (int x = -VIEW_RADIUS; x <= VIEW_RADIUS; x++) {
		            for (int y = -VIEW_RADIUS; y <= VIEW_RADIUS; y++) {
		                if (view[y + VIEW_RADIUS][x + VIEW_RADIUS] != null)
		                    areas.add(view[y + VIEW_RADIUS][x + VIEW_RADIUS]);
		                if (neighborsView[y + VIEW_RADIUS][x + VIEW_RADIUS] !=
		null) {
		                    for (Drone drone : neighborsView[y + VIEW_RADIUS][x 
		+ VIEW_RADIUS]) {
		                        visibleDrones.add(drone);
		                    }
		                }
		            }
		        }

		//Tri des parcelles de la plus critique à la moins critique

		        Collections.sort(areas, new Comparator<DrawableArea>() {

		            @Override
		            public int compare(DrawableArea o1, DrawableArea o2) {
		                return (int) (o2.computeCriticality()*10000 - o1.computeCriticality()*10000);
		            }
		        });


		//On choisit la parcelle la plus critique ET dont je suis le plus proche
		        Area a = getAreaImTheClosestTo(areas, visibleDrones);


		//décommenter les parties suivantes pour essayer des comportements aléatoires
		        //Comportement aléatoire 1
//		         a = areas.get(amas.getRandom().nextInt(areas.size()));

		        //Comportement aléatoire 2
//		        if (getCurrentArea().getX() == targetX && getCurrentArea().getY() == targetY) {
//		            targetX = amas.getRandom().nextInt(World.WIDTH);
//		            targetY = amas.getRandom().nextInt(World.HEIGHT);
//		        }
//		        a = amas.getEnvironment().getAreaByPosition(targetX, targetY);



		        targetArea = a;

		/* FIN DU CODE A COLLER */
	}

	/**
	 * From an ordered list of areas (areas) and a list of drone, return the first
	 * area I'm the closest to.
	 * 
	 * @param areas
	 *            Ordered list of areas
	 * @param drones
	 *            List of drones
	 * @return the first area I'm the closest to
	 */
	private Area getAreaImTheClosestTo(List<DrawableArea> areas, List<Drone> drones) {
		for (Area area : areas) {
			if (closestDrone(area, drones) == this)
				return area;
		}
		return areas.get(getAmas().getEnvironment().getRandom().nextInt(areas.size()));
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
	private Drone closestDrone(Area area, List<Drone> drones) {
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

	/**
	 * Move toward an area and scan the reached area. A drone can only move at 1
	 * area /cycle so the target area may not be the one seen.
	 * 
	 * @param a
	 *            The target area
	 */
	protected void moveToward(Area a) {
		if (dx < a.getX())
			dx++;
		else if (dx > a.getX())
			dx--;
		if (dy < a.getY())
			dy++;
		else if (dy > a.getY())
			dy--;
		currentArea = amas.getEnvironment().getAreaByPosition(dx, dy);
		currentArea.seen(this);
	}

	/**
	 * Getter for the area of the drone
	 * 
	 * @return the current area
	 */
	public Area getCurrentArea() {
		return currentArea;
	}
}
