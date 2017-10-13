package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import elukit.client.Main;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.common.util.DataHelper;
import elukit.server.Server;

public class SPacketClientConnect extends Packet {
	float x, y, z;
	
	public SPacketClientConnect(){}
	
	public SPacketClientConnect(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public byte[] getData() {
		return DataHelper.serialize(x,y,z);
	}

	@Override
	public void readData(byte[] data) {
		List<Object> objs = DataHelper.deserialize(data);
		x = (float)objs.get(0);
		y = (float)objs.get(1);
		z = (float)objs.get(2);
	}

	@Override
	public void onReceive(Side side, DatagramPacket port) {
		if (side == Side.CLIENT){
			Main.connectedMP = true;
			Main.console.addText("Connected to server!");
			Main.player.x = x;
			Main.player.y = y;
			Main.player.z = z;
			Main.world.clearChunks();
			Main.address = new InetSocketAddress(port.getAddress(),port.getPort());
		}
	}

}
