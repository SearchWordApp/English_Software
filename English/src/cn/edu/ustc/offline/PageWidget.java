package cn.edu.ustc.offline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class PageWidget extends View {

	//private static final String TAG = "hmg";
	private int mWidth = 240;
	private int mHeight = 400;
	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;
	private Path mPath0;
	private Path mPath1;
	Bitmap mCurPageBitmap = null; // 当前页
	Bitmap mNextPageBitmap = null;

	PointF mTouch = new PointF(); // 拖拽点
	PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

	PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	PointF mBezierControl2 = new PointF();
	PointF mBeziervertex2 = new PointF();
	PointF mBezierEnd2 = new PointF();

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	float mTouchToCornerDis;
	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // 是否属于右上左下
	float mMaxLength = (float) Math.hypot(mWidth, mHeight);
	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;

	Paint mPaint;

	Scroller mScroller;

	public PageWidget(Context context,int height,int width) {
		super(context);
		this.mHeight = height;
		this.mWidth = width;
		// TODO Auto-generated constructor stub
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;
	}

	

}
