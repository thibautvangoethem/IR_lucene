package lucene_assignment;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import settings.Settings;

public class Main {
	public static void main(String[] args) throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = FSDirectory.open(Paths.get("./index"));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);

		try (IndexWriter w = new IndexWriter(index, config)){	
			
			String url = Settings.getInstance().getJdbc_string();
	        String statement="select * from documents";
	        try (Connection conn = DriverManager.getConnection(url);
	                Statement stmt = conn.createStatement();
	                  ResultSet rs = stmt.executeQuery(statement);) {
	            while(rs.next()) {
	            	if (rs.getString(11) == null) {
	            		continue;
	            	}
	            	addDoc(w, rs.getString(11), rs.getString(8));
	            	
	                //System.out.println(rs.getString(11));
	        }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
	}

	private static void addDoc(IndexWriter w, String title, String body) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("body", body, Field.Store.YES));
		w.addDocument(doc);
	}

}
