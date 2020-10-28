package xml_parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

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
				+ "(Id, PostTypeId, AcceptedAnswerId, ParentId,CreationDate, Score, ViewCount, Body, LastEditDate, LastActivityDate, Title, Tags, AnswerCount, CommentCount, FavoriteCount)"
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
