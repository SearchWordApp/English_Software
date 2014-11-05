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
                	builder.setTitle("��ʾ");                  	
                	builder.setMessage("SD�������ڣ�");
                	builder.setPositiveButton("ȷ��", null);
                	builder.show();  
                }        		 
        	}
        });
        
        button_add.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		final Dialog dialog1=new Dialog(LexiconList.this);
   				dialog1.setTitle("������ʿ�����");
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
   							Toast.makeText(LexiconList.this,"���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();                 	          			
   						}
   						else{
   							final String targetfilepath = fileroot + newfilename +".xml";
   							final File targetfile = new File(targetfilepath);
   							if(targetfile.exists()){
   								new AlertDialog.Builder(LexiconList.this)
   								.setTitle("��ʾ") 
   								.setMessage("����ͬ���ʿ⣬ȷ��������")
   								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
   									public void onClick(DialogInterface dialog, int whichButton) { 
   										setResult(RESULT_OK);//ȷ����ť�¼� 
   										
   										//�˴�ʵ���½��ʿ⹦��
   										FileOperate fo = new FileOperate();
   										fo.deleteFile(targetfilepath);
   										fo.newFile(targetfilepath);  										
   										Toast.makeText(LexiconList.this,"�½��ʿ�ɹ���", Toast.LENGTH_SHORT).show();  
   										dialog1.dismiss();
   										onCreate(savedInstanceState);
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
   								//�˴�ʵ���½��ʿ⹦��
   								FileOperate fo = new FileOperate();
								fo.newFile(targetfilepath);  										
								Toast.makeText(LexiconList.this,"�½��ʿ�ɹ���", Toast.LENGTH_SHORT).show();  
								dialog1.dismiss();
								onCreate(savedInstanceState);
							}						
   						}	          				
   					}      	
   				});
        	}
        });
        
        //ʵ����GridView
        GridView mGridView=(GridView) findViewById(R.id.gridview);
        // ���ɶ�̬���飬���Ҵ�������
        
        //�˴���Ҫ���ݴʿ�����ȷ��forѭ����ֵ
        //Ԥ�ȶ�ȡ���дʿ����ƣ��ŵ�һ��string�����У��Ա�����forѭ��ʱ���ItemText�����ʿ�����
        lstImageItem = new ArrayList<HashMap<String, Object>>();
        cont = this.getApplicationContext();
        fileroot = cont.getFilesDir().getAbsolutePath()+"/lexicon/";
        		
		File filedir = new File(fileroot);
		if(!filedir.exists())
			filedir.mkdir();
		if(filedir.isDirectory()){
			File[] filelist = filedir.listFiles();
    	if(filelist.length==0)
			Toast.makeText(this,"��ǰ�ʿ�Ϊ�գ�������Ӵʿ⣡",Toast.LENGTH_SHORT ).show();
        
        for (int i = 0; i < filelist.length; i++) {
            map = new HashMap<String, Object>();         
            map.put("ItemImage", R.drawable.lexicon_logo);	//���ͼ����Դ��ID
            map.put("filename", filelist[i].getName());
            lstImageItem.add(map);
        }
        //����һ��������
        SimpleAdapter simple = new SimpleAdapter(this, lstImageItem,
                R.layout.common_gridviewitem,
                new String[] { "ItemImage", "filename" }, 
                new int[] { R.id.ItemImage, R.id.ItemText });
        mGridView.setAdapter(simple);
        
        //���ѡ��������¼�
        mGridView.setOnItemClickListener(new GridView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				//���ȸ��ݵ��Ŀ���ȡ�ʿ����ƣ��ٽ��ж�Ӧ����
				filepath = (String) (fileroot + (String)lstImageItem.get(position).get("filename"));
				sdcardpath = (String)(Environment.getExternalStorageDirectory()+"/"+(String)lstImageItem.get(position).get("filename"));
				
				AlertDialog.Builder builder = new Builder(LexiconList.this); 
				builder.setTitle("ѡ�����"); 
				builder.setItems(new String[] {"����ʿ�","�����ʿ�","ɾ���ʿ�","�������ʿ�"},
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
                		   //�˴�ʵ�ִʿ⵼������
                		   final File sourcefile = new File(filepath);
                		   final File targetfile = new File(sdcardpath);
                		   if(targetfile.exists()){
                			   new AlertDialog.Builder(LexiconList.this)
              					.setTitle("��ʾ") 
              					.setMessage("sd���д���ͬ���ʿ⣬ȷ��������")
              					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog,int which) {
										FileOperate fo = new FileOperate();
						   				try {
						   					fo.copyFile(sourcefile,targetfile);
						   					
						   				} catch (IOException e) {}
						   				new AlertDialog.Builder(LexiconList.this) 
						          		.setTitle("��ʾ") 
						          		.setMessage("�����ɹ����ʿ��ļ�λ�ڣ�\n"+sdcardpath) 
						          		.setPositiveButton("ȷ��",null)
						          		.show();
									}             						
              					})
              					.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
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
				          		.setTitle("��ʾ") 
				          		.setMessage("�����ɹ��� �ʿ��ļ�λ�ڣ�\n "+sdcardpath) 
				          		.setPositiveButton("ȷ��",null)
				          		.show();
                		   }
              				break;
               				
                	   case 2:
                		   	new AlertDialog.Builder(LexiconList.this) 
              	          	.setTitle("��ʾ") 
              	          	.setMessage("ȷ��ɾ���ôʿ���") 
              	          	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
              	          		public void onClick(DialogInterface dialog, int whichButton) { 
              	          			setResult(RESULT_OK);//ȷ����ť�¼� 
              	          			
              	          			//�˴����ɾ���ʿ����
              	          			File file = new File( filepath);
              	          			file.delete();
               						Toast.makeText(LexiconList.this,"�ʿ�ɾ���ɹ���", Toast.LENGTH_SHORT).show();               						
               						onCreate(savedInstanceState);
              	          		}								
              	          	}) 
              	          	
              	          	.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
              	          		public void onClick(DialogInterface dialog, int whichButton) { 
              	        		 //ȡ����ť�¼� 
              	          		} 
              	          	}) 
              	          	.show();
                		   	break;
                		   	
                	   case 3:               		   
                		    final Dialog dialog1=new Dialog(LexiconList.this);
               				dialog1.setTitle("������������");
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
               						//�˴�ʵ���������Ĺ��ܣ�newName��Ϊ������
               						newfilename = newName.getText().toString().trim();
               						if(newfilename.trim().equals("")){
               							Toast.makeText(LexiconList.this,"���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();                 	          			
               						}
               						else{
               							String targetfilepath = fileroot + newfilename +".xml";
               							final File targetfile = new File(targetfilepath);
               							final File sourcefile = new File(filepath);
               							if(targetfile.exists()){
               								new AlertDialog.Builder(LexiconList.this)
               								.setTitle("��ʾ") 
               								.setMessage("����ͬ���ʿ⣬ȷ��������")
               								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
               									public void onClick(DialogInterface dialog, int whichButton) { 
               										setResult(RESULT_OK);//ȷ����ť�¼� 
               										sourcefile.renameTo(targetfile);
               										Toast.makeText(LexiconList.this,"�ʿ��������ɹ���", Toast.LENGTH_SHORT).show();                						               						
               										dialog1.dismiss();
               										onCreate(savedInstanceState);
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
               								sourcefile.renameTo(targetfile);
                  	          				Toast.makeText(LexiconList.this,"�ʿ��������ɹ���", Toast.LENGTH_SHORT).show();                						               						
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
    
