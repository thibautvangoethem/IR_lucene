package xml_parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.SAXException;

import settings.Settings;

public class XmlToPostgres {
	

	public static void main(String[] args) throws URISyntaxException, XMLStreamException, ParserConfigurationException, SAXException, IOException {

		String url = Settings.getInstance().getJdbc_string();
		System.out.println("getting file");
		File file = new File(Settings.getInstance().getDump_location());
//		URL resource = XmlToPostgres.class.getClassLoader().getResource("small.xml");
//		File file = new File(resource.toURI());
		System.out.println("start unmarshelling");
		String inputStatement = "INSERT INTO public.documents"
				+ "(id, posttypeid, acceptedanswerid, parentid, creationdate, score, viewcount, body, lasteditdate, lastactivity, title, tags, answercount, commentcount, favoritecount)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(inputStatement);) {
			conn.setAutoCommit(false);
			SaxHandler saxHandler=new SaxHandler(conn,stmt);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, saxHandler);
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
