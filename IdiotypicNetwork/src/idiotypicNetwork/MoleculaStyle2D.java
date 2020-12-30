package idiotypicNetwork;

import java.awt.Color;
import java.awt.Shape;

import org.jmock.expectation.ReturnObjectBag;

import com.bulletphysics.collision.dispatch.CollisionObjectType;

import jogamp.graph.font.UbuntuFontLoader;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

public class MoleculaStyle2D extends DefaultStyleOGL2D {

	
	@Override
	public Color getColor(Object agent) {

		
		if (agent instanceof DeadAntibody) {
			return Color.WHITE;
		} else if (agent instanceof AliveAntibody) {
			return Color.BLACK;
		} 
		
		return super.getColor(agent);
		
	}
	
	 /**
	   * @return a circle of radius 4.
	   */
	  public VSpatial getVSpatial(Object agent, VSpatial spatial) {
	    if (spatial == null) {
	      
	    	  	spatial = shapeFactory.createCircle(8, 16);
	     
	      }
	    
	    return spatial;
	  }

}
