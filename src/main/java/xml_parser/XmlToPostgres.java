package xml_parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import settings.Settings;
import xml.Row;

public class XmlToPostgres {
	static DateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public static void main(String[] args) throws URISyntaxException, SQLException, JAXBException, XMLStreamException,
			FileNotFoundException, FactoryConfigurationError {

		String url = Settings.getInstance().getJdbc_string();
		System.out.println("getting file");
		File file = new File(Settings.getInstance().getDump_location());
//		URL resource = XmlToPostgres.class.getClassLoader().getResource("small.xml");
//		File file = new File(resource.toURI());
		JAXBContext jaxbContext = JAXBContext.newInstance(Row.class);

		PartialUnmarshaller<Row> jaxbUnmarshaller = new PartialUnmarshaller<Row>(new FileInputStream(file), Row.class);
		System.out.println("start unmarshelling");
		String inputStatement = "INSERT INTO public.documents"
				+ "(id, posttypeid, acceptedanswerid, parentid, creationdate, score, viewcount, body, lasteditdate, lastactivity, title, tags, answercount, commentcount, favoritecount)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(inputStatement);) {
			conn.setAutoCommit(false);
			System.out.println("start inserting rows");
			int counter = 0;
//			long start = new Date().getTime();
			while (jaxbUnmarshaller.hasNext()) {
				counter++;
				Row row = jaxbUnmarshaller.next();
				setIntOrNull(stmt, row.getId(), 1);
				setIntOrNull(stmt, row.getPostTypeId(), 2);
				setIntOrNull(stmt, row.getAcceptedAnswerId(), 3);
				setIntOrNull(stmt, row.getParentId(), 4);
				setTimestampOrNull(stmt, row.getCreationDate(), 5);
				setIntOrNull(stmt, row.getScore(), 6);
				setIntOrNull(stmt, row.getViewCount(), 7);
//					Also remove all html tags from te body
				stmt.setString(8, row.getBody() == null ? null : row.getBody().replaceAll("\\<.*?\\>", ""));
				setTimestampOrNull(stmt, row.getLastEditDate(), 9);
				setTimestampOrNull(stmt, row.getLastActivityDate(), 10);
				stmt.setString(11, row.getTitle());
				stmt.setString(12, row.getTags());
				setIntOrNull(stmt, row.getAnswerCount(), 13);
				setIntOrNull(stmt, row.getAnswerCount(), 14);
				setIntOrNull(stmt, row.getAnswerCount(), 15);
				stmt.execute();
//					persist objects to database every 100000 inserts
				if (counter > 100000) {
					System.out.println("persisting 100000 rows");
					conn.commit();
//					long end= new Date().getTime();
//					System.out.println("it took "+ Long.toString(end-start)+" ms");
					counter = 0;
				}

			}
			conn.commit();
		}

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
