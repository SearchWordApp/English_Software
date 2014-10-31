package cn.edu.ustc.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import cn.edu.ustc.main.Word;

public class XmlPull{

	private Word word ;
	private List<Word> list;
	private int count=0;
	private String TAG = null;
	private InputStream inputStream;
	private XmlPullParserFactory pullParserFactory;
	private  XmlPullParser xmlPullParser;
	private int eventType;
	
	public XmlPull(String path) throws Exception{
		pullParserFactory = XmlPullParserFactory.newInstance();
		//获取XmlPullParser的实例
		xmlPullParser = pullParserFactory.newPullParser();
		//设置输入流  xml文件
		//设置输入流为 文件路径，解析文件
		inputStream = new FileInputStream(new File(path));
		//inputStream = new FileInputStream(new File(path));
		xmlPullParser.setInput(inputStream, "UTF-8");
		eventType=xmlPullParser.getEventType();
	}
	
	public XmlPull(String path,int courceSize,int cource) throws Exception{
		pullParserFactory = XmlPullParserFactory.newInstance();
		//获取XmlPullParser的实例
		xmlPullParser = pullParserFactory.newPullParser();
		//设置输入流  xml文件
		//设置输入流为 文件路径，解析文件
		inputStream = new FileInputStream(new File(path));
		xmlPullParser.setInput(inputStream, "UTF-8");
		eventType=xmlPullParser.getEventType();
	}

	public int getWordCount() throws Exception {
		while (eventType != XmlPullParser.END_DOCUMENT) { // 不是文档结束

			if (eventType == XmlPullParser.START_DOCUMENT) { // 文档解析开始
				count = 0;
			} else if (eventType == XmlPullParser.START_TAG) { // 是属性开始标签
				TAG = xmlPullParser.getName(); // 获取事件标记
				if ("item".equals(TAG)) { // 单词word属性
					count++;
				}
			} else if (eventType == XmlPullParser.END_TAG) { // 是属性结束标签
				TAG = null;// 将标记的元素名称清空，否则程序会报错的。

			}
			eventType = xmlPullParser.next(); // 获取下一个标签
		}
		return count;
	}
	
	public List<Word> getWords(){		
		 while(eventType != XmlPullParser.END_DOCUMENT){		//不是文档结束        	
         	if(eventType == XmlPullParser.START_DOCUMENT){		//文档解析开始
         		list = new ArrayList<Word>();
         	}else if(eventType == XmlPullParser.START_TAG){		//是属性开始标签
         		TAG = xmlPullParser.getName();		//	获取事件标记
         		if("item".equals(TAG)){		//	单词word属性
         			word = new Word();
         		}
         	}
         	else if(eventType == XmlPullParser.TEXT){		//是文档文本标签
         		if("word".equals(TAG)){
         			word.setWord(xmlPullParser.getText());
         		}
         		else if("phonetic".equals(TAG)){
         			word.setPhonetic(xmlPullParser.getText());
         		}
         		else if("trans".equals(TAG)){
         			word.setTrans(xmlPullParser.getText());
         		}
         		else if("tags".equals(TAG)){
         			word.setTags(xmlPullParser.getText());
         		}
         		
         	}else if(eventType == XmlPullParser.END_TAG){		//是属性结束标签
                 TAG=xmlPullParser.getName();                 
                 if ("item".equals(TAG)) {  
                	list.add(word);                     
                 	word =null;//清空对象                                       
                 	}                 
                 TAG=null;//将标记的元素名称清空，否则程序会报错的。
                 
         	}
         	try {
				eventType = xmlPullParser.next();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		//获取下一个标签				
         }
		return list;
	}

	public Word getWord(){		
		 while(eventType != XmlPullParser.END_DOCUMENT){		//不是文档结束        	
        	if(eventType == XmlPullParser.START_DOCUMENT){		//文档解析开始
        	
        	}else if(eventType == XmlPullParser.START_TAG){		//是属性开始标签
        		TAG = xmlPullParser.getName();		//	获取事件标记
        		if("item".equals(TAG)){		//	单词word属性
        			word = new Word();
        		}
        	}
        	else if(eventType == XmlPullParser.TEXT){		//是文档文本标签
        		if("word".equals(TAG)){
        			word.setWord(xmlPullParser.getText());
        		}
        		else if("phonetic".equals(TAG)){
        			word.setPhonetic(xmlPullParser.getText());
        		}
        		else if("trans".equals(TAG)){
        			word.setTrans(xmlPullParser.getText());
        		}
        		else if("tags".equals(TAG)){
        			word.setTags(xmlPullParser.getText());
        		}
        		
        	}else if(eventType == XmlPullParser.END_TAG){		//是属性结束标签
                TAG=xmlPullParser.getName();                 
                if ("item".equals(TAG)) {               
                	return word;
                	}   
                TAG=null; 
        	}
        	try {
				eventType = xmlPullParser.next();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		//获取下一个标签				
        }
		return word;
	}
}
