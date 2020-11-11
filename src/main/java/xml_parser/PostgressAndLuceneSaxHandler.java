package xml_parser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PostgressAndLuceneSaxHandler extends DefaultHandler {
	private static DateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static String inputStatement="insert into answers(id,parentid,score,body) values (?,?,?,?);";

	private IndexWriter writer;
	private int counter = 0;
	private long startTime = new Date().getTime();
	private Connection conn=null;

	public PostgressAndLuceneSaxHandler(IndexWriter writer,Connection conn) {
		super();
		this.writer = writer;
		this.conn = conn;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("row")) {

			try (PreparedStatement stmt = conn.prepareStatement(inputStatement)){
				if (attributes.getValue("PostTypeId").equals("1")) {
					int  id = getNullableIntFromString(attributes.getValue("Id"));
					String title = attributes.getValue("Title");
					String body = attributes.getValue("Body");
					if (title != null) {
						addDoc(id,title, body);
					}
				}else {
					try {
						setIntOrNull(stmt, attributes.getValue("Id"), 1);
						setIntOrNull(stmt, attributes.getValue("ParentId"), 2);
						setIntOrNull(stmt, attributes.getValue("Score"), 3);
//							Also remove all html tags from te body
						stmt.setString(4, attributes.getValue("Body") == null ? null
								: attributes.getValue("Body").replaceAll("\\<.*?\\>", ""));
						stmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				counter++;
				if (counter > 100000) {
					counter = 0;
					conn.commit();
					System.out.println("persisting 100000 rows");
					long current = new Date().getTime();
					System.out.println("it took " + Long.toString(current - startTime) + " ms");
				}
			} catch (IOException | SQLException e) {
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

	private void addDoc(int id,String title, String body) throws IOException {
		Document doc = new Document();
		doc.add(new StoredField("id", id));
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("body", body, Field.Store.YES));
		writer.addDocument(doc);
	}
	
	private static void setIntOrNull(PreparedStatement stmt, String value, int index) throws SQLException {
		Integer number = getNullableIntFromString(value);
		if (number == null) {
			stmt.setNull(index, java.sql.Types.INTEGER);
		} else {
			stmt.setInt(index, number);
		}
	}

	private static void setStringOrNull(PreparedStatement stmt, String value, int index) throws SQLException {
		if (value == null) {
			stmt.setNull(index, java.sql.Types.VARCHAR);
		} else {
			stmt.setString(index, value);
		}
	}

	public static Integer getNullableIntFromString(String number) {
		return number == null ? null : Integer.parseInt(number);
	}
}
