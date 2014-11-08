package cn.edu.ustc.main;

import cn.edu.ustc.lexicon.LexiconList;
import cn.edu.ustc.word.WordLearnMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context cont = MainActivity.this.getApplicationContext();
		String filerootLexicon = cont.getFilesDir().getAbsolutePath()+"/lexicon/";
		String filerootOffline = cont.getFilesDir().getAbsolutePath()+"/offline/";
		String path;
		File filedirLexicon = new File(filerootLexicon);
		if(!filedirLexicon.exists()){
			filedirLexicon.mkdir();
			path = filerootLexicon + "cet-4.xml";
			importRes(R.raw.cet4, path);
		}
        File filedirOffline = new File(filerootOffline);
		if(!filedirOffline.exists()){
			filedirOffline.mkdir();
			path = filerootOffline + "Home on the Way.txt";
			importRes(R.raw.homeontheway, path);
			path = filerootOffline + "Love Is Not Like Merchandise.txt";
			importRes(R.raw.loveisnotlikemerchandise, path);
			path = filerootOffline + "Remembering Why We Are Doing Something.txt";
			importRes(R.raw.rememberingwhywearedoingsomething, path);
		}
		String filerootsc = cont.getFilesDir().getAbsolutePath()+"/sc.xml";
		File filedirsc = new File(filerootsc);
		if(!filedirsc.exists()){
			FileOperate fo = new FileOperate();
			fo.newFile(filerootsc); 
		}
		
		ImageButton button_lexicon=(ImageButton) findViewById(R.id.button_lexicon);
		ImageButton button_word_learn = (ImageButton) findViewById(R.id.button_word_learn);
		ImageButton button_offline = (ImageButton) findViewById(R.id.button_offline);
        
        
        button_word_learn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent=new Intent();
        		intent.setClass(MainActivity.this,WordLearnMain.class);
        		startActivity(intent);
        		MainActivity.this.finish();
			}
        	
        } );
        
        button_lexicon.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent=new Intent();
        		intent.setClass(MainActivity.this,LexiconList.class);
        		startActivity(intent);
        		MainActivity.this.finish();
        	}
        });
        
        
        button_offline.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {				
				Intent intent=new Intent();
        		intent.setClass(MainActivity.this,cn.edu.ustc.offline.OfflineList.class);
        		startActivity(intent);
        		MainActivity.this.finish();
			}
        } );
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(this) 
	         .setTitle("提示") 
	         .setMessage("确定退出吗？") 
	         .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	        	 public void onClick(DialogInterface dialog, int whichButton) { 
	        		 setResult(RESULT_OK);//确定按钮事件 
	        		 finish(); 
	        		 System.exit(0);
	        	 } 
	         }) 
	         .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
	        	 public void onClick(DialogInterface dialog, int whichButton) { 
	        		 //取消按钮事件 
	        	 } 
	         }).show();
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public void importRes(int i, String path){
    	FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int count = 0;
			InputStream is = getResources().openRawResource(i);
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
}
