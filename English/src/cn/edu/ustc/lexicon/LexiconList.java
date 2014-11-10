package cn.edu.ustc.lexicon;

import cn.edu.ustc.main.FileOperate;
import cn.edu.ustc.main.MainActivity;
import cn.edu.ustc.main.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class LexiconList extends Activity {
	
	private ArrayList<HashMap<String, Object>> lstImageItem;
	private HashMap<String, Object> map;
	private String filepath="";
	private Context cont;
	private String fileroot ;
	private String sdcardpath;
	private String newfilename;	
	
    @Override
    public void onCreate(final Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lexicon_list);   
        Button button_select=(Button) findViewById(R.id.button_select);
        Button button_add=(Button) findViewById(R.id.button_add);
        button_select.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        			Intent intent1=new Intent();  
        			intent1.setClass(LexiconList.this,AddLexicon.class);
            		startActivity(intent1); 
            		LexiconList.this.finish();
                }
                else{
                	AlertDialog.Builder builder = new Builder(LexiconList.this); 
                	builder.setTitle("提示");                  	
                	builder.setMessage("SD卡不存在！");
                	builder.setPositiveButton("确定", null);
                	builder.show();  
                }        		 
        	}
        });
        
        button_add.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		final Dialog dialog1=new Dialog(LexiconList.this);
   				dialog1.setTitle("请输入词库名称");
   				dialog1.setContentView(R.layout.common_renamedialog);
   				dialog1.show();
   				final EditText newName=(EditText) dialog1.findViewById(R.id.newName);   
   				
   				Button confirm=(Button) dialog1.findViewById(R.id.attention_confirm_button);
   				Button cancel=(Button) dialog1.findViewById(R.id.attention_cancel_button);
   			               				
   				cancel.setOnClickListener(new OnClickListener(){  				
   					public void onClick(View v){
   						dialog1.cancel();
   					}   				
   				});
   				              				
   				confirm.setOnClickListener(new OnClickListener(){
   					public void onClick(View v) {		
   						newfilename = newName.getText().toString().trim();
   						if(newfilename.trim().equals("")){
   							Toast.makeText(LexiconList.this,"输入不能为空！", Toast.LENGTH_SHORT).show();                 	          			
   						}
   						else{
   							final String targetfilepath = fileroot + newfilename +".xml";
   							final File targetfile = new File(targetfilepath);
   							if(targetfile.exists()){
   								new AlertDialog.Builder(LexiconList.this)
   								.setTitle("提示") 
   								.setMessage("存在同名词库，确定覆盖吗？")
   								.setPositiveButton("确定", new DialogInterface.OnClickListener(){
   									public void onClick(DialogInterface dialog, int whichButton) { 
   										setResult(RESULT_OK);//确定按钮事件 
   										
   										//此处实现新建词库功能
   										FileOperate fo = new FileOperate();
   										fo.deleteFile(targetfilepath);
   										fo.newFile(targetfilepath);  										
   										Toast.makeText(LexiconList.this,"新建词库成功！", Toast.LENGTH_SHORT).show();  
   										dialog1.dismiss();
   										onCreate(savedInstanceState);
   									}						
   								})
   								.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
   									public void onClick(DialogInterface dialog, int whichButton) { 
   										//取消按钮事件 
   									} 
   								})
   								.show();
   							}  
   							else{
   								//此处实现新建词库功能
   								FileOperate fo = new FileOperate();
								fo.newFile(targetfilepath);  										
								Toast.makeText(LexiconList.this,"新建词库成功！", Toast.LENGTH_SHORT).show();  
								dialog1.dismiss();
								onCreate(savedInstanceState);
							}						
   						}	          				
   					}      	
   				});
        	}
        });
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent();
    		intent.setClass(LexiconList.this,MainActivity.class);
    		startActivity(intent);	
    		this.finish();								
		}
		return super.onKeyDown(keyCode, event);
	}
      
}
    
