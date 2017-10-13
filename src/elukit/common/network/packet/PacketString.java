package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import elukit.client.Main;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.server.Server;

public class PacketString extends Packet {
	public String s = "";
	
	public PacketString(){}
	
	public PacketString(String s){
		this.s = s;
	}
	
	@Override
	public byte[] getData() {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void readData(byte[] data) {
		s = new String(data,StandardCharsets.UTF_8);
	}

	@Override
	public void onReceive(Side side, DatagramPacket port) {
		if (side == Side.SERVER){
			Server.console.addText("Received packet: " + s);
		}
		if (side == Side.CLIENT){
			Main.console.addText("Received packet: " + s);
		}
	}

}
