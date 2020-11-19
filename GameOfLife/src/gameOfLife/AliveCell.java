package gameOfLife;

import repast.simphony.context.Context;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class AliveCell extends Cell{

	public AliveCell(Grid<Cell> grid) {
		super(grid);
		this.alive = true;
	}

	@Override
	public void step() {
		MooreQuery<Cell> query = new MooreQuery<Cell>(grid,this);
		int neighbours = 0;
		for (Cell cell : query.query()) {
			if(cell instanceof AliveCell) {
				neighbours++;
			}
		}
		
		if (neighbours != 2 && neighbours!=3) {
			this.alive = false;
		}
		
		
	}

	@Override
	public void changeStatus() {
		if(!alive) {
			GridPoint gpt = grid.getLocation(this);
			Context<Cell> context = ContextUtils.getContext(this);
			context.remove(this);
			DeadCell dcell = new DeadCell(grid);
			context.add(dcell);
			grid.moveTo(dcell, gpt.getX(), gpt.getY());
		}
	}

}
