package RNG;


public class NoiseB {

    private float roughness;
    
    float menor = 0;
    float mayor = 0;

    private float[][] grid;

    public NoiseB(float roughness, int width, int height) {
        this.roughness = roughness / width;
        grid = new float[width][height];
    }


    public void initialise() {
        int xh = grid.length - 1;
        int yh = grid[0].length - 1;

        grid[0][0] = RNG.nextFloat() - 0.5f;
        grid[0][yh] = RNG.nextFloat() - 0.5f;
        grid[xh][0] = RNG.nextFloat() - 0.5f;
        grid[xh][yh] = RNG.nextFloat() - 0.5f;

        generate(0, 0, xh, yh);
    }


    private float roughen(float v, int l, int h) {
    	float r = v + roughness * (float) (RNG.nextGaussian() * (h - l));
    	if(r < menor) menor = r;
    	else if(r > mayor) mayor = r;
        return r;
    }


    private void generate(int xl, int yl, int xh, int yh) {
        int xm = (xl + xh) / 2;
        int ym = (yl + yh) / 2;
        if ((xl == xm) && (yl == ym)) return;

        grid[xm][yl] = 0.5f * (grid[xl][yl] + grid[xh][yl]);
        grid[xm][yh] = 0.5f * (grid[xl][yh] + grid[xh][yh]);
        grid[xl][ym] = 0.5f * (grid[xl][yl] + grid[xl][yh]);
        grid[xh][ym] = 0.5f * (grid[xh][yl] + grid[xh][yh]);

        float v = roughen(0.5f * (grid[xm][yl] + grid[xm][yh]), xl + yl, yh
                + xh);
        grid[xm][ym] = v;
        grid[xm][yl] = roughen(grid[xm][yl], xl, xh);
        grid[xm][yh] = roughen(grid[xm][yh], xl, xh);
        grid[xl][ym] = roughen(grid[xl][ym], yl, yh);
        grid[xh][ym] = roughen(grid[xh][ym], yl, yh);

        generate(xl, yl, xm, ym);
        generate(xm, yl, xh, ym);
        generate(xl, ym, xm, yh);
        generate(xm, ym, xh, yh);
    }


    public void printAsCSV() {
    	float ht = Math.abs(mayor - menor);
    	float prom = 0;
        for(int i = 0;i < grid.length;i++) {
            for(int j = 0;j < grid[0].length;j++) {
            	float h = Math.abs(grid[i][j] + Math.abs(menor) );
            	prom += h;
            	if(h < ht*0.1)
            		System.out.print("~");
            	else if(h > ht*0.6 && h < ht*0.7)
            		System.out.print("*");
            	else if(h < ht*0.6)
            		System.out.print(".");
            	else 
            		System.out.print("^");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("altura promedio " + (prom/(grid.length*grid[0].length)));
        System.out.println("altura total " + ht);
        System.out.println("menor " + menor);
        System.out.println("mayor " + mayor);
    }

    public boolean[][] toBooleans() {
        int w = grid.length;
        int h = grid[0].length;
        boolean[][] ret = new boolean[w][h];
        for(int i = 0;i < w;i++) {
            for(int j = 0;j < h;j++) {
                ret[i][j] = grid[i][j] < 0;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        NoiseB n = new NoiseB(1f, 50, 250);
        n.initialise();
        n.printAsCSV();
    }
}