package cn.edu.ustc.offline;

import java.io.IOException;

import cn.edu.ustc.main.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class OfflineRead extends Activity {
	/** Called when the activity is first created. */
	private PageWidget mPageWidget;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private Canvas mCurPageCanvas, mNextPageCanvas;
	private BookPageFactory pagefactory;
	private String filepath;
	private int height;
	private int width;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();  	//获取屏幕分辨率 
        getWindowManager().getDefaultDisplay().getMetrics(dm);   
        width = dm.widthPixels;
        height = dm.heightPixels;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mPageWidget = new PageWidget(this,height,width);
		setContentView(mPageWidget);
		/**************/
		Bundle bundle = this.getIntent().getExtras();
		filepath= bundle.getString("filepath");
		/****************/
		mCurPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(width, height);

		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.bg));
			
			try {
				pagefactory.openbook(filepath);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			pagefactory.onDraw(mCurPageCanvas);
		
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {				
				boolean ret=false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}						
							if(pagefactory.isfirstPage())return false;
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if(pagefactory.islastPage())return false;
							pagefactory.onDraw(mNextPageCanvas);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
                 
					 ret = mPageWidget.doTouchEvent(e);
					return ret;
				}
				return false;
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){		
			Intent intent=new Intent();
 			intent.setClass(OfflineRead.this,OfflineList.class);
 			startActivity(intent);
 			OfflineRead.this.finish();
    		
		}
		return super.onKeyDown(keyCode, event);
	}
}