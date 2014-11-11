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
		
		cont = this.getApplicationContext();
        fileroot = cont.getFilesDir().getAbsolutePath()+"/offline/";
        		
		File filedir = new File(fileroot);
		if(!filedir.exists())
			filedir.mkdir();
		if(filedir.isDirectory()){
			File[] filelist = filedir.listFiles();
			if(filelist.length==0)
				Toast.makeText(this,"当前阅读列表为空！",Toast.LENGTH_SHORT ).show();
			else{
				offlineNames = new String[filelist.length];
		    	for (int i=0;i<offlineNames.length;i++){
		    		offlineNames[i] = filelist[i].getName();
		    	} 
				offlineList.setAdapter(new SimpleAdapter(this, getData(), R.layout.common_listitem,   
                        new String[]{"img", "text"},   
                        new int[]{R.id.list_img, R.id.list_row}));
				offlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						onListItemClick(arg2);
					}
				});
			}      
		}
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

        String filepath;
	String newfilename;
    public void onListItemClick(final int position) {
    	
    	filepath = fileroot + offlineNames[position];
    	AlertDialog.Builder builder = new Builder(OfflineList.this); 
		builder.setTitle("选择操作"); 
		builder.setItems(new String[] {"开始阅读","删除短文","重命名短文"},
			new DialogInterface.OnClickListener() {                    
            public void onClick(DialogInterface dialog, int which) {
            	switch(which){
         	   	case 0:
	         	   	Intent intent=new Intent();            	
	        		intent.putExtra("filepath", filepath);	         	            		
	        		intent.setClass(OfflineList.this,OfflineRead.class);
	        		startActivity(intent);
	        		OfflineList.this.finish();
	        		break;
         	   	case 1:
	         	   	new AlertDialog.Builder(OfflineList.this) 
	  	          	.setTitle("提示") 
	  	          	.setMessage("确定删除该短文吗？") 
	  	          	.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	  	          		public void onClick(DialogInterface dialog, int whichButton) { 
	  	          			setResult(RESULT_OK);//确定按钮事件 
	  	          			File file = new File( filepath);
	  	          			file.delete();
	   						Toast.makeText(OfflineList.this,"短文删除成功！", Toast.LENGTH_SHORT).show();               						
	   						onCreate(savedInstanceState);
	  	          		}								
	  	          	})           	
	  	          	.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
	  	          		public void onClick(DialogInterface dialog, int whichButton) { 
	  	        		 //取消按钮事件 
	  	          		} 
	  	          	}) 
	  	          	.show();
         	   		break;
         	   	case 2:
         	   	
         	   		break;
            	}
            }
		});
		builder.show();  
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
    
