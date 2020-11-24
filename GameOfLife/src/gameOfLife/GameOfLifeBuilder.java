package gameOfLife;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class GameOfLifeBuilder implements ContextBuilder<Cell>{

	@Override
	public Context<Cell> build(Context<Cell> context) {

		context.setId("GameOfLife");
		
		Parameters parameter = RunEnvironment.getInstance().getParameters();
		int gridHeight = parameter.getInteger("gridHeight");
		int gridWidth = parameter.getInteger("gridWidth");
		double frequency = parameter.getDouble("cellFrequency");
		Random random = new Random(parameter.getInteger("randomSeed"));
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Cell> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Cell>(new WrapAroundBorders(),new SimpleGridAdder<Cell>(), true, gridHeight, gridWidth));
		
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
			Cell cell = random.nextDouble() < frequency ? new AliveCell(grid): new DeadCell(grid);
			context.add(cell);
			grid.moveTo(cell, i, j);
			}
		} 
		return context;
	}

}
