package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//thanks http://pojo.sodhanalibrary.com/pojoFromXSD.html
@XmlRootElement(name = "posts")
public class Posts {
	private Row[] row;
	
	@XmlElement(name = "row")
	public Row[] getRow() {
		return row;
	}

	public void setRow(Row[] row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "ClassPojo [row = " + row + "]";
	}
}
