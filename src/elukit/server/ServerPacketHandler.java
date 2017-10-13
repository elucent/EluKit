package elukit.server;

import java.net.DatagramPacket;

import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;

public class ServerPacketHandler implements Runnable {
	Thread t = null;
	
	@Override
	public void run() {
		while(Server.open){
			byte[] buffer = new byte[65536];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			NetworkHelper.receivePacket(Server.socket,packet,Side.SERVER);
		}
	}
	
	public void start(){
		if (t == null){
			t = new Thread(this, "serverPacketHandler");
			t.start();
		}
	}

}
