package idiotypicNetwork;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class ExternalAgent {

	public int antigenTypeCount;
	public Grid<Object> grid;
	public double newAntigenPercentage;

	public ExternalAgent(Grid<Object> grid, int antigenTypeCount) {
		this.grid = grid;
		this.antigenTypeCount = antigenTypeCount;
	}

	@Watch(watcheeClassName = "idiotypicNetwork.ImmuneSystem", watcheeFieldNames = "globalEquilibrium", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void createAntigen() {

		Context<Antigen> context = ContextUtils.getContext(this);

		if (context.getObjectsAsStream(Antigen.class).count() == 0) {

			if (RandomHelper.nextDoubleFromTo(0, 1) < this.newAntigenPercentage) {	
				int antigenType = antigenTypeCount;
				context.add(new Antigen(antigenType, this.grid));
				antigenTypeCount++;
			} else {
				context.add(new Antigen(RandomHelper.nextIntFromTo(0, antigenTypeCount - 1),this.grid));
			}

		}

	}

}
