package idiotypicNetwork;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/* 
 * Classe astratta che rappresenta un anticorpo. 
 * Fà parte della griglia e dello spazio e può essere viva, o morta. 
 * 
 * Ad ogni tick aggiornerà il suo stato.
 * */
public abstract class Antibody {

	public Grid<Antibody> grid;
	
	
	public boolean alive;
	public int type;
	public double hValue;
	public double[] interactionArcs;
	
	public Antibody(Grid<Antibody> grid, int type, double[] interactionArcs) {
		this.grid=grid;
		this.type = type;
		this.hValue = 0;
		this.interactionArcs = interactionArcs;
	}
	
	//The cell check its neighborhood to see determine its status
	@ScheduledMethod(start = 1, interval=1, priority=2)
	public abstract void step();
	
	//The cell can die or revive
	@ScheduledMethod(start = 1, interval=1, priority=1)
	public abstract void changeStatus();
	
}
