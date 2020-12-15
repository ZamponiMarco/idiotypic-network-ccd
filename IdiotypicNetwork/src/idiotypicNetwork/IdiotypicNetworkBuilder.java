package idiotypicNetwork;

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

public class IdiotypicNetworkBuilder implements ContextBuilder<Antibody> {

	@Override
	public Context build(Context<Antibody> context) {

		
		
		context.setId("IdiotypicNetwork");
		Parameters parameter = RunEnvironment.getInstance().getParameters();
		int gridHeight = parameter.getInteger("gridHeight");
		int gridWidth = parameter.getInteger("gridWidth");
		
		double frequency = parameter.getDouble("cellFrequency");
		Random random = new Random(parameter.getInteger("randomSeed"));
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Antibody> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Antibody>(new WrapAroundBorders(),new SimpleGridAdder<Antibody>(), true, gridHeight, gridWidth));
		
		int antibodyCount = gridHeight*gridWidth;
		double[][] matrix = new double[antibodyCount][antibodyCount];
		for(int i = 0; i<antibodyCount; i++) {
			for(int j = 0; j< i ; j++) {	
				double initialValue = random.nextDouble() * 2 -1; //value between -1 , 1 
				matrix[i][j] = initialValue;
			    matrix[j][i] = initialValue;
			}
		}
		
		
		
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
			int type = random.nextInt(antibodyCount);
			Antibody antibody = random.nextDouble() < frequency ? new AliveAntibody(grid, type, matrix[type]): new DeadAntibody(grid, type, matrix[type]);
			context.add(antibody);
			grid.moveTo(antibody, i, j);
			}
		} 
		return context;
		
		
		
		
		
	}

}
