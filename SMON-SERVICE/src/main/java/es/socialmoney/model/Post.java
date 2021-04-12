package es.socialmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 56)
	private String title;
	private String author;
	private String content;
	@Column(nullable = false)
	private LocalDate date;
	@Column(columnDefinition = "boolean default false")
	private boolean isexclusive;
	@Column(columnDefinition = "boolean default true")
	private boolean isfundan;
	@Column(columnDefinition = "boolean default false")
	private boolean isopinion;
	@Column(columnDefinition = "boolean default false")
	private boolean istecan;

	public Post() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isIsexclusive() {
		return isexclusive;
	}

	public void setIsexclusive(boolean isexclusive) {
		this.isexclusive = isexclusive;
	}

	public boolean isIsfundan() {
		return isfundan;
	}

	public void setIsfundan(boolean isfundan) {
		this.isfundan = isfundan;
	}

	public boolean isIsopinion() {
		return isopinion;
	}

	public void setIsopinion(boolean isopinion) {
		this.isopinion = isopinion;
	}

	public boolean isIstecan() {
		return istecan;
	}

	public void setIstecan(boolean istecan) {
		this.istecan = istecan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + (isexclusive ? 1231 : 1237);
		result = prime * result + (isfundan ? 1231 : 1237);
		result = prime * result + (isopinion ? 1231 : 1237);
		result = prime * result + (istecan ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (isexclusive != other.isexclusive)
			return false;
		if (isfundan != other.isfundan)
			return false;
		if (isopinion != other.isopinion)
			return false;
		if (istecan != other.istecan)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", author=" + author + ", content=" + content + ", date=" + date
				+ ", isexclusive=" + isexclusive + ", isfundan=" + isfundan + ", isopinion=" + isopinion + ", istecan="
				+ istecan + "]";
	}

}