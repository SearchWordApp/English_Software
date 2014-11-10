package cn.edu.ustc.lexicon;

import cn.edu.ustc.main.DOMForXML;
import cn.edu.ustc.main.R;
import cn.edu.ustc.main.Word;
import cn.edu.ustc.main.XmlPullSearch;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordInfo extends Activity {
		
	private String word_old;
	private String trans_old;
	private String word_add;
	private String trans_add;
	private String word_update;
	private String trans_update;
	private String word_search;
	private String word_del;
	private String filepath;
	private Word word;
	private TextView textViewWordInfo;	
	private ProgressDialog progressDialog;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lexicon_wordinfo);
        textViewWordInfo=(TextView) findViewById(R.id.textViewWordInfo);
        textViewWordInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        Intent intent=getIntent(); 
        String lexicon=intent.getStringExtra("lexicon"); 
        filepath=intent.getStringExtra("filepath");
        TextView title=(TextView) findViewById(R.id.textViewLexicon);
        title.setText(lexicon);
        
        //Toast.makeText(Word_Info.this,filepath,Toast.LENGTH_SHORT ).show();
		
        Button button_add=(Button) findViewById(R.id.button_add);
        button_add.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		final Dialog dialog=new Dialog(WordInfo.this);
    			dialog.setTitle("添加新单词");
    			dialog.setContentView(R.layout.lexicon_addword_dialog);
    			dialog.show();
    			final EditText editTextWord=(EditText) dialog.findViewById(R.id.editTextWord);
    			final EditText editTextChinese=(EditText) dialog.findViewById(R.id.editTextChinese);
    			
    			Button confirm=(Button) dialog.findViewById(R.id.attention_confirm_button);
    			Button cancel=(Button) dialog.findViewById(R.id.attention_cancel_button);
    			cancel.setOnClickListener(new OnClickListener(){  				
    				public void onClick(View v){
    					dialog.cancel();
    				}   				
    			});
   			
    			confirm.setOnClickListener(new OnClickListener(){
    				XmlPullSearch xps;
					public void onClick(View v) {
						//注意需在click事件中获取editText中的内容！
						word_add = editTextWord.getText().toString().trim();
		    			trans_add = editTextChinese.getText().toString().trim();
						if(word_add.equals("")||trans_add.equals("")){
							Toast.makeText(WordInfo.this,"输入不能为空！",Toast.LENGTH_SHORT ).show();
						}
						else{
							xps = new XmlPullSearch( filepath,word_add);
							//Toast.makeText(WordInfo.this,filepath,Toast.LENGTH_SHORT ).show();
							//此处将新单词保存到xml文件中
							if(xps.getWord()!=null){
								new AlertDialog.Builder(WordInfo.this) 
						         .setTitle("提示") 
						         .setMessage("该单词已存在，添加失败！") 
						         .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
						        	 public void onClick(DialogInterface dialog, int whichButton) { 
						        		 setResult(RESULT_OK);//确定按钮事件 
						        	 } 
						         }).show();
							}
							else{
								progressDialog = ProgressDialog.show(WordInfo.this, "", "正在添加，请稍后...", true, false);
    							new Thread(){
    								public void run(){
    									DOMForXML dfx = new DOMForXML();						
    									dfx.addNewNode(word_add, trans_add, "自定义", "自定义", filepath);	
    									Message msg = new Message();  									
    		  	          				msg.what = 3;
    		  	          				handler.sendMessage(msg);    									
    								}
    							}.start();
							}
							dialog.cancel();
						}		
					}	
    			});
        	}
        });
       
        Button button_Modify=(Button) findViewById(R.id.button_Modify);
        button_Modify.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
    			if(word == null){
    				Toast.makeText(WordInfo.this,"当前没有可编辑单词！",Toast.LENGTH_SHORT ).show();					
    			}
    			else{ 			
    				word_old = word.getWord();  
        			trans_old = word.getTrans();
    				final Dialog dialog=new Dialog(WordInfo.this);
    				dialog.setTitle("修改单词信息");
    				dialog.setContentView(R.layout.lexicon_addword_dialog);
    				dialog.show();   				
    			
    				EditText editTextWord=(EditText) dialog.findViewById(R.id.editTextWord);
    				editTextWord.setText(word_old);
    				editTextWord.setFocusable(false);  
    				editTextWord.setClickable(false);			
    				EditText editTextChinese=(EditText) dialog.findViewById(R.id.editTextChinese);
    				editTextChinese.setText(trans_old);  
    				
    				Button confirm=(Button) dialog.findViewById(R.id.attention_confirm_button);
    				Button cancel=(Button) dialog.findViewById(R.id.attention_cancel_button);
    				cancel.setOnClickListener(new OnClickListener(){  				
    					public void onClick(View v){
    						dialog.cancel();
    					}   				
    				});
   			
    				confirm.setOnClickListener(new OnClickListener(){
    					EditText editTextWord=(EditText) dialog.findViewById(R.id.editTextWord);
    					EditText editTextChinese=(EditText) dialog.findViewById(R.id.editTextChinese);
        				   					
    					public void onClick(View v) {
    						//注意需在click事件中获取editText中的内容！
    						word_update = editTextWord.getText().toString().trim();
            				trans_update = editTextChinese.getText().toString();
            				
    						if(word_update.equals("")||trans_update.trim().equals("")){
    							Toast.makeText(WordInfo.this,"输入不能为空！",Toast.LENGTH_SHORT ).show();
    						}
    						else{
    							//此处将新信息保存到xml文件中
    							dialog.cancel();
    							progressDialog = ProgressDialog.show(WordInfo.this, "", "正在更新，请稍后...", true, false);
    							new Thread(){
    								public void run(){
    									DOMForXML dfx = new DOMForXML(); 
    									//Toast.makeText(WordInfo.this,word_update,Toast.LENGTH_SHORT ).show();   							
    									//Toast.makeText(WordInfo.this,trans_update,Toast.LENGTH_SHORT ).show();
    									dfx.updateNode(word_update, trans_update, filepath);
    									Message msg = new Message();  									
    		  	          				msg.what = 2;
    		  	          				handler.sendMessage(msg); 							
    								}
    							}.start();
    						}						
    					}   				
    				});
    			}
        	}      	
        });
        
        Button button_Delete=(Button) findViewById(R.id.button_Delete);
        button_Delete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){      		
        		if(word == null){
        			Toast.makeText(WordInfo.this,"当前没有可删除单词！",Toast.LENGTH_SHORT ).show();
        		}
        		else{
        			word_del = word.getWord(); 
        			new AlertDialog.Builder(WordInfo.this) 
  	          		.setTitle("提示") 
  	          		.setMessage("确定删除该单词吗？") 
  	          		.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
  	          			public void onClick(DialogInterface dialog, int whichButton) {      				
  	          				//此处添加删除单词代码,删除word	
	  	          			progressDialog = ProgressDialog.show(WordInfo.this, "", "正在删除，请稍后...", true, false);
	  	          			new Thread(){
	  	          				public void run(){
		  	          				DOMForXML dfx = new DOMForXML();
		  	          				dfx.delNode(word_del, filepath);
		  	          				word = null; 
		  	          				Message msg = new Message();
		  	          				msg.what = 1;
		  	          				handler.sendMessage(msg);   
	  	          				}
	  	          			}.start();
  	          			}	          		
  	          		}) 
  	          		.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
  	          			public void onClick(DialogInterface dialog, int whichButton) { 
  	          				dialog.cancel();//取消按钮事件 
  	          			} 
  	          		}) 
  	          		.show();
        		}
        	}
        });
        
        Button button_search=(Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new Button.OnClickListener(){      	
        	public void onClick(View v){
        		EditText editTextWord = (EditText) findViewById(R.id.editTextWord);        		
        		word_search = editTextWord.getText().toString().trim();
        		if(word_search.trim().equals("")){
        			Toast.makeText(WordInfo.this,"输入不能为空！",Toast.LENGTH_SHORT ).show();    			
        		}      		
        		else{   
        			//Toast.makeText(WordInfo.this,filepath,Toast.LENGTH_SHORT ).show();     			
        			/*<wordbook>
        				<item>    
        					<word>cupboard</word>
        		    		<trans><![CDATA[n. 食橱；碗柜]]></trans>
        		    		<phonetic><![CDATA[['kʌbəd]]]></phonetic>
        		    		<tags>CET4-EASY</tags>
        				</item>
        			</wordbook>*/
        			
        			//此处显示单词信息代码，根据输入信息
        			progressDialog = ProgressDialog.show(WordInfo.this, "", "正在查询，请稍后...", true, false);
        			new Thread(){
        				public void run() {
        					XmlPullSearch xml = null;
        					try {
        						xml = new XmlPullSearch(filepath, word_search);
        					} catch (Exception e) {
        						e.printStackTrace();
        					}
        					word = xml.getWord();
        					//Toast.makeText(WordInfo.this, wordSearch, Toast.LENGTH_SHORT)
        						//	.show();
        					Message msg = new Message();
        					msg.what = 0;
        					handler.sendMessage(msg);   
        				}
        			}.start();
				}
			}		
    	});
    }

