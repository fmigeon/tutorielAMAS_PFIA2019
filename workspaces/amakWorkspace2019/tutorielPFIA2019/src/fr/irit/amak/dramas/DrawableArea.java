package fr.irit.amak.dramas;

import fr.irit.amak.dramas.business.Area;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

public class DrawableArea extends Area {

	protected DrawableRectangle drawable;

	public DrawableArea(int x, int y) {
		super(x, y);
		drawable = VUI.get().createRectangle(x*10, y*10, 10,10);
		drawable.setLayer(0);

	}

}