package xml_parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.BytesRef;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lucene_assignment.SpecialCharConverter;

public class LuceneSaxHandler extends DefaultHandler {
	static DateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	private IndexWriter writer;
	private int counter = 0;
	private long startTime = new Date().getTime();

	public LuceneSaxHandler(IndexWriter writer) {
		super();
		this.writer = writer;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("row")) {

			try {

				String title = attributes.getValue("Title");
				String body = attributes.getValue("Body");
				int score = Integer.parseInt(attributes.getValue("Score"));
				boolean answer = true;
				String id = "";
				if (attributes.getValue("PostTypeId").equals("1")) {
					id = attributes.getValue("Id");
					answer = false;
				} else {
					id = attributes.getValue("ParentId");

				}
				String tags = attributes.getValue("Tags");
				if (tags != null) {
					tags = tags.substring(1, tags.length() - 1);
					tags = tags.replaceAll("><", " ");
				}
				addDoc(id, title, body, tags, score, answer);
				counter++;
				if (counter > 100000) {
					counter = 0;
					System.out.println("persisting 100000 rows");
					long current = new Date().getTime();
					System.out.println("it took " + Long.toString(current - startTime) + " ms");
				}
			} catch (IOException e) {
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

	private void addDoc(String id, String title, String body, String tags, int score, boolean answer)
			throws IOException {
		if (id != null) {
			Document doc = new Document();
			doc.add(new IntPoint("id", Integer.parseInt(id)));
			doc.add(new TextField("id", id, Field.Store.YES));
			doc.add(new SortedDocValuesField("id", new BytesRef(id)));
			if (title != null) {
				doc.add(new TextField("title", SpecialCharConverter.encode(title), Field.Store.NO));
				doc.add(new TextField("plainTitle", title, Field.Store.YES));
			}
			doc.add(new TextField("body", body, Field.Store.YES));
//		doc.add(new IntPoint("score", score));
			doc.add(new NumericDocValuesField("score", score));
			doc.add(new StoredField("answer", Boolean.toString(answer)));
			if (tags != null) {
				doc.add(new TextField("tags", tags, Field.Store.YES));
			}
			writer.addDocument(doc);
		}
	}

}
