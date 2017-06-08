/**
 * Created by dongyi.kim on 2017-06-08.
 */
public class ProposedOptimizedV2Filter extends MedianFilter {

    public ProposedOptimizedV2Filter(int kernelSize) {
        super(kernelSize);
    }



    @Override
    public int[][] filtering(int[][] m) {
        int[][] X = getEmptyOutputMatrix(m);
        int h = m.length;
        int w = m[0].length;
        int r = mKernelSize / 2;
        int medianIndex = mKernelSize * mKernelSize / 2;
        int[][] histogramCounter = new int[w][256];
        int[][] histogramSubCounter = new int[w][16];
        for(int i = 0; i < h; i++){
            int[] windowCounter = new int[256];
            int[] windowSubCounter = new int[16];
            for(int j = 0 ; j < w; j++){
                int color = m[i][j];
                int rowToRemove = i - mKernelSize;
                if(rowToRemove >= 0){
                    int colorToRemove = m[rowToRemove][j];
                    histogramCounter[j][colorToRemove] --;
                    histogramSubCounter[j][colorToRemove/16] --;
                }
                histogramCounter[j][color] ++;
                histogramSubCounter[j][color/16]++;

                int columnToRemove = j - mKernelSize;
                for(int csub = 0; csub <= 255; csub += 16){
                    if(histogramSubCounter[j][csub/16] == 0 && (columnToRemove < 0 || histogramSubCounter[columnToRemove][csub/16] == 0 ))
                        continue;

                    for(int ch = csub; ch < csub+16; ch++){
                        if(columnToRemove >= 0){
                            windowCounter[ch] -= histogramCounter[columnToRemove][ch];
                            windowSubCounter[ch/16] -= histogramCounter[columnToRemove][ch];
                        }
                        windowCounter[ch] += histogramCounter[j][ch];
                        windowSubCounter[ch/16] += histogramCounter[j][ch];
                    }
                }


                if(i >= mKernelSize - 1 && j >= mKernelSize - 1){
                    int x = j - mKernelSize + 1;
                    int y = i - mKernelSize + 1;
                    int before = 0;
                    for(int csub = 0; csub <= 255; csub += 16){
                        if(before + windowSubCounter[csub/16] < medianIndex){
                            before += windowSubCounter[csub/16];
                            continue;
                        }
                        for(int cw = csub; cw < csub+16; cw++){
                            before += windowCounter[cw];
                            if( before >= medianIndex ){
                                X[y][x] = cw;
                                break;
                            }
                        }
                        if( before >= medianIndex ){
                            break;
                        }
                    }

                }
            }
        }
        return X;
    }
}
