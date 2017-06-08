import javax.print.attribute.standard.MediaName;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class TestClass {

    public static final int IMAGE_WIDTH = 512;
    public static final int IMAGE_HEIGHT = 512;
    public static final String FILE_PATH = "output.csv";

    public static void main(String[] args) throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
        writer.write("Kernel Radius, Random Boxes, Random Circles, Fully Random\n");
        for(int kernelRadius = 0; kernelRadius <= 120; kernelRadius += 10)
        {
            int kernelSize = kernelRadius * 2 + 1;
            MedianFilter filter = new ProposedOptimizedV2Filter(kernelSize);
            int tn = 30;

            writer.write(String.format("%d", kernelRadius));

            {
                double timeCost = 0;
                for(int i = 0 ; i < tn; i++){
                    int[][] m = UtilClass.getRandomBoxesImage(IMAGE_HEIGHT, IMAGE_WIDTH);
                    double t = test(m, filter);
                    timeCost += t;
                }
                writer.write(String.format(",%.10f", timeCost/tn));
            }

            {
                double timeCost = 0;
                for(int i = 0 ; i < tn; i++){
                    int[][] m = UtilClass.getRandomCirclesImage(IMAGE_HEIGHT, IMAGE_WIDTH);
                    double t = test(m, filter);
                    timeCost += t ;
                }
                writer.write(String.format(",%.10f", timeCost/tn));
            }

            {
                double timeCost = 0;
                for(int i = 0 ; i < tn; i++){
                    int[][] m = UtilClass.getFullyRandomImage(IMAGE_HEIGHT, IMAGE_WIDTH);
                    double t = test(m, filter);
                    timeCost += t;
                }
                writer.write(String.format(",%.10f", timeCost/tn));
            }
            writer.write("\n");
            writer.flush();
        }
        writer.close();
    }

    public static double test(int[][] m, MedianFilter filter)
    {
        double timeCost = 0;
        int tn = 3;
        for(int i = 0 ; i < tn; i++){
            System.gc();
            long st = System.currentTimeMillis();
            filter.filtering(m);
            long et = System.currentTimeMillis();
            timeCost += (double)(et-st)/(1000.);
        }
        return timeCost/tn;
    }

}
