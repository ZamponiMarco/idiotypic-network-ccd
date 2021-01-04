package idiotypicNetwork;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Antigen {

	int type;
	public Grid<Object> grid;
	boolean moved;
	
	
	public Antigen (int type, Grid<Object> grid) {
		
		this.grid= grid;
		this.type = type;
		this.moved = false;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		ImmuneSystem immuneSystem = this.getImmuneSystem();
		if(immuneSystem.globalEquilibrium) {
			
			GridPoint myPoint = grid.getLocation(this);
			GridPoint isPoint = grid.getLocation(immuneSystem);
			
			float deltaX = isPoint.getX() - myPoint.getX();
			float deltaY = isPoint.getY() - myPoint.getY();
			double angle = Math.atan2( deltaY, deltaX ); 

			
			grid.moveByVector(this, 1, angle);
			this.moved = true;
		}
	}
	
	
	
	private ImmuneSystem getImmuneSystem() {
		Context<ImmuneSystem> context = ContextUtils.getContext(this);
		return context.getObjects(ImmuneSystem.class).get(0);
	}
	
}
