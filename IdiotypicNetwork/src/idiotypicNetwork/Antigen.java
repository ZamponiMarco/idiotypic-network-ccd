package idiotypicNetwork;

import repast.simphony.space.grid.Grid;

public class Antigen implements Molecula{

	int type;
	public Grid<Antibody> grid;
	
	
	public Antigen (int type, Grid<Antibody> grid) {
		
		this.grid= grid;
		this.type = type;
		
	}
	
}
