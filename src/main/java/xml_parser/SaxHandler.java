package xml_parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {
	static DateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private Connection conn;

	private PreparedStatement stmt;
	private int counter = 0;
	private long startTime = new Date().getTime();

	public SaxHandler(Connection conn, PreparedStatement stmt) {
		super();
		this.conn = conn;
		this.stmt = stmt;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("row")) {

			try {
				setIntOrNull(stmt, attributes.getValue("Id"), 1);
				setIntOrNull(stmt, attributes.getValue("PostTypeId"), 2);
				setIntOrNull(stmt, attributes.getValue("AcceptedAnswerId"), 3);
				setIntOrNull(stmt, attributes.getValue("ParentId"), 4);
				setTimestampOrNull(stmt, attributes.getValue("CreationDate"), 5);
				setIntOrNull(stmt, attributes.getValue("Score"), 6);
				setIntOrNull(stmt, attributes.getValue("ViewCount"), 7);
//					Also remove all html tags from te body
				stmt.setString(8, attributes.getValue("Body") == null ? null
						: attributes.getValue("Body").replaceAll("\\<.*?\\>", ""));
				setTimestampOrNull(stmt, attributes.getValue("LastEditDate"), 9);
				setTimestampOrNull(stmt, attributes.getValue("LastActivityDate"), 10);
				setStringOrNull(stmt, attributes.getValue("Title"), 11);
				setStringOrNull(stmt, attributes.getValue("Tags"), 12);
				setIntOrNull(stmt, attributes.getValue("AnswerCount"), 13);
				setIntOrNull(stmt, attributes.getValue("CommentCount"), 14);
				setIntOrNull(stmt, attributes.getValue("FavoriteCount"), 15);
				stmt.execute();
				counter++;
				if (counter > 100000) {
					counter=0;
					System.out.println("persisting 100000 rows");
					conn.commit();
					long current = new Date().getTime();
					System.out.println("it took " + Long.toString(current - startTime) + " ms");
				}
			} catch (SQLException e) {
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

	private static void setIntOrNull(PreparedStatement stmt, String value, int index) throws SQLException {
		Integer number = getNullableIntFromString(value);
		if (number == null) {
			stmt.setNull(index, java.sql.Types.INTEGER);
		} else {
			stmt.setInt(index, number);
		}
	}

	private static void setTimestampOrNull(PreparedStatement stmt, String value, int index) throws SQLException {
		Timestamp timestamp = getNullableTimestampFromString(value);
		if (timestamp == null) {
			stmt.setNull(index, java.sql.Types.TIMESTAMP);
		} else {
			stmt.setTimestamp(index, timestamp);
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

	public static Timestamp getNullableTimestampFromString(String timestamp) {
		try {
			return timestamp == null ? null : new Timestamp(timestampFormatter.parse(timestamp).getTime());
		} catch (ParseException e) {
			System.out.println("could not parse date: " + timestamp);
		}
		return null;
	}

}