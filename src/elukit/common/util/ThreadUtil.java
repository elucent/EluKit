package elukit.common.util;

public class ThreadUtil {
	public static void wait(Thread t){
		try {
			t.wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void interrupt(Thread t){
		t.interrupt();
	}
}
