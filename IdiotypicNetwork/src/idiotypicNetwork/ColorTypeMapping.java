package idiotypicNetwork;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorTypeMapping {
	
	private static ColorTypeMapping instance;
	
	private Map<Integer, Color> map;
	
	
	public ColorTypeMapping() {
		instance = this;
		this.map = new HashMap<Integer, Color>();
		
	}
	
	public static ColorTypeMapping getInstance() {
		if(instance == null) new ColorTypeMapping();
		return instance;

	}
	
	public Color getColor(int type) {
		if (!map.containsKey(type)) {
			
			Random rand = new Random();
			float r = (float) (rand.nextFloat() / 2f + 0.5);
			float g = (float) (rand.nextFloat() / 2f + 0.5);
			float b = (float) (rand.nextFloat() / 2f + 0.5);
			
			map.put(type, new Color(r,g,b));
		}
		
		return map.get(type);
	}
}
