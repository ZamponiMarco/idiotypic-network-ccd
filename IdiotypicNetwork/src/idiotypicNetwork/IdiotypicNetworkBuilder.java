package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class IdiotypicNetworkBuilder implements ContextBuilder<Object> {

	
	
	
	@Override
	public Context build(Context<Object> context) {

		context.setId("IdiotypicNetwork");
		Parameters parameter = RunEnvironment.getInstance().getParameters();
		int gridHeight = parameter.getInteger("gridHeight");
		int gridWidth = parameter.getInteger("gridWidth");
		int antibodyTypeCount = parameter.getInteger("antibodyTypeCount");
		int antibodyEquilibriumMaxLength = parameter.getInteger("antibodyEquilibriumMaxLength");


		Random random = new Random(parameter.getInteger("randomSeed"));

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new RandomGridAdder<Object>(), true, gridHeight, gridWidth));

		
		Grid<Object> gridGlobal = gridFactory.createGrid("gridGlobal", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new RandomGridAdder<Object>(), true, gridHeight, gridWidth));
		
		
		
		double[][] matrix = new double[antibodyTypeCount][antibodyTypeCount];
		for (int i = 0; i < antibodyTypeCount; i++) {
			for (int j = 0; j < i; j++) {
				double initialValue = random.nextDouble() * 2 - 1; // value between -1 , 1
				matrix[i][j] = initialValue;
				matrix[j][i] = initialValue;

			}
		}
		
		
		ImmuneSystem immuneSystem = new ImmuneSystem(matrix, antibodyEquilibriumMaxLength);
		context.add(immuneSystem);
		
		ExternalAgent externalAgent = new ExternalAgent(gridGlobal, antibodyTypeCount);
		context.add(externalAgent);
		
		


		for (int i = 0; i < antibodyTypeCount; i++) {
			Antibody antibody = new Antibody(i, antibodyEquilibriumMaxLength);
			context.add(antibody);
			ColorTypeMapping.getInstance().getColor(antibody.type);
		}

		return context;

	}

}
