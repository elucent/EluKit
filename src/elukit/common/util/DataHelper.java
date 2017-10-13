package elukit.common.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Sets;

import elukit.client.Main;

public class DataHelper {
	public static byte[] TERMINATION = getBytes("TER".hashCode(),3);
	public static byte T_INT = "I".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_FLOAT = "F".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_SHORT = "S".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_DOUBLE = "D".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_STRING = "L".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_BYTES = "A".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_BOOL = "B".getBytes(StandardCharsets.UTF_8)[0];
	public static byte T_NULL = "N".getBytes(StandardCharsets.UTF_8)[0];
	public static Set<Byte> types = new HashSet<>();
	public static Map<Byte, Function<Object, byte[]>> serializers = new HashMap<>();
	public static Map<Byte, Function<byte[], Object>> deserializers = new HashMap<>();
	
	static {
		serializers.put(T_INT, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = getBytes((int)arg0);
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_INT;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_INT, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return getInt(prod);
			}
		});
		serializers.put(T_FLOAT, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = getBytes((float)arg0);
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_FLOAT;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_FLOAT, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return getFloat(prod);
			}
		});
		serializers.put(T_DOUBLE, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = getBytes((double)arg0);
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_DOUBLE;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_DOUBLE, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return getDouble(prod);
			}
		});
		serializers.put(T_SHORT, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = getBytes((short)arg0);
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_SHORT;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_SHORT, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return getShort(prod);
			}
		});
		serializers.put(T_STRING, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = getBytes((String)arg0);
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_STRING;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_STRING, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return getString(prod);
			}
		});
		serializers.put(T_BYTES, new Function<Object, byte[]>(){
			@Override
			public byte[] apply(Object arg0) {
				byte[] raw = (byte[])arg0;
				byte[] prod = new byte[raw.length+4];
				prod[0] = T_BYTES;
				System.arraycopy(raw, 0, prod, 1, raw.length);
				System.arraycopy(TERMINATION, 0, prod, prod.length-3, 3);
				return prod;
			}
		});
		deserializers.put(T_BYTES, new Function<byte[], Object>(){
			@Override
			public Object apply(byte[] raw) {
				byte[] prod = new byte[raw.length-4];
				System.arraycopy(raw, 1, prod, 0, prod.length);
				return prod;
			}
		});
	}
	
	public static byte[] getBytes(String s){
		return s.getBytes(StandardCharsets.UTF_8);
	}
	
	public static String getString(byte[] bytes){
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	public static byte[] getBytes(int i){
		return ByteBuffer.allocate(Integer.SIZE/8).putInt(i).array();
	}
	
	public static byte[] getBytes(int i, int count){
		byte[] raw = ByteBuffer.allocate(Integer.SIZE/8).putInt(i).array();
		byte[] trimmed = new byte[count];
		System.arraycopy(raw, raw.length-count, trimmed, 0, count);
		return trimmed;
	}
	
	public static int getInt(byte... bytes){
		byte[] padded = new byte[Integer.SIZE/8];
		System.arraycopy(bytes, 0, padded, Integer.SIZE/8-bytes.length, bytes.length);
		return ByteBuffer.wrap(padded).getInt();
	}

	public static byte[] getBytes(float i){
		return ByteBuffer.allocate(Float.SIZE/8).putFloat(i).array();
	}
	
	public static byte[] getBytes(float i, int count){
		byte[] raw = ByteBuffer.allocate(Float.SIZE/8).putFloat(i).array();
		byte[] trimmed = new byte[count];
		System.arraycopy(raw, raw.length-count, trimmed, 0, count);
		return trimmed;
	}
	
	public static float getFloat(byte... bytes){
		byte[] padded = new byte[Float.SIZE/8];
		System.arraycopy(bytes, 0, padded, Float.SIZE/8-bytes.length, bytes.length);
		return ByteBuffer.wrap(padded).getFloat();
	}

	public static byte[] getBytes(short i){
		return ByteBuffer.allocate(Short.SIZE/8).putShort(i).array();
	}
	
	public static byte[] getBytes(short i, int count){
		byte[] raw = ByteBuffer.allocate(Short.SIZE/8).putShort(i).array();
		byte[] trimmed = new byte[count];
		System.arraycopy(raw, raw.length-count, trimmed, 0, count);
		return trimmed;
	}
	
	public static short getShort(byte... bytes){
		byte[] padded = new byte[Short.SIZE/8];
		System.arraycopy(bytes, 0, padded, Short.SIZE/8-bytes.length, bytes.length);
		return ByteBuffer.wrap(padded).getShort();
	}

	public static byte[] getBytes(double i){
		return ByteBuffer.allocate(Double.SIZE/8).putDouble(i).array();
	}
	
	public static byte[] getBytes(double i, int count){
		byte[] raw = ByteBuffer.allocate(Double.SIZE/8).putDouble(i).array();
		byte[] trimmed = new byte[count];
		System.arraycopy(raw, raw.length-count, trimmed, 0, count);
		return trimmed;
	}
	
	public static double getDouble(byte... bytes){
		byte[] padded = new byte[Double.SIZE];
		System.arraycopy(bytes, 0, padded, Double.SIZE-bytes.length, bytes.length);
		return ByteBuffer.wrap(padded).getDouble();
	}
	
	public static byte getTypeByte(Object o){
		if (o instanceof Integer){
			return T_INT;
		}
		else if (o instanceof Float){
			return T_FLOAT;
		}
		else if (o instanceof Double){
			return T_DOUBLE;
		}
		else if (o instanceof String){
			return T_STRING;
		}
		else if (o instanceof Short){
			return T_SHORT;
		}
		else if (o instanceof byte[]){
			return T_BYTES;
		}
		return T_NULL;
	}
	
	public static byte[] serialize(Object... objects){
		List<byte[]> data = new ArrayList<>();
		int count = 0;
		for (Object o : objects){
			byte type = getTypeByte(o);
			if (type != T_NULL){
				byte[] d = serializers.get(type).apply(o);
				data.add(d);
				count += d.length;
			}
		}
		byte[] prod = new byte[count];
		int counter = 0;
		for (int i = 0; i < data.size(); i ++){
			byte[] t = data.get(i);
			System.arraycopy(t, 0, prod, counter, t.length);
			counter += t.length;
		}
		return prod;
	}
	
	public static List<Object> deserialize(byte[] data){
		List<Object> objects = new ArrayList<>();
		List<Byte> bytes = new ArrayList<>();
		for (int i = 0; i < data.length; i ++){
			bytes.add(data[i]);
			if (bytes.size() >= 2 && bytes.get(bytes.size()-1) == TERMINATION[2] && bytes.get(bytes.size()-2) == TERMINATION[1] && bytes.get(bytes.size()-3) == TERMINATION[0]){
				byte[] d = new byte[bytes.size()];
				for (int j = 0; j < bytes.size() ; j ++){
					d[j] = bytes.get(j);
				}
				if (deserializers.containsKey(d[0])){
					objects.add(deserializers.get(d[0]).apply(d));
				}
				bytes.clear();
			}
		}
		return objects;
	}
}
