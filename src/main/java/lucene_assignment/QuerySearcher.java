package lucene_assignment;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.Explanation;
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
		long startTime = new Date().getTime();
		try {

			EnglishAnalyzer analyzer = new EnglishAnalyzer();
			Directory index = FSDirectory.open(Paths.get("./index"));

			String input = "What is the --> operator";
			String query = SpecialCharConverter.encode(input);
			System.out.println(query);
			Query qTitle = new QueryParser("title", analyzer).parse(query);
			Query qBody = new QueryParser("body", analyzer).parse(query);
			Query qTags = new QueryParser("tags", analyzer).parse(query);
			BoostQuery boostedTitle = new BoostQuery(qTitle, 2);

			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);

			BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
			queryBuilder.add(boostedTitle, Occur.SHOULD);
			queryBuilder.add(qBody, Occur.SHOULD);
			queryBuilder.add(qTags, Occur.SHOULD);
			BooleanQuery q = queryBuilder.build();
			DoubleValuesSource valueSource = DoubleValuesSource.fromDoubleField("score");
			FunctionScoreQuery scoreQuery = FunctionScoreQuery.boostByValue(q, valueSource);
			
			TopDocs searchDocuments = searcher.search(scoreQuery, 10);

			Map<Document, List<Document>> resultMap = new LinkedHashMap<>();
			for (ScoreDoc scoredoc : searchDocuments.scoreDocs) {
				Document doc = searcher.doc(scoredoc.doc);
				String id = doc.get("id");
				getDocumentForId(analyzer, searcher, id, resultMap);

				Explanation expl = searcher.explain(scoreQuery, scoredoc.doc);
				System.out.println(expl);
			}

			ResultsToHtmlPage.toHtml(input, resultMap);
		} catch (Exception e) {
			System.out.println(e);
		}
		long current = new Date().getTime();
		System.out.println("it took " + Long.toString(current - startTime) + " ms");

	}

	private static void getDocumentForId(Analyzer analyzer, IndexSearcher searcher, String id,
			Map<Document, List<Document>> result) throws ParseException, IOException {
		Document question = null;

		Query qId = new QueryParser("id", analyzer).parse(id);

		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(qId, Occur.MUST);
		BooleanQuery q = queryBuilder.build();
		TopDocs docs = searcher.search(q, 1000);
		ScoreDoc[] hits = docs.scoreDocs;
		List<Document> results = new ArrayList<>();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			if (d.get("answer").equals("true")) {
				results.add(d);
			} else {
				question = d;
			}

		}
		if (!result.containsKey(question)) {
			result.put(question, results);
		}
	}

	public static String escapeProhibitedLuceneCharacters(String query) {
//		Make sure the \ is escaped first otherwise things will be escape twice
		List<String> escapelist = Arrays.asList("\\", "+", "-", "=", "&&", "||", ">", "<", "!", "(", ")", "{", "}", "[",
				"]", "^", "\"", "~", "*", "?", ":", "/");
		String newquery = query;
		for (String character : escapelist) {
			newquery = newquery.replace(character, '\\' + character);
		}

		return newquery;
	}
}