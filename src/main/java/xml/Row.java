package xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//thanks http://pojo.sodhanalibrary.com/pojoFromXSD.html
@XmlRootElement(name = "row")
public class Row {
	private String ParentId;

	private String CreationDate;

	private String CommunityOwnedDate;

	private String ViewCount;

	private String Title;

	private String LastEditorUserId;

	private String LastEditorDisplayName;

	private String ContentLicense;

	private String content;

	private String Score;

	private String OwnerUserId;

	private String LastActivityDate;

	private String FavoriteCount;

	private String AnswerCount;

	private String CommentCount;

	private String Id;

	private String PostTypeId;

	private String LastEditDate;

	private String AcceptedAnswerId;

	private String Body;

	private String Tags;

	@XmlAttribute(name = "ParentId")
	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String ParentId) {
		this.ParentId = ParentId;
	}

	@XmlAttribute(name = "CreationDate")
	public String getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(String CreationDate) {
		this.CreationDate = CreationDate;
	}

	@XmlAttribute(name = "CommunityOwnedDate")
	public String getCommunityOwnedDate() {
		return CommunityOwnedDate;
	}

	public void setCommunityOwnedDate(String CommunityOwnedDate) {
		this.CommunityOwnedDate = CommunityOwnedDate;
	}

	@XmlAttribute(name = "ViewCount")
	public String getViewCount() {
		return ViewCount;
	}

	public void setViewCount(String ViewCount) {
		this.ViewCount = ViewCount;
	}

	@XmlAttribute(name = "Title")
	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	@XmlAttribute(name = "LastEditorUserId")
	public String getLastEditorUserId() {
		return LastEditorUserId;
	}

	public void setLastEditorUserId(String LastEditorUserId) {
		this.LastEditorUserId = LastEditorUserId;
	}

	@XmlAttribute(name = "LastEditorDisplayName")
	public String getLastEditorDisplayName() {
		return LastEditorDisplayName;
	}

	public void setLastEditorDisplayName(String LastEditorDisplayName) {
		this.LastEditorDisplayName = LastEditorDisplayName;
	}

	@XmlAttribute(name = "ContentLicense")
	public String getContentLicense() {
		return ContentLicense;
	}

	public void setContentLicense(String ContentLicense) {
		this.ContentLicense = ContentLicense;
	}
	
	@XmlAttribute(name = "Content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlAttribute(name = "Score")
	public String getScore() {
		return Score;
	}

	public void setScore(String Score) {
		this.Score = Score;
	}

	@XmlAttribute(name = "OwnerUserId")
	public String getOwnerUserId() {
		return OwnerUserId;
	}

	public void setOwnerUserId(String OwnerUserId) {
		this.OwnerUserId = OwnerUserId;
	}

	@XmlAttribute(name = "LastActivityDate")
	public String getLastActivityDate() {
		return LastActivityDate;
	}

	public void setLastActivityDate(String LastActivityDate) {
		this.LastActivityDate = LastActivityDate;
	}

	@XmlAttribute(name = "FavoriteCount")
	public String getFavoriteCount() {
		return FavoriteCount;
	}

	public void setFavoriteCount(String FavoriteCount) {
		this.FavoriteCount = FavoriteCount;
	}

	@XmlAttribute(name = "AnswerCount")
	public String getAnswerCount() {
		return AnswerCount;
	}

	public void setAnswerCount(String AnswerCount) {
		this.AnswerCount = AnswerCount;
	}

	@XmlAttribute(name = "CommentCount")
	public String getCommentCount() {
		return CommentCount;
	}

	public void setCommentCount(String CommentCount) {
		this.CommentCount = CommentCount;
	}

	@XmlAttribute(name = "Id")
	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	@XmlAttribute(name = "PostTypeId")
	public String getPostTypeId() {
		return PostTypeId;
	}

	public void setPostTypeId(String PostTypeId) {
		this.PostTypeId = PostTypeId;
	}

	@XmlAttribute(name = "LastEditDate")
	public String getLastEditDate() {
		return LastEditDate;
	}

	public void setLastEditDate(String LastEditDate) {
		this.LastEditDate = LastEditDate;
	}

   @XmlAttribute(name = "AcceptedAnswerId")
	public String getAcceptedAnswerId() {
		return AcceptedAnswerId;
	}

	public void setAcceptedAnswerId(String AcceptedAnswerId) {
		this.AcceptedAnswerId = AcceptedAnswerId;
	}

	@XmlAttribute(name = "Body")
	public String getBody() {
		return Body;
	}

	public void setBody(String Body) {
		this.Body = Body;
	}

	@XmlAttribute(name = "Tags")
	public String getTags() {
		return Tags;
	}

	public void setTags(String Tags) {
		this.Tags = Tags;
	}

	@Override
	public String toString() {
		return "ClassPojo [ParentId = " + ParentId + ", CreationDate = " + CreationDate + ", CommunityOwnedDate = "
				+ CommunityOwnedDate + ", ViewCount = " + ViewCount + ", Title = " + Title + ", LastEditorUserId = "
				+ LastEditorUserId + ", LastEditorDisplayName = " + LastEditorDisplayName + ", ContentLicense = "
				+ ContentLicense + ", content = " + content + ", Score = " + Score + ", OwnerUserId = " + OwnerUserId
				+ ", LastActivityDate = " + LastActivityDate + ", FavoriteCount = " + FavoriteCount + ", AnswerCount = "
				+ AnswerCount + ", CommentCount = " + CommentCount + ", Id = " + Id + ", PostTypeId = " + PostTypeId
				+ ", LastEditDate = " + LastEditDate + ", AcceptedAnswerId = " + AcceptedAnswerId + ", Body = " + Body
				+ ", Tags = " + Tags + "]";
	}
}
