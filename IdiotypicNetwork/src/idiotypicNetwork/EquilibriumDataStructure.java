package idiotypicNetwork;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class EquilibriumDataStructure {
	
	String lastStateString;
	int maxLength;
	boolean equilibrium;
	
	private Predicate<String> equilibriumPredicate;

	public EquilibriumDataStructure(int maxLength) {

		this.lastStateString = "";
		this.maxLength = maxLength;

		this.equilibrium = false;

		this.equilibriumPredicate = Pattern.compile(String.format("A{%d}|D{%d}|(AD){%d}%s|(DA){%d}%s", maxLength, maxLength, 
				maxLength / 2, maxLength % 2 == 0 ? "" : "A", maxLength / 2, maxLength % 2 == 0 ? "" : "D")).asMatchPredicate();
	}

	public void addState(String state) {
		lastStateString = lastStateString.concat(state);

		if (lastStateString.length() > maxLength) {
			lastStateString = lastStateString.substring(lastStateString.length() - maxLength, lastStateString.length());
		}
	}

	public boolean isEquilibrium() {
		this.equilibrium = equilibriumPredicate.test(lastStateString);
		return this.equilibrium;

	}

	public void reset() {
		this.lastStateString = "";
	}
}
