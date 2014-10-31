package cn.edu.ustc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class XmlPullSearch{
	
	private String path;
	private String wordSearch;
	private Word word;
	
	public XmlPullSearch(String path, String wordSearch){
		this.path = path;
		this.wordSearch = wordSearch;
	}
	
	public Word getWord() {
		
		try {
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser xmlPullParser = pullParserFactory.newPullParser(); // 获取XmlPullParser的实例
			InputStream inputStream = new FileInputStream(new File(path)); // 设置输入流 xml文件
			xmlPullParser.setInput(inputStream, "UTF-8"); // 设置输入流为 文件路径，解析文件
			int eventType = xmlPullParser.getEventType();
			/*
			<item>    
				<word>cupboard</word>
	    		<trans><![CDATA[n. 食橱；碗柜]]></trans>
	    		<phonetic><![CDATA[['kʌbəd]]]></phonetic>
	    		<tags>CET4-EASY</tags>
			</item>*/
			boolean found = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName=xmlPullParser.getName();
                switch (eventType) {
                //文档开始
                case XmlPullParser.START_DOCUMENT:
                    break;
                //开始节点
                case XmlPullParser.START_TAG:
                    //判断如果其实节点为word
                    if("word".equals(nodeName)){
                        //实例化word对象
                    	String wt = xmlPullParser.nextText().trim();
                    	if(wordSearch.equals(wt)){
                    		word=new Word();
                    		word.setWord(wt);
                    		found = true;
                    	}                      
                    }else if("trans".equals(nodeName)&& found ){
                    	word.setTrans(xmlPullParser.nextText());
                    }else if("phonetic".equals(nodeName) && found){
                        word.setPhonetic(xmlPullParser.nextText());
                    }else if("tags".equals(nodeName) && found){
                        word.setTags(xmlPullParser.nextText());
                    }
                    break;
                //结束节点
                case XmlPullParser.END_TAG:
                    if("item".equals(nodeName) && found){
                        return word; 
                    }
                    break;
                default:
                    break;
                }
                eventType=xmlPullParser.next();
            }
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}		
}
	


