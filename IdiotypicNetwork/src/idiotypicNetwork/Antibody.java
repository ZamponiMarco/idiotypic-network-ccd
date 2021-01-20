package idiotypicNetwork;

import java.util.stream.Stream;

import repast.simphony.context.Context;
import repast.simphony.engine.controller.Controller;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import repast.simphony.visualization.editor.SpaceAddHandler;

/* 
 * Classe astratta che rappresenta un anticorpo. 
 * Fà parte della griglia e dello spazio e può essere viva, o morta. 
 * 
 * Ad ogni tick aggiornerà il suo stato.
 * */
public class Antibody {

	public boolean alive;
	public int type;
	public double hValue;
	public EquilibriumDataStructure eq;

	public Antibody( int type, int maxEquilibriumStateLength) {
		this.type = type;
		this.hValue = 0;
		this.alive = true;
		this.eq = new EquilibriumDataStructure(maxEquilibriumStateLength);
	}

	// The cell check its neighborhood to see determine its status
	@ScheduledMethod(start = 1, interval = 2, priority = 2)
	public void step() {

		ImmuneSystem immuneSystem = this.getImmuneSystem();

		if (this.alive && !immuneSystem.globalEquilibrium) {
		
			this.getAntibodies().forEach(antibody -> antibody.hValue += immuneSystem.matrix[this.type][antibody.type]);
			
		}

	}

	// The cell can die or revive
	@ScheduledMethod(start = 2, interval = 2, priority = 1)
	public void changeStatus() {
		if (!this.getImmuneSystem().globalEquilibrium) {
			if (this.hValue < 0) {
				this.alive = false;
			} else {
				this.alive = true;
			}

			eq.addState(this.alive ? "A" : "D");
			this.hValue=0;
		}
	}

	private ImmuneSystem getImmuneSystem() {
		Context<ImmuneSystem> context = ContextUtils.getContext(this);
		return context.getObjects(ImmuneSystem.class).get(0);
	}

	private Stream<Antibody> getAntibodies() {
		Context<Antibody> context = ContextUtils.getContext(this);
		return context.getObjectsAsStream(Antibody.class);

	}

}
