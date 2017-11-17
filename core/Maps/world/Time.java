package world;

import java.util.Calendar;

public abstract class Time {
	
	/**
	 * TODO: Hacer otra clase que maneje el clima y agregarle estaciones y estados climáticos
	 */
	
	private static Calendar calendar = Calendar.getInstance();
	
	private static float lightLevel = 1f;
	
	static {
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.MONTH, 0);
	}
	
	public static String getHour() {
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d:%02d", minutes, seconds);
	}
	
	public static void advanceTime(int seconds) {
		calendar.add(Calendar.SECOND, seconds);
		recalculateLightLevel();
	}
	
	private static void recalculateLightLevel() {
		float minutes = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
		if(minutes >= 300 && minutes < 480) {
			lightLevel = (float) (0.00635199f * Math.pow(1.01059529f, minutes));
		}else if(minutes >= 1080 && minutes < 1260) {
			lightLevel = (float) (2.14833262E+37f * Math.pow(minutes, -12.30691935f));
		}else if(minutes >= 1260 || minutes < 300) {
			lightLevel = 0.15f;
		}else {
			lightLevel = 1f;
		}
	}

	public static float getLightLevel() {
		return lightLevel;
	}
	
}
