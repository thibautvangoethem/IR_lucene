package xml_parser;

import java.io.File;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import settings.Settings;
import xml.Posts;
import xml.Row;

public class XmlToPostgres {
	static DateFormat timestampFormatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public static void main(String[] args) throws URISyntaxException, SQLException, JAXBException {

		URL resource = XmlToPostgres.class.getClassLoader().getResource("small.xml");
		String url = Settings.getInstance().getJdbc_string();
		File file = new File(resource.toURI());
		JAXBContext jaxbContext = JAXBContext.newInstance(Posts.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Posts e = (Posts) jaxbUnmarshaller.unmarshal(file);
		String inputStatement="INSERT INTO public.documents"
				+ "(id, posttypeid, acceptedanswerid, parentid, creationdate, score, viewcount, body, lasteditdate, lastactivity, title, tags, answercount, commentcount, favoritecount)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(inputStatement);) {
			conn.setAutoCommit(false);
			for (Row row : e.getRow()) {
					setIntOrNull(stmt, row.getId(),1);
					setIntOrNull(stmt, row.getPostTypeId(),2);
					setIntOrNull(stmt, row.getAcceptedAnswerId(),3);
					setIntOrNull(stmt, row.getParentId(),4);
					setTimestampOrNull(stmt, row.getCreationDate(), 5);
					setIntOrNull(stmt, row.getScore(),6);
					setIntOrNull(stmt, row.getViewCount(),7);
					stmt.setString(8, row.getBody());
					setTimestampOrNull(stmt, row.getLastEditDate(), 9);
					setTimestampOrNull(stmt, row.getLastActivityDate(), 10);
					stmt.setString(11, row.getTitle());
					stmt.setString(12, row.getTags());
					setIntOrNull(stmt, row.getAnswerCount(),13);
					setIntOrNull(stmt, row.getAnswerCount(),14);
					setIntOrNull(stmt, row.getAnswerCount(),15);
					stmt.execute();

			}
			conn.commit();
		}

	}

	private static void setIntOrNull(PreparedStatement stmt, String value,int index) throws SQLException {
		Integer number=getNullableIntFromString(value);
		if(number==null) {
			stmt.setNull(index, java.sql.Types.INTEGER);
		}else {
			stmt.setInt(index, number);
		}
	}
	
	private static void setTimestampOrNull(PreparedStatement stmt, String value,int index) throws SQLException {
		Timestamp timestamp=getNullableTimestampFromString(value);
		if(timestamp==null) {
			stmt.setNull(index, java.sql.Types.TIMESTAMP);
		}else {
			stmt.setTimestamp(index, timestamp);
		}
	}
	
	public static Integer getNullableIntFromString(String number) {
		return number==null?null:Integer.parseInt(number);
	}
	
	public static Timestamp getNullableTimestampFromString(String timestamp) {
		try {
			return timestamp==null?null:new Timestamp(timestampFormatter.parse(timestamp).getTime());
		} catch (ParseException e) {
			System.out.println("could not parse date: "+timestamp);
		}
		return null;
	}

}
