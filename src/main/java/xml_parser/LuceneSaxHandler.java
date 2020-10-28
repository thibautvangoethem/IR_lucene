package xml_parser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LuceneSaxHandler extends DefaultHandler {
	static DateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	private IndexWriter writer;
	private int counter = 0;
	private long startTime = new Date().getTime();

	public LuceneSaxHandler(IndexWriter writer) {
		super();
		this.writer = writer;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("row")) {

			try {
				String title=attributes.getValue("Title");
				String body=attributes.getValue("Body");
				if(title!=null ) {
					addDoc(title, body);
				}
				counter++;
				if (counter > 100000) {
					counter = 0;
					System.out.println("persisting 100000 rows");
					long current = new Date().getTime();
					System.out.println("it took " + Long.toString(current - startTime) + " ms");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

	}
	
	private void addDoc(String title, String body) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("body", body, Field.Store.YES));
		writer.addDocument(doc);
	}
	

}
