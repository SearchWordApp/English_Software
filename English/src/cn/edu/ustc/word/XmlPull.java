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
		//��ȡXmlPullParser��ʵ��
		xmlPullParser = pullParserFactory.newPullParser();
		//����������  xml�ļ�
		//����������Ϊ �ļ�·���������ļ�
		inputStream = new FileInputStream(new File(path));
		//inputStream = new FileInputStream(new File(path));
		xmlPullParser.setInput(inputStream, "UTF-8");
		eventType=xmlPullParser.getEventType();
	}
	
	public XmlPull(String path,int courceSize,int cource) throws Exception{
		pullParserFactory = XmlPullParserFactory.newInstance();
		//��ȡXmlPullParser��ʵ��
		xmlPullParser = pullParserFactory.newPullParser();
		//����������  xml�ļ�
		//����������Ϊ �ļ�·���������ļ�
		inputStream = new FileInputStream(new File(path));
		xmlPullParser.setInput(inputStream, "UTF-8");
		eventType=xmlPullParser.getEventType();
	}

	public int getWordCount() throws Exception {
		while (eventType != XmlPullParser.END_DOCUMENT) { // �����ĵ�����

			if (eventType == XmlPullParser.START_DOCUMENT) { // �ĵ�������ʼ
				count = 0;
			} else if (eventType == XmlPullParser.START_TAG) { // �����Կ�ʼ��ǩ
				TAG = xmlPullParser.getName(); // ��ȡ�¼����
				if ("item".equals(TAG)) { // ����word����
					count++;
				}
			} else if (eventType == XmlPullParser.END_TAG) { // �����Խ�����ǩ
				TAG = null;// ����ǵ�Ԫ��������գ��������ᱨ��ġ�

			}
			eventType = xmlPullParser.next(); // ��ȡ��һ����ǩ
		}
		return count;
	}
	
	public List<Word> getWords(){		
		 while(eventType != XmlPullParser.END_DOCUMENT){		//�����ĵ�����        	
         	if(eventType == XmlPullParser.START_DOCUMENT){		//�ĵ�������ʼ
         		list = new ArrayList<Word>();
         	}else if(eventType == XmlPullParser.START_TAG){		//�����Կ�ʼ��ǩ
         		TAG = xmlPullParser.getName();		//	��ȡ�¼����
         		if("item".equals(TAG)){		//	����word����
         			word = new Word();
         		}
         	}
         	else if(eventType == XmlPullParser.TEXT){		//���ĵ��ı���ǩ
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
         		
         	}else if(eventType == XmlPullParser.END_TAG){		//�����Խ�����ǩ
                 TAG=xmlPullParser.getName();                 
                 if ("item".equals(TAG)) {  
                	list.add(word);                     
                 	word =null;//��ն���                                       
                 	}                 
                 TAG=null;//����ǵ�Ԫ��������գ��������ᱨ��ġ�
                 
         	}
         	try {
				eventType = xmlPullParser.next();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		//��ȡ��һ����ǩ				
         }
		return list;
	}

	public Word getWord(){		
		 while(eventType != XmlPullParser.END_DOCUMENT){		//�����ĵ�����        	
        	if(eventType == XmlPullParser.START_DOCUMENT){		//�ĵ�������ʼ
        	
        	}else if(eventType == XmlPullParser.START_TAG){		//�����Կ�ʼ��ǩ
        		TAG = xmlPullParser.getName();		//	��ȡ�¼����
        		if("item".equals(TAG)){		//	����word����
        			word = new Word();
        		}
        	}
        	else if(eventType == XmlPullParser.TEXT){		//���ĵ��ı���ǩ
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
        		
        	}else if(eventType == XmlPullParser.END_TAG){		//�����Խ�����ǩ
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
			}		//��ȡ��һ����ǩ				
        }
		return word;
	}
}
