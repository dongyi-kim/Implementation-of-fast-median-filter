import java.util.Random;

/**
 * Created by dongyi.kim on 2017-06-08.
 */
public class UtilClass {
    public static int[][] getRandomBoxesImage(int height, int width){
        int[][] m = new int[height][width];
        Random random = new Random();
        for(int i = 0 ; i < 10000 ; i++){
            int left = random.nextInt(width);
            int bottom = random.nextInt(height);
            int top = bottom + random.nextInt( Math.min(height - bottom, 100));
            int right = left + random.nextInt( Math.min(100,width - left));
            for(int x = left; x<=right; x++){
                int color = Math.abs(random.nextInt())%256;
                for(int y = bottom; y <= top; y++){
                    m[y][x] = color;
                }
            }
        }
        return addGaussianNoise(addSaltAndPepperNoise(m));
    }

    public static int[][] getRandomCirclesImage(int height, int width)
    {
        int[][] m = new int[height][width];
        Random random = new Random();
        for(int i = 0 ; i < 10000 ; i++){
            int cx = random.nextInt(width);
            int cy = random.nextInt(height);
            int r = random.nextInt(Math.min(width,height)/2);
            int left = Math.max(0, cx-r);
            int right = Math.min(width-1, cx + r);
            int bottom = Math.max(0, cy-r);
            int top = Math.min(height-1, cy+r);
            int color = random.nextInt(256);

            for(int y = bottom; y <= top; y++){
                for(int x = left; x <=right; x++){
                    int d = (x-cx)*(x-cx) + (y-cy)*(y-cy);
                    if( d > r * r ) continue;
                    m[y][x] = color;
                }
            }
        }
        return addGaussianNoise(addSaltAndPepperNoise(m));
    }


    public static int[][] getFullyRandomImage(int height, int width){
        int[][] m = new int[height][width];
        Random random = new Random();
        for(int y = 0 ; y < height; y++){
            for(int x = 0 ;x < width; x++){
                m[y][x] = random.nextInt(256);
            }
        }
        return m;
    }

    public static int[][] addGaussianNoise(int[][] m){
        int nNoise = 100;
        int h = m.length;
        int w = m[0].length;
        Random random = new Random();
        for(int i = 0; i < nNoise; i++){
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            m[y][x] += random.nextInt()%20;
            m[y][x] = Math.max(m[y][x], 0);
            m[y][x] = Math.min(m[y][x], 255);
        }
        return m;
    }

    public static int[][] addSaltAndPepperNoise(int[][] m){
        int nNoise = 100;
        int h = m.length;
        int w = m[0].length;
        Random random = new Random();
        for(int i = 0; i < nNoise; i++){
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            m[y][x] = random.nextInt(2) * 255;
        }
        return m;
    }
}
