package idiotypicNetwork;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.help.plaf.basic.BasicFavoritesNavigatorUI.RemoveAction;

import com.l2fprod.common.beans.editor.BooleanAsCheckBoxPropertyEditor;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;
import repast.simphony.util.ContextUtils;

public class ImmuneSystem {

	public boolean globalEquilibrium;
	public double[][] matrix;
	public boolean isInfected;
	public int maxEquilibriumStateLength;

	public ImmuneSystem(double[][] matrix, int maxEquilibriumStateLength) {
		this.globalEquilibrium = false;
		this.matrix = matrix;
		this.isInfected = false;
		this.maxEquilibriumStateLength = maxEquilibriumStateLength;
	}

	@ScheduledMethod(start = 1, interval = 2, priority = 2)
	public void checkEquilibrium() {
		Context<Antibody> context = ContextUtils.getContext(this);
		this.globalEquilibrium = context.getObjectsAsStream(Antibody.class).allMatch(agent -> agent.eq.isEquilibrium());
		if (this.globalEquilibrium && this.isInfected) {
			this.removeAntigen();
			this.isInfected = false;
		}

	}

	@Watch(watcheeClassName = "idiotypicNetwork.Antigen", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void reactToAntigen() {
		this.getAntibodies().forEach(antibody -> antibody.eq.reset());
		this.globalEquilibrium = false;
		this.isInfected = true;

		Antigen antigen = this.getAntigen();
		if (antigen != null && antigen.type < this.matrix.length) {
			Context<Antibody> context = ContextUtils.getContext(this);

			this.addNewAntibodyToMatrix();
			context.add(new Antibody(antigen.type, this.maxEquilibriumStateLength));
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

}
