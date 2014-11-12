package cn.edu.ustc.main;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DOMForXML {
	public void addNewNode(String wordAdd,String transAdd, String phonAdd, String tagsAdd, String path){		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();        
			DocumentBuilder builder = factory.newDocumentBuilder();         
			Document document = builder.parse(new File(path));  
			
			// 添加节点
			Element root = document.getDocumentElement(); 			
			Element item = document.createElement("item");  
			Element word = document.createElement("word");  
			word.setTextContent(wordAdd);
			Element trans = document.createElement("trans");  
			trans.setTextContent(transAdd);
			Element tags = document.createElement("tags");
			tags.setTextContent(tagsAdd);
			item.appendChild(word);
			item.appendChild(trans);
			item.appendChild(tags);
			root.appendChild(item);
						
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer tfer = tf.newTransformer();
			DOMSource ds = new DOMSource(document);
			StreamResult sr = new StreamResult(new File(path));
			tfer.transform(ds, sr);			
		} catch(Exception e){
			e.printStackTrace();
		}		
	}
	