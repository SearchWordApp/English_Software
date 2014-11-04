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
		/*<book>
			<item>    
				<word>cupboard</word>
				<trans><![CDATA[n. 食橱；碗柜]]></trans>
				<phonetic><![CDATA[['kʌbəd]]]></phonetic>
				<tags>CET4-EASY</tags>
			</item>
		</book>*/
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
			Element phonetic = document.createElement("phonetic");  
			phonetic.setTextContent(phonAdd);
			Element tags = document.createElement("tags");
			tags.setTextContent(tagsAdd);
			item.appendChild(word);
			item.appendChild(trans);
			item.appendChild(phonetic);
			item.appendChild(tags);
			root.appendChild(item);
						
			 // 此时真正的处理将新数据添加到文件中 磁盘
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer tfer = tf.newTransformer();
			DOMSource ds = new DOMSource(document);
			StreamResult sr = new StreamResult(new File(path));
			tfer.transform(ds, sr);			
		} catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void delNode(String wordDel, String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(path));
			NodeList list = document.getElementsByTagName("word");  
			for (int i = 0; i < list.getLength(); i++) {
				String value = list.item(i).getFirstChild()  
                        .getTextContent();  
                if (wordDel != null && wordDel.equalsIgnoreCase(value)) {  
                    Node parentNode = list.item(i).getParentNode();  
                    document.getFirstChild().removeChild(parentNode);  
                    break;
                }  
			}
			TransformerFactory tsf = TransformerFactory.newInstance();
			Transformer tf = tsf.newTransformer();
			StreamResult result = new StreamResult(new File(path));
			DOMSource source = new DOMSource(document);
			tf.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateNode(String word, String trans, String path){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(path));
			NodeList list = document.getElementsByTagName("word");  
			for (int i = 0; i < list.getLength(); i++) {  
                String value = list.item(i).getTextContent();  
                if (word != null && word.equalsIgnoreCase(value)) {  
                    Node parentNode = list.item(i).getParentNode();  
                    NodeList nl = parentNode.getChildNodes();  
                    for (int j = 0; j < nl.getLength(); j++) {  
                        String modifyNode = nl.item(j).getNodeName();  
                        if (modifyNode.equalsIgnoreCase("trans")) {  
                            nl.item(j).setTextContent(trans);  
                            break;
                        }  
                    }  
                    break;
                }  
            }  	
			TransformerFactory tsf = TransformerFactory.newInstance();
			Transformer tf = tsf.newTransformer();
			StreamResult result = new StreamResult(new File(path));
			DOMSource source = new DOMSource(document);
			tf.transform(source, result);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
