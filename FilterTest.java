import java.util.Random;

/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class FilterTest {

    public static int[][] getRandomImage(int height, int width){
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

        for(int i = 0 ; i < height * width ; i++){
            int x = random.nextInt(width-1);
            int y = random.nextInt(height-1);
            m[y][x] += + random.nextInt()%2;
            m[y][x] = Math.max(0, m[y][x]);
            m[y][x] = Math.min(255, m[y][x]);

        }
        return m;
    }

    public static void main(String[] args){


        int tn = 10;
        for(int r = 0; r <= 120; r+=10){
            int kernelSize = r * 2 + 1;
            MedianFilter[] filters = new MedianFilter[]{ new HuangsFilter(kernelSize), new ProposedFilter(kernelSize)};
            double[] cost = new double[filters.length];

            for(int t = 0 ; t < tn; t++){
                int[][] m = getRandomImage(1024,1024);
                long[] beginTimes = new long[filters.length];
                long[] endTimes = new long[filters.length];

                for(int i = 0 ; i < filters.length; i++) {
                    System.gc();
                    beginTimes[i] = System.currentTimeMillis();
                    filters[i].filtering(m);
                    endTimes[i] = System.currentTimeMillis();
                }

                for(int i = 0 ; i < filters.length; i++){
                    double tc = (endTimes[i] - beginTimes[i]) / 1000.f;
                    cost[i] += tc;
                }
            }

            for(int i = 0 ; i < filters.length; i++){
                cost[i] /= (double)tn;
                System.out.printf("%.5f ", cost[i]);
            }
            System.out.println();


        }

//        for(int i = 0 ; i < cost.length; i++){

//        }

    }

}
