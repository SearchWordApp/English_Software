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
			XmlPullParser xmlPullParser = pullParserFactory.newPullParser(); 
			InputStream inputStream = new FileInputStream(new File(path)); 
			xmlPullParser.setInput(inputStream, "UTF-8"); 
			int eventType = xmlPullParser.getEventType();
			
			boolean found = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName=xmlPullParser.getName();
                switch (eventType) {
            
                case XmlPullParser.START_DOCUMENT:
                    break;
               
                case XmlPullParser.START_TAG:
                    
                    if("word".equals(nodeName)){
                       
                    	String wt = xmlPullParser.nextText().trim();
                    	if(wordSearch.equals(wt)){
                    		word=new Word();
                    		word.setWord(wt);
                    		found = true;
                    	}                      
                    }else if("trans".equals(nodeName)&& found ){
                    	word.setTrans(xmlPullParser.nextText());
                    }/**else if("phonetic".equals(nodeName) && found){
                        word.setPhonetic(xmlPullParser.nextText());
                    }*/else if("tags".equals(nodeName) && found){
                        word.setTags(xmlPullParser.nextText());
                    }
                    break;
                
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
	


