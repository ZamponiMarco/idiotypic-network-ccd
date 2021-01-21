package it.unicam.dcc.idiotypicnetwork;


import java.util.stream.IntStream;

import it.unicam.dcc.idiotypicnetwork.agent.Antibody;
import it.unicam.dcc.idiotypicnetwork.agent.ExternalAgent;
import it.unicam.dcc.idiotypicnetwork.agent.ImmuneSystem;
import it.unicam.dcc.idiotypicnetwork.visualization.ColorTypeMapping;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.controller.Controller;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
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
		int antibodyMaxAmountPerType = parameter.getInteger("antibodyMaxAmountPerType");
		int antibodyEquilibriumMaxLength = parameter.getInteger("antibodyEquilibriumMaxLength");
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new RandomGridAdder<Object>(), true, gridHeight, gridWidth));

		
		Grid<Object> gridGlobal = gridFactory.createGrid("gridGlobal", context, new GridBuilderParameters<Object>(
				new WrapAroundBorders(), new RandomGridAdder<Object>(), true, 41, 41));
		
		ImmuneSystem immuneSystem = new ImmuneSystem(antibodyTypeCount, antibodyEquilibriumMaxLength);
		context.add(immuneSystem);
		gridGlobal.moveTo(immuneSystem, gridHeight/2, gridWidth/2);
		
		ExternalAgent externalAgent = new ExternalAgent(gridGlobal, antibodyTypeCount);
		context.add(externalAgent);
		
		IntStream.range(0, antibodyTypeCount).forEach(i -> {
			int typeAmount = RandomHelper.nextIntFromTo(1, antibodyMaxAmountPerType);
			IntStream.range(0, typeAmount).forEach(n -> {
				Antibody antibody = new Antibody(i, antibodyEquilibriumMaxLength);
				context.add(antibody);
				ColorTypeMapping.getInstance().getColor(antibody.getType());
			});
		});

		return context;

	}

}
