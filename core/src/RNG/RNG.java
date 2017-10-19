package RNG;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public abstract class RNG {
	
	private static Random rng = new Random();
	
	/**========================================================================
	 * ============================COLLECTIONS=================================
	 * ========================================================================*/
	
	public static <T> T getRandom(T[] array){
		return array[nextInt(array.length)];
	}

	public static <T> T getRandom(T[][] array){
		return array[nextInt(array.length)][nextInt(array[0].length)];
	}
	
	public static <T> T getRandom(T[][] array, Predicate<T> cond){
		while(true){
			T t = getRandom(array);
			if(cond.test(t)) return t;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRandom(Collection<T> collection){
		if(collection.isEmpty()) return null;
		T[] array = (T[]) collection.toArray();
		return array[nextInt(array.length)];
	}
	
	public static <T> T getRandom(Collection<T> collection, Predicate<T> cond){
		if(collection.isEmpty()) return null;
		while(true){
			T t = getRandom(collection);
			if(cond.test(t)){
				return t;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRandom(Set<T> set){
		if(set.isEmpty()) return null;
		T[] array = (T[]) set.toArray();
		return array[nextInt(array.length)];
	}

	public static <T> T getRandom(Set<T> set, Predicate<T> cond){
		if(set.isEmpty()) return null;
		while(true){
			T t = getRandom(set);
			if(cond.test(t)){
				return t;
			}
		}
	}
	
	public static <T> T getRandom(List<T> list){
		if(list.isEmpty()) return null;
		return list.get(nextInt(list.size()));
	}
	
	public static <T> T getRandom(List<T> list, Predicate<T> cond){
		if(list.isEmpty()) return null;
		while(true){
			T t = getRandom(list);
			if(cond.test(t)) return t;
		}
	}
	
	
	/**========================================================================
	 * ============================COLORS======================================
	 * ========================================================================*/
	
	public static Color getApproximateColor(Color color){
		Color newColor = color;
		if(rng.nextBoolean())
			newColor = getAproximateClarity(newColor);
		if(rng.nextBoolean())
			newColor = getAproximateHue(newColor);
		
		return newColor;
	}
	
	public static Color getAproximateClarity(Color color){
		float delta = MathUtils.clamp(rng.nextFloat() / 20f, 0f, 0.025f);
		
		float deltaR = color.r * delta;
		float deltaG = color.g * delta;
		float deltaB = color.b * delta;
		
		return rng.nextBoolean() ? new Color(color).sub(deltaR, deltaG, deltaB, 1f) : new Color(color).add(deltaR, deltaG, deltaB, 1f);
	}
	
	public static Color getAproximateHue(Color color){
		int delta = 30; // Mientras mas bajo mas se nota el cambio
		
		int r = (int) (color.r * 255);
		int deltaR = r / delta;
		r = nextGaussian(r, deltaR);
		
		int g = (int) (color.g * 255);
		int deltaG = g / delta;
		g = nextGaussian(g, deltaG);
		
		int b = (int) (color.b * 255);
		int deltaB = b / delta;
		b = nextGaussian(b, deltaB);
		
		return new Color(r/255f, g/255f, b/255f, 1f);
	}
	
	/**========================================================================
	 * ============================NUMBERS=====================================
	 * ========================================================================*/
	
	/**
	 * @param limit: Número máximo (no incluído)
	 * @return
	 */
	public static int nextInt(int limit) {
		return rng.nextInt(limit);
	}
	
	/**
	 * @param min: Mínimo número deseado
	 * @param max: Máximo número deseado (no incluído)
	 * @return
	 */
	public static int nextInt(int min, int max){
		int range = max - min;
		int number = rng.nextInt(range);
		return number + min;
	}
	
	public static float nextFloat(){
		return rng.nextFloat();
	}
	
	//TODO: Sacar cuando se elimine la clase Noise
	public static double nextDouble(){
		return rng.nextDouble();
	}
	
	public static double nextGaussian(){
		return rng.nextGaussian();
	}
	
	/**
	 * 
	 * @param mean: valor base
	 * @param variation: variación máxima desde el valor base
	 * @return un entero entre (mean - variation) y (mean + variation) que tiende a quedarse cerca del valor base
	 */
	public static int nextGaussian(int mean, int variation){
		float result = (float) (rng.nextGaussian() * variation + mean);
		if(result < (mean - variation) || result > (mean + variation))
			return nextGaussian(mean, variation);
		else
			return Math.round(result);
	}
	
	public static boolean getBoolean(){
		return rng.nextBoolean();
	}
}
