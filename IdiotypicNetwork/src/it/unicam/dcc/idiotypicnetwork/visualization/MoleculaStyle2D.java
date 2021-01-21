package it.unicam.dcc.idiotypicnetwork.visualization;

import java.awt.Color;

import it.unicam.dcc.idiotypicnetwork.agent.Antibody;
import it.unicam.dcc.idiotypicnetwork.agent.Antigen;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class MoleculaStyle2D extends DefaultStyleOGL2D {

	@Override
	public Color getColor(Object agent) {

		if (agent instanceof Antibody) {

			if (!((Antibody) agent).isAlive()) {
				return Color.WHITE;
			} else {
				return ColorTypeMapping.getInstance().getColor(((Antibody) agent).getType());
			}
		}

		if (agent instanceof Antigen) {
			return ColorTypeMapping.getInstance().getColor(((Antigen) agent).getType());
		}
		
		
		return super.getColor(agent);

	}
	
	@Override
	public Color getBorderColor(Object agent) {
		if (agent instanceof Antibody) {

			if (((Antibody) agent).getEq().updateAndGetEquilibrium()) {
				return Color.GREEN;
			}else {
				return Color.WHITE;
			}
		}
		return super.getBorderColor(agent);
	}
	
	@Override
	public int getBorderSize(Object agent) {
		if (agent instanceof Antibody) {

			if (((Antibody) agent).getEq().isEquilibrium() == true) {
				return 2;
			}else {
				return 0;
			}
		}
		return 0;
	}
	
	

	/**
	 * @return a circle of radius 4.
	 */
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {

			spatial = shapeFactory.createCircle(8, 16);

		}

		return spatial;
	}

}
