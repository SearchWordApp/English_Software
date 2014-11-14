package cn.edu.ustc.word;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import cn.edu.ustc.main.MainActivity;
import cn.edu.ustc.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WordLearnMain extends Activity {
	
	private ArrayList<HashMap<String, Object>> lstImageItem;
	private HashMap<String, Object> map;
	private Context cont;
	private String fileroot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_learn_main);
		Button button_backtomain = (Button) findViewById(R.id.button_backtomainfromlearn);
		button_backtomain.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordLearnMain.this, MainActivity.class);
				startActivity(intent);
				WordLearnMain.this.finish();
			}
		});
		Button button_SC = (Button) findViewById(R.id.button_SC);
		button_SC.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordLearnMain.this, WordSCShow.class);
				startActivity(intent);
				WordLearnMain.this.finish();
			}
		});

		GridView mGridView = (GridView) findViewById(R.id.gridviewwordlearn);

		lstImageItem = new ArrayList<HashMap<String, Object>>();
		cont = this.getApplicationContext();
		fileroot = cont.getFilesDir().getAbsolutePath() + "/lexicon/";

		File filedir = new File(fileroot);
		if (!filedir.exists())
			filedir.mkdir();
		if (filedir.isDirectory()) {
			File[] filelist = filedir.listFiles();
			if (filelist.length == 0)
				Toast.makeText(this, "暂无可学习词库，请先添加！", Toast.LENGTH_SHORT)
						.show();

			for (int i = 0; i < filelist.length; i++) {
				map = new HashMap<String, Object>();
				map.put("ItemImage", R.drawable.word_logo); // 添加图像资源的ID
				map.put("filename", filelist[i].getName());
				lstImageItem.add(map);
			}
			// 构建一个适配器
			SimpleAdapter simple = new SimpleAdapter(this, lstImageItem,
					R.layout.common_gridviewitem, new String[] { "ItemImage",
							"filename" }, new int[] { R.id.ItemImage,
							R.id.ItemText });
			mGridView.setAdapter(simple);
			mGridView
					.setOnItemClickListener(new GridView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View view, final int position, long id) {
							String filename = (String) lstImageItem.get(
									position).get("filename");
							Bundle bundle = new Bundle();
							bundle.putString("filename", filename);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(WordLearnMain.this,
									WordCourceSelect.class);
							startActivity(intent);
							WordLearnMain.this.finish();
						}
					});
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(WordLearnMain.this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}