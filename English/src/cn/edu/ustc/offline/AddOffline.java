package cn.edu.ustc.offline;

import cn.edu.ustc.main.FileOperate;
import cn.edu.ustc.main.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AddOffline extends Activity {

	private String[] fileNames;
	private File[] currentFiles;
	private File currentParent;
	private ListView sdcardList;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.offline_add);
    	sdcardList = (ListView) findViewById(R.id.showlistoffline);
       
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	File f = new File("/mnt/sdcard/");
        	currentParent=f;
        	currentFiles = f.listFiles(); 
        	showListFiles(currentFiles);
        }
        else{
        	Toast.makeText(this,"SD�������ڣ�",Toast.LENGTH_SHORT ).show();
        }
        
    }
      
    private void showListFiles(File[] file){   	
    	fileNames = new String[file.length];
    	for (int i=0;i<fileNames.length;i++){
    		fileNames[i] = file[i].getName();
    	}
    	sdcardList.setAdapter(new SimpleAdapter(this, getData(), R.layout.common_listitem,   
                new String[]{"img", "text"},   
                new int[]{R.id.list_img, R.id.list_row}));
    	sdcardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onListItemClick(arg2);
			}
		});
	}
    
    private List<Map<String, Object>> getData() {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();                
        for(int i = 0; i < fileNames.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            if(currentFiles[i].isFile()){
            	if(currentFiles[i].getName().toLowerCase().endsWith(".xml")){
	            	map.put("text",fileNames[i]);  
	            	map.put("img", R.drawable.xml_logo);  
            	}
            	else if(currentFiles[i].getName().toLowerCase().endsWith(".txt")){
            		map.put("text",fileNames[i]);  
                	map.put("img", R.drawable.txt_logo);
            	}
            	else{
            		map.put("text",fileNames[i]);  
                	map.put("img", R.drawable.other_logo);
            	}
            } else{
            	map.put("text",fileNames[i]);  
            	map.put("img", R.drawable.file_logo);                     	
            }
            list.add(map);             
        }           
        return list;  
    } 
    
	class Add_File {
		public Add_File(File soucrfile,File targetfile){	
			FileOperate fo = new FileOperate();
			try {
				fo.copyFile(soucrfile,targetfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onListItemClick(final int position) {
		class Add_Success implements OnClickListener{
			File soucrfile,targetfile;
			public Add_Success(File soucrfile,File targetfile){
				this.soucrfile=soucrfile;
				this.targetfile=targetfile;
			}
			public void onClick(DialogInterface dialog, int which) {				
				new Add_File(soucrfile,targetfile);
				new AlertDialog.Builder(AddOffline.this) ;
				Toast.makeText(AddOffline.this,"���ĵ���ɹ���",Toast.LENGTH_SHORT ).show();
        		Intent intent=new Intent();
        		intent.setClass( AddOffline.this,OfflineList.class);
        		startActivity(intent);
        		AddOffline.this.finish();       		
			}
		}
		
		//���ļ���������xml�ļ����ܵ���
		if(currentFiles[position].isFile() && currentFiles[position].getName().toLowerCase().endsWith(".txt")){
			new AlertDialog.Builder(AddOffline.this) 
	        .setTitle("��ʾ") 
	        .setMessage("ȷ�������ƪ������") 
	        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
	        	public void onClick(DialogInterface dialog, int whichButton) { 
	        		setResult(RESULT_OK);//ȷ����ť�¼� 
	        		Context cont = AddOffline.this.getApplicationContext();
	        		String fileroot = cont.getFilesDir().getAbsolutePath()+"/offline/";
	        		File targetfile = new File(fileroot + currentFiles[position].getName());
	        		File sourcefile = currentFiles[position];
	    			//String filepath = currentFiles[position].getAbsolutePath();
	    			if(targetfile.exists()){
	    				new AlertDialog.Builder(AddOffline.this)
	    				.setTitle("��ʾ") 
	    				.setMessage("�ö����Ѵ��ڣ�ȷ��������")
	    				.setPositiveButton("ȷ��",new Add_Success(sourcefile,targetfile))	    				
	    				.setNegativeButton("ȡ��",new OnClickListener(){
	    					public void onClick(DialogInterface dialog, int whichButton) {}})
	      	          	.show();
	    			}
	    			else {
	    				new Add_File(sourcefile,targetfile);	
	    				Toast.makeText(AddOffline.this,"�����µ���ɹ���",Toast.LENGTH_SHORT ).show();
	            		Intent intent=new Intent();
	            		//intent.putExtra("filepath", filepath);	         	            		
	            		intent.setClass( AddOffline.this,OfflineList.class);
	            		startActivity(intent);
	            		AddOffline.this.finish(); 
	    			}
	        	}
	        }) 	          	
	        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
	          	public void onClick(DialogInterface dialog, int whichButton) { 
	        		 //ȡ����ť�¼� 
	          		} 
	        }) 
	        .show();							
		}
		else{
			if(currentFiles[position].isDirectory()){
				File[] temp=currentFiles[position].listFiles();//��ȡ�û�������ļ��� �µ������ļ� 
				if(temp==null||temp.length==0){
					Toast.makeText(this,"�ļ���Ϊ�գ���·�����ɷ��ʣ�",Toast.LENGTH_SHORT ).show();
				}
				else{
					currentParent=currentFiles[position];
					currentFiles=temp;
					showListFiles(temp);
				}
			}
			else	
				Toast.makeText(AddOffline.this, "�ļ���ʽ����ֻ��Ϊtxt�ļ�", Toast.LENGTH_SHORT).show();
		}		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!currentParent.toString().equals("/mnt/sdcard")){
    			currentParent=currentParent.getParentFile();
    			currentFiles=currentParent.listFiles();
    			showListFiles(currentFiles);
    			return false;	//�ɱ�֤�䲻��ֱ�ӷ�����һ��activity
			}
			else{
				Intent intent=new Intent();
     			intent.setClass(AddOffline.this,OfflineList.class);
     			startActivity(intent);
    			AddOffline.this.finish();
    		}	
		}
		return super.onKeyDown(keyCode, event);
	}
}