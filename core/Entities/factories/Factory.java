package factories;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class Factory {
	
	protected static XStream xstream = initializeXStream();
	protected static Transformer t = initializeTransformer();
	protected static XPath XPath = XPathFactory.newInstance().newXPath();
	
	/**
	 * @param XMLString: el String que contiene la data de la entidad que se quiere crear
	 * @return una entidad creada según el XMLString
	 */
	protected static <T> Entity create(String XMLString) {
		@SuppressWarnings("unchecked")
		T[] p = (T[]) xstream.fromXML(XMLString);
		
		Entity e = Juego.ENGINE.createEntity();
		Pattern pattern = Pattern.compile("(?<=id=\")(\\s*.*)(?=\")");
		Matcher matcher = pattern.matcher(XMLString);
		matcher.find();
		e.flags = Integer.parseInt(matcher.group(0));
		for(int i = 0; i < p.length; i++) {
			e.add((Component) p[i]);
		}
		return e;
	}
	
	/**
	 * @param path: path del archivo XML con la data
	 * @return un map con <id, string xml de la entidad>
	 */
	protected static HashMap<Integer, String> loadEntities(String path){
		HashMap<Integer, String> entities = new HashMap<>();
		Document doc = loadDocument(path);
		NodeList nl = doc.getElementsByTagName("object-array");
		for(int i = 0; i < nl.getLength(); i++){
			Integer id = Integer.parseInt(((Element)nl.item(i)).getAttributes().getNamedItem("id").getTextContent());
			String entityString = nodeToString(nl.item(i));
			entities.put(id, entityString);
		}
		return entities;
	}
	
	protected static HashMap<String, String> makeMapWithNames(HashMap<Integer, String> entitiesMap){
		HashMap<String, String> entities = new HashMap<>();
		Pattern pattern = Pattern.compile("(?<=\\<name\\>)(\\s*.*)(?=\\<\\/name\\>)");
		for(String s : entitiesMap.values()) {
			Matcher matcher = pattern.matcher(s);
			matcher.find();
			String name = matcher.group(0);
			entities.put(name, s);
		}
		return entities;
	}
	
	private static Document loadDocument(String path) {
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(path));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println(e);
		}
		return doc;
	}
	
	private static XStream initializeXStream() {
		XStream xs = new XStream(new DomDriver());
		xs.autodetectAnnotations(true);
		return xs;
	}
	
	private static Transformer initializeTransformer() {
		Transformer tran = null;
		try {
			tran = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			System.out.println(e);
		}
		tran.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		tran.setOutputProperty(OutputKeys.INDENT, "yes");
		return tran;
	}
	
	/**
	 * @param node: nodo del archivo XML
	 * @return un String que representa al nodo dado como parámetro
	 */
	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException e) {}
		return sw.toString();
	}
	
//	/**
//	 * @param doc: el archivo XML en el que se busca
//	 * @return una lista con todos los Strings que representan los nodos de las entidades en el archivo
//	 */
//	private static List<String> findEntitiesXMLStrings(Document doc){
//		ArrayList<String> stringList = new ArrayList<>();
//		XPathExpression expr = null;
//		try {
//			expr = XPath.compile("//object-array");
//			NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//			
//			for(int i = 0; i < list.getLength(); i++) {
//				stringList.add(nodeToString(list.item(i)));
//			}
//		} catch (XPathExpressionException e) {}
//		return stringList;
//	}
}
