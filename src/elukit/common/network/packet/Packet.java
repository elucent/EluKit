package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import elukit.client.Main;
import elukit.common.network.Side;
import elukit.common.util.DataHelper;
import elukit.server.Server;

public abstract class Packet {
	static final byte STARTER = ":".getBytes(StandardCharsets.UTF_8)[0];
	static final byte[] NO_DATA = new byte[]{};
	
	public byte[] write(){
		byte[] dataBytes = getData();
		int dl = dataBytes.length;
		int len = dl + 5;
		byte[] bytes = new byte[len];
		int id = PacketRegistry.getId(this.getClass());
		byte[] idBytes = DataHelper.getBytes(id, 2);
		byte[] sizeBytes = DataHelper.getBytes(len, 2);
		bytes[0] = STARTER;
		System.arraycopy(idBytes, 0, bytes, 1, idBytes.length);
		System.arraycopy(sizeBytes, 0, bytes, 3, sizeBytes.length);
		System.arraycopy(dataBytes, 0, bytes, 5, dataBytes.length);
		return bytes;
	}
	
	public static void read(byte[] in, Side side, DatagramPacket packet){
		if (in[0] == STARTER){
			int id = DataHelper.getInt(Arrays.copyOfRange(in, 1, 3));
			if (PacketRegistry.hasId(id)){
				int size = DataHelper.getInt(Arrays.copyOfRange(in, 3, 5));
				byte[] data = Arrays.copyOfRange(in, 5, size);
				Packet p = PacketRegistry.newPacket(id);
				p.readData(data);
				if (side == Side.CLIENT){
					Main.packetQueue.enqueue(p);
					Main.rawQueue.enqueue(packet);
				}
				else {
					Server.packetQueue.enqueue(p);
					Server.rawQueue.enqueue(packet);
				}
			}
		}
	}
	
	public abstract byte[] getData();
	
	public abstract void readData(byte[] data);
	
	public abstract void onReceive(Side side, DatagramPacket port);
}
