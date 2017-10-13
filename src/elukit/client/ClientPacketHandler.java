package elukit.client;

import java.net.DatagramPacket;

import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;

public class ClientPacketHandler implements Runnable {
	Thread t = null;
	
	@Override
	public void run() {
		while(true){
			//if (Main.socket != null){
				byte[] buffer = new byte[65536];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				NetworkHelper.receivePacket(Main.socket,packet,Side.CLIENT);
			//}
		}
	}
	
	public void start(){
		if (t == null){
			t = new Thread(this, "clientPacketHandler");
			t.start();
		}
	}

}
