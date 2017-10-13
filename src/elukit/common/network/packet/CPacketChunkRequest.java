package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.common.struct.Vec2i;
import elukit.common.util.DataHelper;
import elukit.server.Server;

public class CPacketChunkRequest extends Packet {
	int x = 0;
	int z = 0;
	public CPacketChunkRequest(){}

	public CPacketChunkRequest(int x, int z){
		this.x = x;
		this.z = z;
	}
	
	@Override
	public byte[] getData() {
		return DataHelper.serialize(x, z);
	}

	@Override
	public void readData(byte[] data) {
		List<Object> objs = DataHelper.deserialize(data);
		this.x = (int)objs.get(0);
		this.z = (int)objs.get(1);
	}

	@Override
	public void onReceive(Side side, DatagramPacket raw) {
		if (side == Side.SERVER){
			Server.world.genChunk(x, z);
			NetworkHelper.sendPacket(Server.socket, raw.getAddress(), raw.getPort(), new SPacketChunkData(Server.world.chunks.get(new Vec2i(x,z))));
		}
	}

}
