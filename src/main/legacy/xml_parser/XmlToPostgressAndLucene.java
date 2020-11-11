package xml_parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.SAXException;

import settings.Settings;

public class XmlToPostgressAndLucene {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, SQLException {
		String url = Settings.getInstance().getJdbc_string();
		System.out.println("getting file");
		File file = new File(Settings.getInstance().getDump_location());
		System.out.println("start unmarshelling");

		EnglishAnalyzer analyzer = new EnglishAnalyzer();
		Directory index = FSDirectory.open(Paths.get("./index"));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);

		try (Connection conn = DriverManager.getConnection(url);
				IndexWriter w = new IndexWriter(index, config)) {
			conn.setAutoCommit(false);
			PostgressAndLuceneSaxHandler saxHandler = new PostgressAndLuceneSaxHandler(w,conn);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, saxHandler);
			conn.commit();
		}
	}
	

}
