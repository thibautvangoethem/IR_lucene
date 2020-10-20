package xml_parser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import xml.Posts;
import xml.Row;

public class XmlToPostgres {
	public static void main(String[] args) {
		try {    
			URL resource = XmlToPostgres.class.getClassLoader().getResource("small.xml");
            File file = new File(resource.toURI());    
            JAXBContext jaxbContext = JAXBContext.newInstance(Posts.class);    
         
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Posts e=(Posts) jaxbUnmarshaller.unmarshal(file);    
            for(Row row:e.getRow()) {
            	System.out.print(row.getId());
            }
            
          } catch (JAXBException e) {e.printStackTrace(); } catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
         
	}

}
