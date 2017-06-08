/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class TestClass {



    public static void main(String[] args){


        int tn = 10;
        for(int r = 0; r <= 120; r+=10){
            int kernelSize = r * 2 + 1;
            MedianFilter[] filters = new MedianFilter[]{ new HuangsFilter(kernelSize), new ProposedFilter(kernelSize)};
            double[] cost = new double[filters.length];

            for(int t = 0 ; t < tn; t++){
                int[][] m = UtilClass.getRandomBoxesImage(1024,1024);
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
