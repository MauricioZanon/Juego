package RNG;

public abstract class Noise {
	
	public static void main(String[] args) {
		
		print(generatePerlinNoise(50, 250, 3));
		
	}
	
	public static void print(float[][] noise) {
		for(int j = 0; j < noise[0].length; j++){
			for(int i = 0; i < noise.length; i++){
				int h = (int) (noise[i][j]*10);
				System.out.print(h);
			}
			System.out.println();
		}
	}
	
	private static float[][] generateWhiteNoise(int width, int height)	{
	    float[][] noise = new float[width][height];
	 
	    for (int i = 0; i < width; i++){
	        for (int j = 0; j < height; j++){
	            noise[i][j] = (float)RNG.nextDouble() % 1;
	        }
	    }
	 
	    return noise;
	}
	
	private static float[][] generateSmoothNoise(float[][] baseNoise, int octave){
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][] smoothNoise = new float[width][height];
	 
	   int samplePeriod = (int) Math.pow(2, octave);
	   float sampleFrequency = 1.0f / samplePeriod;
	 
	   for (int i = 0; i < width; i++) {
	      int sample_i0 = (i / samplePeriod) * samplePeriod;
	      int sample_i1 = (sample_i0 + samplePeriod) % width;
	      float horizontal_blend = (i - sample_i0) * sampleFrequency;
	 
	      for (int j = 0; j < height; j++) {
	         int sample_j0 = (j / samplePeriod) * samplePeriod;
	         int sample_j1 = (sample_j0 + samplePeriod) % height;
	         float vertical_blend = (j - sample_j0) * sampleFrequency;
	 
	         float top = interpolate(baseNoise[sample_i0][sample_j0],
	            baseNoise[sample_i1][sample_j0], horizontal_blend);
	 
	         float bottom = interpolate(baseNoise[sample_i0][sample_j1],
	            baseNoise[sample_i1][sample_j1], horizontal_blend);
	 
	         //final blend
	         smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
	      }
	   }
	 
	   return smoothNoise;
	}
	
	private static float interpolate(float x0, float x1, float alpha)	{
	   return x0 * (1 - alpha) + alpha * x1;
	}
	
	/**
	 * 
	 * @param width: Ancho del array
	 * @param height: Alto del array
	 * @param octaveCount: Mientras mas alto sea este valor menor es el cambio de los valores entre celdas (con 5 queda bien)
	 * @return
	 */
	public static float[][] generatePerlinNoise(int width, int height, int octaveCount){
		
		float[][] baseNoise = generateWhiteNoise(width, height);
		float[][][] smoothNoise = new float[octaveCount][width][height];
		 
		float persistance = 0.5f;
		 
		for (int i = 0; i < octaveCount; i++) {
		    smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}
		 
		 float[][] perlinNoise = new float[width][height];
		 float amplitude = 1.0f;
		 float totalAmplitude = 0.0f;
		 
		 for (int octave = octaveCount - 1; octave >= 0; octave--) {
		    amplitude *= persistance;
		    totalAmplitude += amplitude;
		 
		    for (int i = 0; i < width; i++) {
		       for (int j = 0; j < height; j++) {
		          perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
		       }
		    }
		 }
		 
		for (int i = 0; i < width; i++) {
		   for (int j = 0; j < height; j++) {
		      perlinNoise[i][j] /= totalAmplitude;
		   }
		}
		 
		return perlinNoise;
	}

}
