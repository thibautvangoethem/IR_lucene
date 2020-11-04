package lucene_assignment;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;


public class QuerySearcher {
	public static void main(String[] args) throws IOException, ParseException {
		EnglishAnalyzer analyzer = new EnglishAnalyzer();
		Directory index = FSDirectory.open(Paths.get("./index"));

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		String input="c++";
		String query=escapeProhibitedLuceneCharacters(input);	
		System.out.println(query);
		Query qTitle = new QueryParser("title", analyzer).parse(query);
		Query qBody = new QueryParser("body", analyzer).parse(query);
 		BoostQuery boostedTitle = new BoostQuery(qTitle, 2);
 		
 		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(boostedTitle, Occur.SHOULD);
		queryBuilder.add(qBody, Occur.SHOULD);
		BooleanQuery q = queryBuilder.build();
		DoubleValuesSource valueSource= DoubleValuesSource.fromIntField("score");
		FunctionScoreQuery scoreQuery=new FunctionScoreQuery(q, valueSource);
		
		GroupingSearch gSearch=new GroupingSearch("id");
		gSearch.setAllGroups(true);
		TopGroups<BytesRef> gSearchDocuments = gSearch.search(searcher, scoreQuery, 0, 10);
		
		GroupDocs<BytesRef>[] test = gSearchDocuments.groups;
		for (GroupDocs<BytesRef> group : test) {
		    for (ScoreDoc scoredoc : group.scoreDocs) {
		        Document doc = searcher.doc(scoredoc.doc);
		        System.out.println( ". " + doc.get("title"));
		        System.out.println( ". " + doc.get("id"));
		        String id=doc.get("id");
		        Query qId = new QueryParser("id", analyzer).parse(id);
		        BooleanQuery.Builder queryBuilder2 = new BooleanQuery.Builder();
				queryBuilder.add(qId, Occur.MUST);
				BooleanQuery q2 = queryBuilder.build();
				TopDocs docs = searcher.search(q2, 10);
				ScoreDoc[] hits = docs.scoreDocs;
				List<Document> results=new ArrayList<>();
				System.out.println("Found " + hits.length + " hits.");
				for (int i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
//				    results.add(d);
					System.out.println((i + 1) + ". " + d.get("id"));
					System.out.println((i + 1) + ". " + d.get("title"));
//				    System.out.println((i + 1) + ". " + d.get("body"));

				}
		    }
		}
		

		int hitsPerPage = 10;
		
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		
		List<Document> results=new ArrayList<>();
//		System.out.println("Found " + hits.length + " hits.");
//		for(int i=0;i<hits.length;++i) {
//		    int docId = hits[i].doc;
//		    Document d = searcher.doc(docId);
////		    results.add(d);
//		    System.out.println((i + 1) + ". " + d.get("title"));
////		    System.out.println((i + 1) + ". " + d.get("body"));
//		    
//		}
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