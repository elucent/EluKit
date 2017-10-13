package elukit.common.element;

import java.util.HashMap;
import java.util.Map;

public class ElementRegistry {
	public static Map<Integer, Element> tiles = new HashMap<Integer, Element>();
	static int id = 0;
	public static void init(){
		registerTile(new ElementSimpleCube(id++, 0, 0));
		registerTile(new ElementBillboard(id++, 16, 0));
		registerTile(new ElementPlinth(id++, 16, 0));
	}
	
	public static void registerTile(Element tile){
		tiles.put(tile.id, tile);
	}
	
	public static Element getTile(int id){
		return tiles.get(id);
	}
}
