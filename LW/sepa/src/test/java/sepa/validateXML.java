package sepa;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class validateXML {
	
	private String filename = "src/main/resources/tp1.sepa.01.xml";
	private String xsdfile = "src/main/resources/tp1.sepa.01.xsd";
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Test
	public void should_validate_with_DOM() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		SchemaFactory schemaFactory = 
		    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		factory.setSchema(schemaFactory.newSchema(
		    new Source[] {new StreamSource(xsdfile)}));

		DocumentBuilder builder = factory.newDocumentBuilder();
		SimpleErrorHandler seh = new SimpleErrorHandler();
		builder.setErrorHandler(seh);
		builder.parse(new InputSource(filename));
		
		assertFalse(seh.hasError());
	}
	
	@Test
	public void should_validate_with_SAX() throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		SchemaFactory schemaFactory = 
		    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		factory.setSchema(schemaFactory.newSchema(
		    new Source[] {new StreamSource(xsdfile)}));

		SAXParser parser = factory.newSAXParser();

		XMLReader reader = parser.getXMLReader();
		SimpleErrorHandler seh = new SimpleErrorHandler();
		reader.setErrorHandler(seh);
		reader.parse(new InputSource(filename));

		assertFalse(seh.hasError());
	}
}
