package screens;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public abstract class FontLoader {
	
	public static HashMap<String, BitmapFont> fonts = new HashMap<>();
	
	private final static String NPCS_PATH = "../core/assets/Data/Enemies.xml";
	private final static String WEAPONS_PATH = "../core/assets/Data/Weapons.xml";
	private final static String POTIONS_PATH = "../core/assets/Data/Potions.xml";
	private final static String TERRAINS_PATH = "../core/assets/Data/Terrains.xml";
	
	private static Document npcsDoc;
	private static Document weaponsDoc;
	private static Document potionsDoc;
	private static Document terrainsDoc;
	
	private static XPath XPath;
	
	static{
		XPath = XPathFactory.newInstance().newXPath();
		try {
			npcsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(NPCS_PATH));
			weaponsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(WEAPONS_PATH));
			potionsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(POTIONS_PATH));
			terrainsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(TERRAINS_PATH));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println(e);
		}
		load(GameScreenASCII.TILE_SIZE);
	}
	
	public static void load(int tileSize){
		FreeTypeFontGenerator generator;
		FreeTypeFontParameter parameter;
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/arialbd.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.characters += searchASCII(terrainsDoc);
		parameter.size = (int) (tileSize * 0.85);
		fonts.put("terrain", generator.generateFont(parameter));
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/arialbd.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.characters += searchASCII(weaponsDoc);
		parameter.characters += searchASCII(potionsDoc);
		parameter.characters += searchASCII(npcsDoc);
		parameter.size = (int) (tileSize * 0.85);
		parameter.shadowColor = Color.BLACK;
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		fonts.put("general", generator.generateFont(parameter));
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/arial.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 15;
		fonts.put("title", generator.generateFont(parameter));

		generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/arial.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 15;
		fonts.put("menu", generator.generateFont(parameter));
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/arial.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = (int) (tileSize * 0.85);
		parameter.shadowColor = Color.BLACK;
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		parameter.kerning = true;
		fonts.put("console", generator.generateFont(parameter));
		fonts.get("console").getData().markupEnabled = true;
		
		generator.dispose();

	}
	
	public static String searchASCII(Document XMLFile){
		XPathExpression expr = null;
		String ASCIIStrings = "";
		try {
			expr = XPath.compile("//ASCII");
			NodeList list = (NodeList) expr.evaluate(XMLFile, XPathConstants.NODESET);
			
			for(int i = 0; i < list.getLength(); i++){
				ASCIIStrings += list.item(i).getTextContent();
			}	
		} catch (XPathExpressionException e) {}
		return ASCIIStrings;
	}

}
