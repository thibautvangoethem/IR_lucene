package settings;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//thanks http://pojo.sodhanalibrary.com/pojoFromXSD.html
@XmlRootElement(name = "settings")
public class Settings {
	
	private String dump_location;

	private String jdbc_string;

	@XmlTransient
	private static Settings instance = null;

	public static Settings getInstance() {
		if(instance==null) {
			try {    
				URL resource = Settings.class.getClassLoader().getResource("settings.xml");
	            File file = new File(resource.toURI());    
	            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);    
	         
	            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	            instance=(Settings) jaxbUnmarshaller.unmarshal(file);    
	            
	          } catch (JAXBException e) {e.printStackTrace(); } catch (URISyntaxException e1) {
				e1.printStackTrace();
	          }
		}
		return instance;

	}

	private Settings() {
	}
	
	@XmlElement
	public String getDump_location() {
		return dump_location;
	}

	public void setDump_location(String dump_location) {
		this.dump_location = dump_location;
	}

	@XmlElement
	public String getJdbc_string() {
		return jdbc_string;
	}

	public void setJdbc_string(String jdbc_string) {
		this.jdbc_string = jdbc_string;
	}

	@Override
	public String toString() {
		return "ClassPojo [dump_location = " + dump_location + ", jdbc_string = " + jdbc_string + "]";
	}
}
