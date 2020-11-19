package gameOfLife;

import repast.simphony.context.Context;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class DeadCell extends Cell{

	public DeadCell(Grid<Cell> grid) {
		super(grid);
		alive = false;	}

	@Override
	public void step() {
		MooreQuery<Cell> query = new MooreQuery<Cell>(grid, this);	
		int neighbours = 0;
		for (Cell cell : query.query()) {
			if( cell instanceof AliveCell) {
				neighbours++;
			}
		}
		
		if(neighbours == 3) {
			alive = true;
		}
		
	}

	@Override
	public void changeStatus() {

		if(alive) {
			GridPoint gpt = grid.getLocation(this);
			Context<Cell> context = ContextUtils.getContext(this);
			context.remove(this);
			AliveCell acell = new AliveCell(grid);
			context.add(acell);
			grid.moveTo(acell, gpt.getX(), gpt.getY());
		}
	}

}
