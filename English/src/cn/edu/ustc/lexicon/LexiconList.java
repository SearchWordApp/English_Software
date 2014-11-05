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
        
        //实例化GridView
        GridView mGridView=(GridView) findViewById(R.id.gridview);
        // 生成动态数组，并且传入数据
        
        //此处需要根据词库数量确定for循环的值
        //预先读取所有词库名称，放到一个string数组中，以便以下for循环时添加ItemText，即词库名称
        lstImageItem = new ArrayList<HashMap<String, Object>>();
        cont = this.getApplicationContext();
        fileroot = cont.getFilesDir().getAbsolutePath()+"/lexicon/";
        		
		File filedir = new File(fileroot);
		if(!filedir.exists())
			filedir.mkdir();
		if(filedir.isDirectory()){
			File[] filelist = filedir.listFiles();
    	if(filelist.length==0)
			Toast.makeText(this,"当前词库为空，请先添加词库！",Toast.LENGTH_SHORT ).show();
        
        for (int i = 0; i < filelist.length; i++) {
            map = new HashMap<String, Object>();         
            map.put("ItemImage", R.drawable.lexicon_logo);	//添加图像资源的ID
            map.put("filename", filelist[i].getName());
            lstImageItem.add(map);
        }
        //构建一个适配器
        SimpleAdapter simple = new SimpleAdapter(this, lstImageItem,
                R.layout.common_gridviewitem,
                new String[] { "ItemImage", "filename" }, 
                new int[] { R.id.ItemImage, R.id.ItemText });
        mGridView.setAdapter(simple);
        
        //添加选择项监听事件
        mGridView.setOnItemClickListener(new GridView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				//首先根据点击目标获取词库名称，再进行对应操作
				filepath = (String) (fileroot + (String)lstImageItem.get(position).get("filename"));
				sdcardpath = (String)(Environment.getExternalStorageDirectory()+"/"+(String)lstImageItem.get(position).get("filename"));
				
				AlertDialog.Builder builder = new Builder(LexiconList.this); 
				builder.setTitle("选择操作"); 
				builder.setItems(new String[] {"进入词库","导出词库","删除词库","重命名词库"},
					new DialogInterface.OnClickListener() {                    
                    public void onClick(DialogInterface dialog, int which) {
                           
                	   switch(which){
                	   case 0:
                		   String temp =(String)lstImageItem.get(position).get("filename");
                		   String  lexicon=temp.substring(0,temp.lastIndexOf("."));
                		   Intent intent=new Intent();
                		   intent.setClass(LexiconList.this,WordInfo.class);
                		   intent.putExtra("lexicon", lexicon); 
                		   intent.putExtra("filepath", filepath); 
                		   startActivity(intent);
                		   break;
                		   	
                	   case 1:                		   
                		   //此处实现词库导出功能
                		   final File sourcefile = new File(filepath);
                		   final File targetfile = new File(sdcardpath);
                		   if(targetfile.exists()){
                			   new AlertDialog.Builder(LexiconList.this)
              					.setTitle("提示") 
              					.setMessage("sd卡中存在同名词库，确定覆盖吗？")
              					.setPositiveButton("确定", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog,int which) {
										FileOperate fo = new FileOperate();
						   				try {
						   					fo.copyFile(sourcefile,targetfile);
						   					
						   				} catch (IOException e) {}
						   				new AlertDialog.Builder(LexiconList.this) 
						          		.setTitle("提示") 
						          		.setMessage("导出成功，词库文件位于：\n"+sdcardpath) 
						          		.setPositiveButton("确定",null)
						          		.show();
									}             						
              					})
              					.setNegativeButton("取消", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog,
											int which) {										
									}             						
              					})
              	  	          	.show();
                		   }
                		   else{
                			   FileOperate fo = new FileOperate();
				   				try {
				   					fo.copyFile(sourcefile,targetfile);
				   					
				   				} catch (IOException e) {}
				   				new AlertDialog.Builder(LexiconList.this) 
				          		.setTitle("提示") 
				          		.setMessage("导出成功， 词库文件位于：\n "+sdcardpath) 
				          		.setPositiveButton("确定",null)
				          		.show();
                		   }
              				break;
               				
                	   case 2:
                		   	new AlertDialog.Builder(LexiconList.this) 
              	          	.setTitle("提示") 
              	          	.setMessage("确定删除该词库吗？") 
              	          	.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
              	          		public void onClick(DialogInterface dialog, int whichButton) { 
              	          			setResult(RESULT_OK);//确定按钮事件 
              	          			
              	          			//此处添加删除词库代码
              	          			File file = new File( filepath);
              	          			file.delete();
               						Toast.makeText(LexiconList.this,"词库删除成功！", Toast.LENGTH_SHORT).show();               						
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
                		   	
                	   case 3:               		   
                		    final Dialog dialog1=new Dialog(LexiconList.this);
               				dialog1.setTitle("请输入新名称");
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
               						//此处实现重命名的功能，newName即为新名称
               						newfilename = newName.getText().toString().trim();
               						if(newfilename.trim().equals("")){
               							Toast.makeText(LexiconList.this,"输入不能为空！", Toast.LENGTH_SHORT).show();                 	          			
               						}
               						else{
               							String targetfilepath = fileroot + newfilename +".xml";
               							final File targetfile = new File(targetfilepath);
               							final File sourcefile = new File(filepath);
               							if(targetfile.exists()){
               								new AlertDialog.Builder(LexiconList.this)
               								.setTitle("提示") 
               								.setMessage("存在同名词库，确定覆盖吗？")
               								.setPositiveButton("确定", new DialogInterface.OnClickListener(){
               									public void onClick(DialogInterface dialog, int whichButton) { 
               										setResult(RESULT_OK);//确定按钮事件 
               										sourcefile.renameTo(targetfile);
               										Toast.makeText(LexiconList.this,"词库重命名成功！", Toast.LENGTH_SHORT).show();                						               						
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
               								sourcefile.renameTo(targetfile);
                  	          				Toast.makeText(LexiconList.this,"词库重命名成功！", Toast.LENGTH_SHORT).show();                						               						
                  	          				dialog1.dismiss();
                  	          				onCreate(savedInstanceState);
               							}
               						}
               					}
               				});              				
               				break;
                	   	}                          
                    }}) ; 
					builder.show();  				
				}       	
        	});               			
        }
	}
    
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
    
