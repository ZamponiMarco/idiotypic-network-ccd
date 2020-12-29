package idiotypicNetwork;


import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class AliveAntibody extends Antibody{

	public AliveAntibody(Grid<Antibody> grid, int type, double[] interactionArcs) {
		super(grid, type, interactionArcs);
	
	}

	@Override
	public void step() {
		
		for (Antibody antibody : grid.getObjects()) {

			antibody.hValue += this.interactionArcs[antibody.type]; //aumenta l'hValue di ogni anticorpo in base al suo interaction arcs
			
		}
		
		
		
		
	}

	@Override
	public void changeStatus() {
		if(hValue<0) {
			GridPoint gpt = grid.getLocation(this);
			Context<Antibody> context = ContextUtils.getContext(this);
			context.remove(this);
			DeadAntibody dantibody = new DeadAntibody(grid, this.type, this.interactionArcs);
			context.add(dantibody);
			grid.moveTo(dantibody, gpt.getX(), gpt.getY());
		}
		//hValue = 0;
		
	}

}
