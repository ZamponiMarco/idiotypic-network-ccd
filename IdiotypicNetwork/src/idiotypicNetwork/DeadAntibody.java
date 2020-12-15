package idiotypicNetwork;


import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class DeadAntibody extends Antibody{

	public DeadAntibody(Grid<Antibody> grid, int type, double[] interactionArcs) {
		super(grid, type, interactionArcs);
	
	}

	@Override
	public void step() {

		//SONO MORTO NON POSSO FARE NULLA
		
	}

	@Override
	public void changeStatus() {
		if(hValue>0) {
			GridPoint gpt = grid.getLocation(this);
			Context<Antibody> context = ContextUtils.getContext(this);
			context.remove(this);
			AliveAntibody aantibody = new AliveAntibody(grid, this.type, this.interactionArcs);
			context.add(aantibody);
			grid.moveTo(aantibody, gpt.getX(), gpt.getY());
		}
		hValue = 0;
		
	}

}
