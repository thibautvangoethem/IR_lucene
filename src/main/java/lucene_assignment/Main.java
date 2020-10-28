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
	    
	        
			
			
			
			
//			addDoc(w, "Lucene in Action", "193398817");
//			addDoc(w, "Lucene for Dummies", "55320055Z");
//			addDoc(w, "Managing Gigabytes", "55063554A");
//			addDoc(w, "The Art of Computer Science", "9900333X");
//			w.close();
//			String querystr = args.length > 0 ? args[0] : "lucene";
//			Query q = new QueryParser("title", analyzer).parse(querystr);
//			int hitsPerPage = 10;
//			IndexReader reader = DirectoryReader.open(index);
//			IndexSearcher searcher = new IndexSearcher(reader);
//			TopDocs docs = searcher.search(q, hitsPerPage);
//			ScoreDoc[] hits = docs.scoreDocs;
//
//			System.out.println("Found " + hits.length + " hits.");
//			for (int i = 0; i < hits.length; ++i) {
//				int docId = hits[i].doc;
//				Document d = searcher.doc(docId);
//				System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

	}

	private static void addDoc(IndexWriter w, String title, String body) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("body", body, Field.Store.YES));
		w.addDocument(doc);
	}

}
