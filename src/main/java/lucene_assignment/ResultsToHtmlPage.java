package lucene_assignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

public class ResultsToHtmlPage {

	public static void toHtml(List<Document> results) {
		try {
			FileWriter index = createFile("results/index.html");
			index.write("<h3>Found " + results.size()+ " hit(s).</h3>");
			for (int docindex = 0; docindex < results.size(); docindex++) {
				Document doc=results.get(docindex);
				String title=doc.get("title");
				String body=doc.get("body");
				String filename = "answer"+Integer.toString(docindex)+".html";
				FileWriter answerWriter= createFile("results/"+filename);
				answerWriter.write("<h1>"+title+"</h1>");
				answerWriter.write(body);
				answerWriter.close();
				
				index.write("<a href="+filename+">"+title+"</a> ");
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
