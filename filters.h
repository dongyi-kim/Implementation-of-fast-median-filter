#include<algorithm>
#include<vector>
using namespace std;

#ifndef __FILTERS_H__
#define __FILTERS_H__
typedef unsigned char byte;
class Image {
public:
	int width;
	int height;
	vector<vector<byte> > bitmap;
	Image(int height, int width) : height(height), width(width)
	{
		bitmap.resize(height, vector<byte>(width, 0));
	}

	void addSaltAndPepperNoise();
	void addGaussianNoise();
};

class MedianFilter {
public:
	int mKernelRadius;
	MedianFilter(int kernelRadius) {
		this->mKernelRadius = kernelRadius;
	}
	

public:
	virtual Image process(Image m) = 0;
};

class BruteForceFilter : public MedianFilter {
public:
	BruteForceFilter(int kernelRadius) : MedianFilter(kernelRadius)
	{}
	Image process(Image m);
};

class BruteForceOptimizedFilter : public  MedianFilter {
public:
	BruteForceOptimizedFilter(int kernelRadius) : MedianFilter(kernelRadius)
	{}
	Image process(Image m);
};

class HuangsFilter : public MedianFilter {
public:
	HuangsFilter(int kernelRadius) : MedianFilter(kernelRadius)
	{}
	Image process(Image m);
};

class ProposedFilter :public  MedianFilter {
public:
	ProposedFilter(int kernelRadius) : MedianFilter(kernelRadius)
	{}
	Image process(Image m);
};



class ProposedOptimizedV1Filter : public MedianFilter {
public:
	ProposedOptimizedV1Filter(int kernelRadius) : MedianFilter(kernelRadius)
	{}
	Image process(Image m);
};






#endif
