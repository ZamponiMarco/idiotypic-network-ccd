package gameOfLife;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;

/* 
 * Classe astratta che rappresenta una cellula. 
 * Fà parte della griglia e può essere viva, o morta. 
 * 
 * Ad ogni tick controllerà aggiornerà il suo stato, e creerà la cellula relativa.
 * */
public abstract class Cell {

	public Grid<Cell> grid;
	public boolean alive;
	
	public Cell(Grid<Cell> grid) {
		this.grid=grid;
	}
	
	//The cell check its neighborhood to see determine its status
	@ScheduledMethod(start = 1, interval=1, priority=2)
	public abstract void step();
	
	//The cell can die or revive
	@ScheduledMethod(start = 1, interval=1, priority=1)
	public abstract void changeStatus();

	/**
	 * Get the number of alive cells in a Moore neighborhood around
	 * the {@link gameOfLife.Cell}
	 * 
	 * @param query Query containing the Moore neighbours of this {@link gameOfLife.Cell}
	 * @return number of instances of {@link gameOfLife.AliveCell} around this {@link gameOfLife.Cell}
	 */
	protected int getNeighboursNumber(MooreQuery<Cell> query) {
		int neighbours = 0;
		for (Cell cell : query.query()) {
			if(cell instanceof AliveCell) {
				neighbours++;
			}
		}
		return neighbours;
	}
}
