package elukit.common.network.packet;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PacketRegistry {
	static BiMap<Class<? extends Packet>, Integer> registry = HashBiMap.create();
	static int id = 0;
	
	public static void addPacket(Class<? extends Packet> c){
		registry.put(c, id++);
	}
	
	public static boolean hasId(int id){
		return registry.containsValue(id);
	}
	
	public static Packet newPacket(int id){
		try {
			return registry.containsValue(id) ? ((Class<? extends Packet>)registry.inverse().get(id)).newInstance() : null;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getId(Class<? extends Packet> c){
		return registry.containsKey(c) ? registry.get(c) : 0;
	}
	
	static {
		addPacket(CPacketConnectRequest.class);
		addPacket(SPacketClientConnect.class);
		addPacket(PacketString.class);
		addPacket(CPacketDisconnectRequest.class);
		addPacket(SPacketClientDisconnect.class);
		addPacket(CPacketChunkRequest.class);
		addPacket(SPacketChunkData.class);
	}
}
