package idiotypicNetwork;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import java.util.Random;

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

			Random r = new Random();
			if (r.nextDouble() < this.newAntigenPercentage) {

				int antigenType = antigenTypeCount;
				context.add(new Antigen(antigenType, this.grid));
				antigenTypeCount++;
			} else {
				context.add(new Antigen(r.nextInt(antigenTypeCount),this.grid));
			}

		}

		// 1 - controlla il tipo
		// se gia c'è lo elimina e lo stato rimane uguale
		// altrimenti aggiunge la nuova riga a tutti gli anticorpi spara un nuovo
		// anticoorpo all'interno della grid e mette il proprio stato di global
		// equilibrium false

	}

}
