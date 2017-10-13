package console;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import RNG.RNG;

public class MessageFactory {
	
	//TODO guardar mensajes en un map con el metodo load
	
	private final static String DOC_PATH = "../core/assets/Data/Messages.xml";
	private static Document messagesDoc;
	
	private static XPath XPath;
	
	static{
		XPath = XPathFactory.newInstance().newXPath();
		
		try {
			messagesDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(DOC_PATH));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println(e);
		}
	}
	
	public static void createMessage(String message){
		Console.add(new Message(message));
	}
	
	public static void loadMessage(String type){
		String message = searchMessage(type);
		Console.add(new Message(message));
	}
	
	public static void loadMessage(String type, String[] extraText){
		String message = searchMessage(type);
		
		//Reemplazar tags
		for (int i = 0; i < extraText.length; i++){
			String tag = "/" + i + "/";
			if(message.contains(tag)){
				message = message.replace(tag, extraText[i]);
			}
		}
		Console.add(new Message(message));
	}
	
	private static String searchMessage(String type){
		XPathExpression expr = null;
		String message = null;
		String path = "//Message[Type = '" + type + "']/Text";
		try {
			expr = XPath.compile(path);
			NodeList list = (NodeList) expr.evaluate(messagesDoc, XPathConstants.NODESET);
			Node messageNode = list.item(RNG.nextInt(list.getLength()));
			message = messageNode.getTextContent();
		} catch (XPathExpressionException e) {
			Console.add(new Message("[GREEN]Mensaje fallido[]" + type));
			return message;
		}
		return message;
	}
	
}
