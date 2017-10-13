package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import elukit.client.Main;
import elukit.common.level.Chunk;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.common.util.DataHelper;
import elukit.server.Server;

public class SPacketChunkData extends Packet {
	Chunk c;
	
	public SPacketChunkData(){}
	
	public SPacketChunkData(Chunk c){
		this.c = c;
	}
	
	@Override
	public byte[] getData() {
		return DataHelper.serialize(c.worldX,c.worldZ,c.serialize());
	}

	@Override
	public void readData(byte[] data) {
		List<Object> objs = DataHelper.deserialize(data);
		c = new Chunk(Main.world,(int)objs.get(0),(int)objs.get(1));
		c.deserialize((byte[])objs.get(2));
	}

	@Override
	public void onReceive(Side side, DatagramPacket port) {
		if (side == Side.CLIENT){
			c.bake();
			Main.world.setChunk(c);
		}
	}

}
