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
		
		Button button_minus = (Button) this.findViewById(R.id.cource_group)
				.findViewById(R.id.button_cource_minus);
		Button button_add = (Button) findViewById(R.id.button_cource_add);
		button_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (wordCount != 0) {
					cource = Integer.valueOf(editText.getText().toString());
					if (cource < courceCount)
						cource++;
					editText.setText(String.valueOf(cource));
				}
			}
		});

		button_minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (wordCount != 0) {
					cource = Integer.valueOf(editText.getText().toString());
					if (cource > 1)
						cource--;
					editText.setText(String.valueOf(cource));
				}
			}
		});
		
		Button button_startButton = (Button) findViewById(R.id.button_word_learn_start);
		button_startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (wordCount == 0) {
					Toast.makeText(WordCourceSelect.this, "当前词库没有单词！",
							Toast.LENGTH_SHORT).show();
				} else {
					try{
						cource = Integer.parseInt(editText.getText().toString().trim());
					}catch (Exception e){
						cource = 1;
					}
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("courceSize", courceSize);
					bundle.putInt("cource", cource);
					bundle.putInt("wordCount", wordCount);
					bundle.putString("filename", filename);
					intent.putExtras(bundle);
					intent.setClass(WordCourceSelect.this, WordLearnShow.class);
					startActivity(intent);
				}
			}
		});
	}

		
}
