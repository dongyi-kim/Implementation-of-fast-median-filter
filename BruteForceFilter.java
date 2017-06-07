/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class BruteforceFilter extends MedianFilter {

    public BruteforceFilter(int kernelSize) {
        super(kernelSize);
    }

    @Override
    public int[][] filtering(int[][] m) {
        int[][] X = getEmptyOutputMatrix(m);
        int r = mKernelSize / 2;
        int h = m.length;
        int w = m[0].length;
        int medianIndex = mKernelSize * mKernelSize / 2;
        for(int i = r; i + r < h ; i++ ){
            for(int j = r ; j + r < w ; j ++){
                int[] cnt = new int[256];

                for(int y = i - r ; y <= i + r ; y ++){
                    for(int x = j - r; x <= j + r ; x ++){
                        cnt[ m[y][x] ] ++;
                    }
                }
                int before = 0;
                for(int color = 0 ; color <= 255; color ++){
                    before += cnt[color];
                    if(before >= medianIndex){
                        X[i-r][j-r] = color;
                        break;
                    }
                }

            }
        }
        return X;
    }
}
