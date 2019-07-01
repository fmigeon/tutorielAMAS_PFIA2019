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
public class DroneAgent extends Agent<DrAmas, World> {
	private static final int VIEW_RADIUS = Drone.VIEW_RADIUS;
	private Drone droneDelegate = new Drone();
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
	public DroneAgent(DrAmas amas, int startX, int startY) {
		super(amas, startX, startY);
	}

	/**
	 * Getter for the x coordinate
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return droneDelegate.dx;
	}

	/**
	 * Getter for the y coordinate
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return droneDelegate.dy;
	}


	@Override
	protected void onInitialization() {

		droneDelegate.dx = (int) params[0];
		droneDelegate.dy = (int) params[1];
	}
	@Override
	protected void onRenderingInitialization() {
		drawable = VUI.get().createRectangle(droneDelegate.dx*10, droneDelegate.dy*10, 10,10);
		drawable.setLayer(1);
		drawable.setColor(Color.WHITE);
	}

	/**
	 * Initialize the first area of the drone
	 */
	@Override
	protected void onReady() {
		initCurrentArea();
	}

	/**
	 * Compute the criticality of the drone (if any)
	 */
	@Override
	protected double computeCriticality() {
		return 0;
	}

	/**
	 * Perception phase of the agent
	 */
	@Override
	protected void onPerceive() {
		// Clear the last set neighbors list
		clearNeighbors();
		// Check areas in a range of VIEW_RADIUSxVIEW_RADIUS
		for (int x = -VIEW_RADIUS; x <= VIEW_RADIUS; x++) {
			for (int y = -VIEW_RADIUS; y <= VIEW_RADIUS; y++) {
				ActiveDrawableArea areaByPosition = amas.getEnvironment().getAreaByPosition(droneDelegate.dx + x, droneDelegate.dy + y);
				// store the seen areas
				droneDelegate.view[y + VIEW_RADIUS][x + VIEW_RADIUS] = areaByPosition;
				DroneAgent[] agentsInArea = amas.getAgentsInArea(areaByPosition);
				// store the seen agents
				droneDelegate.neighborsView[y + VIEW_RADIUS][x + VIEW_RADIUS] = agentsInArea;
				// Set seen agents as neighbors
				addNeighbor(agentsInArea);
			}
		}
	}

	/**
	 * Agent action phase. Move toward a specified area
	 */
	@Override
	protected void onAct() {
		if (droneDelegate.targetArea != null) {
			moveToward(droneDelegate.targetArea);
		}
	}

	@Override
	protected void onUpdateRender() {
		drawable.move(droneDelegate.dx*10, droneDelegate.dy*10);
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
		        List<ActiveDrawableArea> areas = new ArrayList<>();
		        List<Drone> visibleDrones = new ArrayList<>();

		        for (int x = -VIEW_RADIUS; x <= VIEW_RADIUS; x++) {
		            for (int y = -VIEW_RADIUS; y <= VIEW_RADIUS; y++) {
		                if (droneDelegate.view[y + VIEW_RADIUS][x + VIEW_RADIUS] != null)
		                    areas.add(droneDelegate.view[y + VIEW_RADIUS][x + VIEW_RADIUS]);
		                if (droneDelegate.neighborsView[y + VIEW_RADIUS][x + VIEW_RADIUS] !=
		null) {
		                    for (DroneAgent drone : droneDelegate.neighborsView[y + VIEW_RADIUS][x 
		+ VIEW_RADIUS]) {
		                        visibleDrones.add(drone.droneDelegate);
		                    }
		                }
		            }
		        }

		//Tri des parcelles de la plus critique à la moins critique

		        Collections.sort(areas, new Comparator<ActiveDrawableArea>() {

		            @Override
		            public int compare(ActiveDrawableArea o1, ActiveDrawableArea o2) {
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



		        droneDelegate.targetArea = a;

		/* FIN DU CODE A COLLER */
	}

	/**
	 * Move toward an area and scan the reached area. A drone can only move at 1
	 * area /cycle so the target area may not be the one seen.
	 * 
	 * @param a
	 *            The target area
	 */
	protected void moveToward(Area a) {
		if (droneDelegate.dx < a.getX())
			droneDelegate.dx++;
		else if (droneDelegate.dx > a.getX())
			droneDelegate.dx--;
		if (droneDelegate.dy < a.getY())
			droneDelegate.dy++;
		else if (droneDelegate.dy > a.getY())
			droneDelegate.dy--;
		initCurrentArea();
		droneDelegate.currentArea.scanned();
	}

	/**
	 * Getter for the area of the drone
	 * 
	 * @return the current area
	 */
	public Area getCurrentArea() {
		return droneDelegate.currentArea;
	}

	protected void initCurrentArea() {
		droneDelegate.currentArea = amas.getEnvironment().getAreaByPosition(droneDelegate.dx, droneDelegate.dy);
	}

	/**
	 * Clear the neighbors list
	 */
	protected void clearNeighbors() {
		neighborhood.clear();
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
	protected Area getAreaImTheClosestTo(List<ActiveDrawableArea> areas, List<Drone> drones) {
		for (Area area : areas) {
			if (droneDelegate.closestDrone(area, drones) == droneDelegate)
				return area;
		}
		return areas.get(getAmas().getEnvironment().getRandom().nextInt(areas.size()));
	}
}
