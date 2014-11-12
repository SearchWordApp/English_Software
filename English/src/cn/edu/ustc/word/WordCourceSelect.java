package cn.edu.ustc.word;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.ustc.main.R;

public class WordCourceSelect extends Activity {

	private int wordCount = 0;
	private int courceSize = 50;
	private int cource = 1;
	private EditText editText;
	private TextView textView;
	private String wordCountDesc;
	private String filename;
	private int courceCount;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_cource_select);
		Bundle bundle = this.getIntent().getExtras();
		filename = getApplicationContext().getFilesDir().getAbsolutePath()
				+ "/lexicon/" + bundle.getString("filename");
		editText = (EditText) findViewById(R.id.courceselect);
		textView = (TextView) findViewById(R.id.wordcount);
		progressDialog = ProgressDialog.show(WordCourceSelect.this, "",
				"正在统计单词数量，请稍后...", true, false);
		new Thread() {
			public void run() {
				try {
					wordCount = new XmlPull(filename).getWordCount();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}.start();


		
}
