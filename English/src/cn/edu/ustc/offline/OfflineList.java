package cn.edu.ustc.offline;

import cn.edu.ustc.main.MainActivity;
import cn.edu.ustc.main.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class OfflineList extends Activity {
	
	private Context cont;
    private String fileroot ;	
	private ListView offlineList;
	private String[] offlineNames;
	private Bundle savedInstanceState;
	
    public void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.offline_list);   
        offlineList = (ListView) findViewById(R.id.listOffline);
        Button button_select_offline=(Button) findViewById(R.id.button_select_offline);
        Button button_backtomain=(Button) findViewById(R.id.button_backtomain);
        button_select_offline.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        			Intent intent1=new Intent();  
        			intent1.setClass(OfflineList.this,AddOffline.class);
            		startActivity(intent1); 
            		OfflineList.this.finish();
                }
                else{
                	AlertDialog.Builder builder = new Builder(OfflineList.this); 
                	builder.setTitle("提示");                  	
                	builder.setMessage("SD卡不存在！");
                	builder.setPositiveButton("确定", null);
                	builder.show();  
                }        		 
        	}
        });
        button_backtomain.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent=new Intent();
        		intent.setClass(OfflineList.this,MainActivity.class);
        		startActivity(intent);	
        		OfflineList.this.finish();    		 
        	}
        });
    }
    
    private List<Map<String, Object>> getData() {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();                
        for(int i = 0; i < offlineNames.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("text",offlineNames[i]);  
            map.put("img", R.drawable.offline_logo);            
            list.add(map);  
        }           
        return list;  
    }  

    
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent();
    		intent.setClass(OfflineList.this,MainActivity.class);
    		startActivity(intent);	
    		this.finish();								
		}
		return super.onKeyDown(keyCode, event);
	}
      
}
    
