package idiotypicNetwork;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import bsh.This;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;

public class EquilibriumDataStructure {

	String lastStateString;
	int maxLength;
	List<String> checkerList;
	boolean equilibrium;

	public EquilibriumDataStructure(int maxLength) {

		this.lastStateString = "";
		this.maxLength = maxLength;

		String s1 = "";
		String s2 = "";
		String s3 = "";

		for (int i = 0; i < maxLength; i++) {
			s1 = s1.concat("A");
			s2 = s2.concat("D");
			s3 = s3.concat("AD");
		}

		this.checkerList = new ArrayList<String>();
		this.checkerList.add(s1);
		this.checkerList.add(s2);
		this.checkerList.add(s3.substring(0, maxLength));
		this.checkerList.add(s3.substring(1, maxLength + 1));

		this.equilibrium = false;
	}

	public void addState(String state) {
		lastStateString = lastStateString.concat(state);

		if (lastStateString.length() > maxLength) {
			lastStateString = lastStateString.substring(lastStateString.length() - maxLength, lastStateString.length());
		}
	}

	public boolean isEquilibrium() {
		this.equilibrium = this.checkerList.stream().anyMatch(string -> string.equals(this.lastStateString));
		return this.equilibrium;
				

	}
	
	public void reset() {
		this.lastStateString="";
	}
}
