package it.unicam.dcc.idiotypicnetwork.agent;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class ExternalAgent {

	private int antigenTypeCount;
	private Grid<Object> grid;
	private double newAntigenPercentage;

	public ExternalAgent(Grid<Object> grid, int antigenTypeCount, double newAntigenPercentage) {
		this.grid = grid;
		this.antigenTypeCount = antigenTypeCount;
		this.newAntigenPercentage = newAntigenPercentage;
	}

	@Watch(watcheeClassName = "it.unicam.dcc.idiotypicnetwork.agent.ImmuneSystem", watcheeFieldNames = "globalEquilibrium", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void createAntigen() {

		Context<Antigen> context = ContextUtils.getContext(this);

		if (context.getObjectsAsStream(Antigen.class).count() == 0) {

			if (RandomHelper.nextDoubleFromTo(0, 1) < this.newAntigenPercentage) {	
				context.add(new Antigen(RandomHelper.nextIntFromTo(0, antigenTypeCount - 1),this.grid));
			} else {
				int antigenType = antigenTypeCount;
				context.add(new Antigen(antigenType, this.grid));
				antigenTypeCount++;
			}

		}

	}

}
