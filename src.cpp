#include "filters.h"
#include<cstdio>
#include<cstdlib>
#include<ctime>
#include<vector>

using namespace std;

Image getFullyRandomImage(int height, int width) {
	Image m(height, width);
	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++) {
			m.bitmap[i][j] = rand() % 256;
		}
	}
	return m;
}

Image getRandomBoxesImage(int height, int width) {
	Image m(height, width);
	for (int n = 0; n < 1000; n++) {
		int color = rand() % 256;
		int left = rand() % width;
		int bottom = rand() % height;
		int right = left + rand() % (width - left);
		int top = bottom + rand() % (height - bottom);
		for (int i = bottom; i <= top; i++) {
			for (int j = left; j <= right; j++) {
				m.bitmap[i][j] = color;
			}
		}
	}
	for (int i = 0; i < height;i++) {
		m.addGaussianNoise();
		m.addSaltAndPepperNoise();
	}
	return m;
}

Image getRandomCirclesImage(int height, int width) {
	Image m(height, width);
	for (int n = 0; n < 1000; n++) {
		int color = rand() % 256;
		int cx = rand() % width;
		int cy = rand() % height;
		int r = rand() % (height / 3);
		for (int i = cy - r; i <= cy + r; i++) {
			if (i < 0 || i >= height) continue;
			for (int j = cx - r; j <= cx + r; j++) {
				if (j < 0 || j >= width) continue;
				int d = (cx - j) *(cx - j) + (cy - i)*(cy - i);
				if (d > r)	continue;
				m.bitmap[i][j] = color;
			}
		}
	}
	for (int i = 0; i < height;i++) {
		m.addGaussianNoise();
		m.addSaltAndPepperNoise();
	}
	return m;
}

int main() {
	int width = 1024;
	int height = 1024;
	vector<Image> vImages;
	for (int i = 0; i < 10; i++) {
		vImages.push_back(getRandomBoxesImage(height, width));
		vImages.push_back(getRandomCirclesImage(height, width));
		vImages.push_back(getFullyRandomImage(height, width));
	}
	printf("Kernel Radius, BF, BF-Optimized, Huangs, Proposed, ProposedOptimizedV1\n");
	for (int r = 0; r <= 200; r += 10) {
		vector<MedianFilter*> vFilter;/*
		vFilter.push_back(new BruteForceFilter(r));
		vFilter.push_back(new BruteForceOptimizedFilter(r));*/
		vFilter.push_back(new HuangsFilter(r));
		vFilter.push_back(new ProposedFilter(r));
		vFilter.push_back(new ProposedOptimizedV1Filter(r));
		printf("%d", r);
		double timeCost = 0;
		for (MedianFilter* filter : vFilter) {
			clock_t t = clock();
			for (Image m : vImages) {
				filter->process(m);
			}
			t = clock() - t;
			double timeCost = (double) t / CLOCKS_PER_SEC;
			timeCost /= vImages.size();
			printf(",%.10lf", timeCost);
		}
		printf("\n");
	}


	return 0;
}
