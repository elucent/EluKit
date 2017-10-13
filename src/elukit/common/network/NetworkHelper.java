package elukit.common.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import elukit.common.network.packet.Packet;

public class NetworkHelper {
	public static DatagramSocket getSocket(){
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return datagramSocket;
	}
	
	public static InetAddress localhost(){
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InetAddress getNamedAddr(String addr){
		try {
			return InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InetAddress getRawAddr(byte[] addr){
		try {
			return InetAddress.getByAddress(addr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DatagramSocket getSocket(int port){
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return datagramSocket;
	}
	
	public static DatagramSocket getSocket(int port, InetAddress addr){
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(port,addr);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return datagramSocket;
	}
	
	public static void receivePacket(DatagramSocket sock, DatagramPacket packet, Side side){
		try {
			if (sock != null){
				sock.receive(packet);
				Packet.read(packet.getData(), side, packet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPacket(DatagramSocket sock, InetAddress addr, int port, Packet pack){
		byte[] data = pack.write();
		DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
		try {
			sock.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
