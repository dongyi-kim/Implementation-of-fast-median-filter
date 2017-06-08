/**
 * Created by dongyi.kim on 2017-06-07.
 */
public class CustomFilter extends MedianFilter {
    public CustomFilter(int kernelSize) {
        super(kernelSize);
    }

    private int[][][][] mPyramid;
    private void generatePyramid(int[][] m){
        mPyramid = new int[mTreeHeight+1][][][];
        for(int h = 1, w = 1; h <= mTreeHeight; h ++ , w *= 2){
            mPyramid[h] = new int[w][w][256];
        }
        for(int r = 0 ; r < m.length; r++){
            for(int c = 0 ; c < m[0].length; c++){
                int color = m[r][c];
                mPyramid[mTreeHeight][r][c][ color ] ++;
            }
        }

        for(int lv = mTreeHeight ; lv >= 2; lv--) {
            int size = mPyramid[lv].length;
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    int pr = r / 2;
                    int pc = c / 2;
                    for (int color = 0; color <= 255; color++) {
                        mPyramid[lv - 1][pr][pc][color] += mPyramid[lv][r][c][color];
                    }
                }
            }
        }
    }

    private int[] getCountMatrix(int bottom, int top, int left, int right){
        return query(bottom, top, left, right, 0, mTreeWidth -1, 0, mTreeWidth - 1, 1);
    }
    private int[] query(int bottom, int top, int left, int right, int bm, int tm, int lm, int rm, int lv){
        if( top < bm || tm < bottom || rm < left || right < lm){
            return new int[256];
        }
        if( bottom <= bm && tm <= top && left <= lm && rm <= right){
            int rc = tm / (tm - bm + 1);
            int cc = rm / (rm - lm + 1);
            return mPyramid[lv][rc][cc];
        }

        int lrCenter = (lm +rm ) / 2;
        int btCenter = (bm +tm ) / 2;
        int[] m1 = query(bottom, top, left, right, bm,              btCenter,    lm,            lrCenter, lv+1);
        int[] m2 = query(bottom, top, left, right, bm,              btCenter,lrCenter + 1,  rm, lv+1);
        int[] m3 = query(bottom, top, left, right, btCenter+1, tm,           lm,            lrCenter, lv+1);
        int[] m4 = query(bottom, top, left, right, btCenter+1, tm,      lrCenter + 1,   rm, lv+1);
        int[] ret = new int[256];
        for(int color = 0; color <= 255; color++){
            ret[color] = m1[color] + m2[color] + m3[color] + m4[color];
        }
        return ret;
    }

    private int mTreeWidth = 0;
    private int mTreeHeight = 0;

    @Override
    public int[][] filtering(int[][] m) {
        int h = m.length;
        int w = m[0].length;
        int size = Math.max(h,w);
        int medianIndex = mKernelSize * mKernelSize / 2;
        for(mTreeHeight = 1, mTreeWidth = 1; mTreeWidth < size; ){
            mTreeWidth *= 2;
            mTreeHeight ++;
        }

        generatePyramid(m);

        int[][] X = getEmptyOutputMatrix(m);
        for(int y = 0 ; y < X.length ; y ++){
            for(int x = 0 ; x < X[0].length; x++){
                int[] cnt = getCountMatrix(y,y+mKernelSize-1,x, x +mKernelSize-1);
                int before = 0;
                for(int color = 0 ; color <= 255; color++){
                    before += cnt[color];
                    if(before >= medianIndex){
                        X[y][x] = (byte)color;
                        break;
                    }
                }
            }
        }

        return X;
    }
}
