package it.unicam.dcc.idiotypicnetwork.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import cern.jet.random.Uniform;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ContextUtils;

public class ImmuneSystem {

	private boolean globalEquilibrium;
	private double[][] matrix;
	private boolean isInfected;
	private int maxEquilibriumStateLength;
	
	private List<Integer> infectionTimes;
	private int currentInfectionTime;

	public ImmuneSystem(int antibodyTypeCount, int maxEquilibriumStateLength) {
		this.globalEquilibrium = false;
		
		Uniform uniform = RandomHelper.createUniform(-1, 1);
		
		this.matrix = new double[antibodyTypeCount][antibodyTypeCount];
		for (int i = 0; i < antibodyTypeCount; i++) {
			for (int j = 0; j < i; j++) {
				double initialValue = uniform.nextDouble(); // value between -1 , 1
				matrix[i][j] = initialValue;
				matrix[j][i] = initialValue;

			}
		}
		
		this.isInfected = false;
		this.maxEquilibriumStateLength = maxEquilibriumStateLength;
		
		this.infectionTimes = new ArrayList<>();
		this.currentInfectionTime = 0;
	}

	@ScheduledMethod(start = 1, interval = 2, priority = 2)
	public void checkEquilibrium() {
		Context<Antibody> context = ContextUtils.getContext(this);
		this.currentInfectionTime++;
		this.globalEquilibrium = context.getObjectsAsStream(Antibody.class).allMatch(agent -> agent.getEq().updateAndGetEquilibrium());
		if (this.globalEquilibrium && this.isInfected) {
			this.removeAntigen();
			this.infectionTimes.add(currentInfectionTime);
			this.isInfected = false;
		}

	}

	@Watch(watcheeClassName = "it.unicam.dcc.idiotypicnetwork.agent.Antigen", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void reactToAntigen() {
		this.getAntibodies().forEach(antibody -> antibody.getEq().reset());
		this.globalEquilibrium = false;
		this.isInfected = true;
		this.currentInfectionTime = 0;

		Antigen antigen = this.getAntigen();
		if (antigen != null && antigen.getType() < this.matrix.length) {
			Context<Antibody> context = ContextUtils.getContext(this);

			this.addNewAntibodyToMatrix();
			context.add(new Antibody(antigen.getType(), this.maxEquilibriumStateLength));
		}

	}

	private Stream<Antibody> getAntibodies() {
		Context<Antibody> context = ContextUtils.getContext(this);
		return context.getObjectsAsStream(Antibody.class);
	}

	private void removeAntigen() {
		Context<Antigen> context = ContextUtils.getContext(this);
		if (context.getObjectsAsStream(Antigen.class).count() > 0) {
			Antigen antigen = context.getObjects(Antigen.class).get(0);
			context.remove(antigen);
		}
	}

	private Antigen getAntigen() {
		Context<Antigen> context = ContextUtils.getContext(this);
		if (context.getObjectsAsStream(Antigen.class).count() > 0) {
			return context.getObjects(Antigen.class).get(0);
		}
		return null;
	}

	private void addNewAntibodyToMatrix() {
		double[][] newMatrix = new double[this.matrix.length + 1][this.matrix.length + 1];

		Random random = new Random();

		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix.length; j++) {
				newMatrix[i][j] = matrix[i][j];
			}
		}
		
		for (int i = 0; i < newMatrix.length; i++) {
			double randomValue = random.nextDouble() * 2 - 1;
			newMatrix[this.matrix.length][i] = randomValue;
			newMatrix[i][this.matrix.length] = randomValue;
		}
		newMatrix[matrix.length][matrix.length] = 0;
		
		this.matrix = newMatrix;
	}
	
	
	public int getMatrixLength() {
		return this.getMatrix().length;
	}
	
	public int isInfected() {
		return isInfected?1:0;
	}
	
	public double averageInfectionTime() {
		if (infectionTimes.size() != 0) {			
			return infectionTimes.stream().reduce(0, Integer::sum) / (double) infectionTimes.size();
		} 
		return 0;
	}

	public boolean isGlobalEquilibrium() {
		return globalEquilibrium;
	}

	public double[][] getMatrix() {
		return matrix;
	}

}
