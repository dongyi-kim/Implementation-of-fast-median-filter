/**
 * Created by dongyi.kim on 2017-06-07.
 */
public abstract class MedianFilter {
    public final int mKernelSize;
    protected MedianFilter(int kernelSize)
    {
        this.mKernelSize = kernelSize;
    }

    public abstract int[][] filtering(int[][] m);
    public final int[][] getEmptyOutputMatrix(int[][] m)
    {
        int h = m.length - mKernelSize + 1;
        int r = m[0].length - mKernelSize + 1;
        return new int[h][r];
    }

}
