/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class HuangsFilter extends MedianFilter {

    public HuangsFilter(int kernelSize) {
        super(kernelSize);
    }

    @Override
    public int[][] filtering(int[][] m) {
        int[][] X = getEmptyOutputMatrix(m);
        int h = m.length;
        int w = m[0].length;
        int r = mKernelSize/2;
        int medianIndex = mKernelSize * mKernelSize / 2;

        for(int ic=r; ic+r < h; ic++){
            int[] cnt = new int[256];
            for(int j = 0 ; j < w ; j++){
                for(int i = ic - r  ; i <= ic + r; i ++){
                    int color = m[i][j];
                    cnt[color] ++;
                    if( j >= mKernelSize){
                        int colorToRemove = m[i][j-mKernelSize];
                        cnt[colorToRemove] --;
                    }
                }

                if( j >= mKernelSize -1  ){
                    int x = j - mKernelSize + 1;
                    int y = ic - r;
                    int before = 0;
                    for(int cw = 0; cw <= 255; cw++){
                        before += cnt[cw];
                        if(before >= medianIndex){
                            X[y][x] = cw;
                            break;
                        }
                    }
                }

            }
        }
        return X;
    }
}
