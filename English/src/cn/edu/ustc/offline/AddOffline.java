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
        	Toast.makeText(this,"SD¿¨²»´æÔÚ£¡",Toast.LENGTH_SHORT ).show();
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
			
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}