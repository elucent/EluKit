package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import elukit.client.Main;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.server.Server;

public class SPacketClientDisconnect extends Packet {
	public SPacketClientDisconnect(){}
	
	@Override
	public byte[] getData() {
		return NO_DATA;
	}

	@Override
	public void readData(byte[] data) {
	}

	@Override
	public void onReceive(Side side, DatagramPacket port) {
		if (side == Side.CLIENT){
			Main.connectedMP = false;
			Main.socket = null;
			Main.console.addText("Disconnected!");
		}
	}

}
