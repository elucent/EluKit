package elukit.common.network.packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.server.Server;

public class CPacketConnectRequest extends Packet {
	public CPacketConnectRequest(){}
	
	@Override
	public byte[] getData() {
		return NO_DATA;
	}

	@Override
	public void readData(byte[] data) {
	}

	@Override
	public void onReceive(Side side, DatagramPacket raw) {
		if (side == Side.SERVER){
			InetSocketAddress sockaddr = new InetSocketAddress(raw.getAddress(),raw.getPort());
			if (!Server.connections.contains(sockaddr)){
				Server.console.addText("Received connection from client " + sockaddr.toString() + "!");
				Server.connections.add(sockaddr);
				NetworkHelper.sendPacket(Server.socket, raw.getAddress(), raw.getPort(), new SPacketClientConnect(Server.globalSpawn.x,Server.globalSpawn.y,Server.globalSpawn.z));
			}
		}
	}

}
