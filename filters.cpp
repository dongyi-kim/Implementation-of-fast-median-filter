#include "filters.h"
#include<memory.h>
#include <algorithm>
#include<vector>

using namespace std;

void Image::addGaussianNoise()
{
	int dc = (rand() % 7) - 3;
	int i = rand() % height;
	int j = rand() % width;
	int org = bitmap[i][j];
	bitmap[i][j] = min(255,max(0, org + dc));
}

void Image::addSaltAndPepperNoise() {
	int i = rand() % height;
	int j = rand() % width;
	bitmap[i][j] = (rand() % 2) * 255;
}

Image BruteForceFilter::process(Image m) {
	Image X(m.height - mKernelRadius * 2, m.width - 2 * mKernelRadius);
	vector<int> window((mKernelRadius*2 + 1)*(mKernelRadius*2+1));
	for (int ci = mKernelRadius; ci + mKernelRadius < m.height; ci++) {
		for (int cj = mKernelRadius; cj + mKernelRadius < m.width; cj++) {
			int y = ci - mKernelRadius;
			int x = cj - mKernelRadius;
			int cnt = 0;
			for (int i = ci - mKernelRadius; i <= ci + mKernelRadius; i++) {
				for (int j = cj - mKernelRadius; j <= cj + mKernelRadius; j++) {
					window[cnt++] = m.bitmap[i][j];
				}
			}
			sort(window.begin(), window.end());
			X.bitmap[y][x] = window[mKernelRadius];
		}
	}
	return X;
}

Image BruteForceOptimizedFilter::process(Image m) {
	Image X(m.height - mKernelRadius * 2,m.width - 2 * mKernelRadius);
	int cnt[256];
	int cntSub[16];
	memset(cnt, 0, sizeof(cnt));
	memset(cntSub, 0, sizeof(cntSub));
	for (int ci = mKernelRadius; ci + mKernelRadius < m.height; ci++) {
		for (int cj = mKernelRadius; cj + mKernelRadius < m.width; cj++) {
			int y = ci - mKernelRadius;
			int x = cj - mKernelRadius;
			for (int i = ci - mKernelRadius; i <= ci + mKernelRadius; i++) {
				for (int j = cj - mKernelRadius; j <= cj + mKernelRadius; j++) {
					cnt[m.bitmap[i][j]]++;
					cntSub[m.bitmap[i][j] / 16]++;
				}
			}
			int noc = 0;
			for (int color = 0; color < 256; color++) {
				if (noc + cntSub[color / 16] <= mKernelRadius) {
					noc += cntSub[color / 16];
					continue;
				}
				noc += cnt[color];
				if (noc > mKernelRadius) {
					X.bitmap[y][x] = color;
					break;
				}
			}
		}
	}
	return X;
}


Image HuangsFilter::process(Image m) {
	Image X(m.height - mKernelRadius * 2, m.width - 2 * mKernelRadius);
	for (int ci = mKernelRadius; ci + mKernelRadius < m.height; ci++) {
		int cnt[256];
		int cntSub[16];
		memset(cnt, 0, sizeof(cnt));
		memset(cntSub, 0, sizeof(cntSub));
		for (int j = 0; j < m.width; j++) {
			for (int i = ci - mKernelRadius; i <= ci + mKernelRadius; i++) {
				byte colorToAdd = m.bitmap[i][j];
				cnt[colorToAdd] ++;
				cntSub[colorToAdd/16]++;
				if (j - (mKernelRadius * 2 + 1) >= 0) {
					byte colorToRemove = m.bitmap[i][j - (mKernelRadius*2+1)];
					cnt[colorToRemove] --;
					cntSub[colorToAdd / 16] --;
				}
			}

			int y = ci - mKernelRadius;
			int x = j - mKernelRadius*2;
			if (x < 0 || y < 0) continue;
			int noc = 0;
			for (int color = 0; color < 256; color++) {
				if (noc + cntSub[color / 16] <= mKernelRadius) {
					noc += cntSub[color / 16];
					continue;
				}
				noc += cnt[color];
				if (noc > mKernelRadius) {
					X.bitmap[y][x] = color;
					break;
				}
			}
		}
	}
	return X;
}


Image ProposedFilter::process(Image m) {
	Image X(m.height - mKernelRadius * 2, m.width - 2 * mKernelRadius);
	vector<vector<int> > h(m.width, vector<int>(256, 0));

	for (int i = 0; i < m.height; i++) {
		int cnt[256];
		int cntSub[16];
		memset(cnt, 0, sizeof(cnt));
		memset(cntSub, 0, sizeof(cntSub));
		for (int j = 0; j < m.width; j++) {
			byte colorToAdd = m.bitmap[i][j];
			h[j][colorToAdd]++;
			int ri = i - (mKernelRadius * 2 + 1);
			if (ri >= 0) {

				byte colortoRemove = m.bitmap[i][j];
				h[j][colortoRemove]--;
			}

			for (int color = 0; color < 256; color++) {
				cnt[color] += h[j][color];
				cntSub[color / 16] += h[j][color];
			}

			int rj = j - (mKernelRadius * 2 + 1);
			if (rj >= 0) {
				for (int color = 0; color < 256;color++) {
					cnt[color] -= h[rj][color];
					cntSub[color / 16] -= h[rj][color];
				}
			}
		
			int y = i - mKernelRadius*2;
			int x = j - mKernelRadius*2;
			if (y < 0 || x < 0)	continue;
			int noc = 0;
			for (int color = 0; color < 256; color++) {
				if (noc + cntSub[color / 16] <= mKernelRadius) {
					noc += cntSub[color / 16];
					continue;
				}
				noc += cnt[color];
				if (noc > mKernelRadius) {
					X.bitmap[y][x] = color;
					break;
				}
			}
		}
	}
	return X;
}



Image ProposedOptimizedV1Filter::process(Image m) {
	Image X(m.height - mKernelRadius * 2, m.width - 2 * mKernelRadius);
	vector<vector<int> > h(m.width, vector<int>(256, 0));
	vector<vector<int> > hSub(m.width, vector<int>(16, 0));

	for (int i = 0; i < m.height; i++) {
		int cnt[256];
		int cntSub[16];
		memset(cnt, 0, sizeof(cnt));
		memset(cntSub, 0, sizeof(cntSub));
		for (int j = 0; j < m.width; j++) {
			byte colorToAdd = m.bitmap[i][j];
			h[j][colorToAdd]++;
			hSub[j][colorToAdd / 16]++;
			int ri = i - (mKernelRadius * 2 + 1);
			if (ri >= 0) {
				byte colortoRemove = m.bitmap[i][j];
				h[j][colortoRemove]--;
				hSub[j][colortoRemove / 16]--;
			}

			int noc = 0;
			int med = -1;
			int rj = j - (mKernelRadius * 2 + 1);
			int y = i - mKernelRadius * 2;
			int x = j - mKernelRadius * 2;
			for (int csub = 0; csub < 16; csub++) {
				int cl = csub * 16;
				int cr = cl + 15;
				if (hSub[j][csub] > 0)
				{
					for (int color = cl; color <= cr; color++) {
						cnt[color] += h[j][color];
						cntSub[color / 16] += h[j][color];
					}
				}
				if (rj >= 0 && hSub[rj][csub]>0) {
					for (int color = cl; color <= cr; color++) {
						cnt[color] -= h[rj][color];
						cntSub[color / 16] -= h[rj][color];
					}
				}
				if (y < 0 || x < 0)	continue;
				if (med == -1 && noc + cntSub[csub] <= mKernelRadius)
				{
					noc += cntSub[csub];
					continue;
				}
				else {
					for (int color = cl; color <= cr; color++) {
						noc += cnt[color];
						if (noc > mKernelRadius) {
							med = color;
							break;
						}
					}
				}
			}


			if (y < 0 || x < 0)	continue;
			X.bitmap[y][x] = med;
		}
	}
	return X;
}
