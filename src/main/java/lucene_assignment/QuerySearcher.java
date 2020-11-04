package lucene_assignment;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class QuerySearcher {
	public static void main(String[] args) throws IOException, ParseException {
		EnglishAnalyzer analyzer = new EnglishAnalyzer();
		Directory index = FSDirectory.open(Paths.get("./index"));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		String input="c int to string";
		String query=escapeProhibitedLuceneCharacters(input);	
		System.out.println(query);
		Query qTitle = new QueryParser("title", analyzer).parse(query);
		Query qBody = new QueryParser("body", analyzer).parse(query);
		BoostQuery boostedTitle = new BoostQuery(qTitle, 2);
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(boostedTitle, Occur.SHOULD);
		queryBuilder.add(qBody, Occur.SHOULD);
		BooleanQuery q = queryBuilder.build();
		
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		
		List<Document> results=new ArrayList<>();
		System.out.println("Found " + hits.length + " hits.");
		for(int i=0;i<hits.length;++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
//		    results.add(d);
		    System.out.println((i + 1) + ". " + d.get("title"));
//		    System.out.println((i + 1) + ". " + d.get("body"));
		    
		}
		ResultsToHtmlPage.toHtml(results);
	      
	}
	
	public static String escapeProhibitedLuceneCharacters(String query) {
//		Make sure the \ is escaped first otherwise things will be escape twice
		List<String> escapelist=Arrays.asList("\\" ,"+", "-" ,"=" ,"&&" ,"||" ,">", "<","!" ,"(" ,")" ,"{", "}" ,"[" ,"]" ,"^" ,"\"", "~" ,"*" ,"?" ,":" ,"/");
		String newquery=query;
		for (String character:escapelist) {
			newquery=newquery.replace(character, '\\'+character);
		}
		
		
		return newquery;
	}
}