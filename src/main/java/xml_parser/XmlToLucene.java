package xml_parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.SAXException;

import settings.Settings;

public class XmlToLucene {

	public static void main(String[] args)
			throws URISyntaxException, XMLStreamException, ParserConfigurationException, SAXException, IOException {

		String url = Settings.getInstance().getJdbc_string();
		System.out.println("getting file");
		File file = new File(Settings.getInstance().getDump_location());
		System.out.println("start unmarshelling");

		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = FSDirectory.open(Paths.get("./index"));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);

		try (IndexWriter w = new IndexWriter(index, config)) {
			LuceneSaxHandler saxHandler = new LuceneSaxHandler(w);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, saxHandler);
		}
	}

}
