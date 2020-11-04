package lucene_assignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;

import com.sun.javadoc.Doc;

public class ResultsToHtmlPage {

	public static void toHtml(String originalquery,Map<Document, List<Document>> results) {
		try {
			FileWriter index = createFile("results/index.html");
			index.write("<h3>Found " + results.size()+ " hit(s) for query: "+originalquery+"</h3>");
			int docIndex=1;
			for (Document doc:results.keySet()) {
				String title=doc.get("title");
				String body=doc.get("body");
				String filename = "answer"+Integer.toString(docIndex)+".html";
				FileWriter answerWriter= createFile("results/"+filename);
				answerWriter.write("<h1>"+title+"</h1>");
				answerWriter.write(body);
				answerWriter.close();
				
				index.write(Integer.toString(docIndex)+":"+"<a href="+filename+">"+title+"</a><br> ");
				docIndex+=1;
			}
			index.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private static FileWriter createFile(String name) throws IOException {
		File file = new File(name);
		if (file.exists())
			file.delete();
		file.createNewFile();
		return new FileWriter(name);
	}
	
	private static List<String> getAnswers(String id){
		return null;
	}
}
