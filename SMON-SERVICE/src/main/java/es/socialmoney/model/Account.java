package es.socialmoney.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, length = 24)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false, length = 56)
	private String name;
	@Column(nullable = false)
	private int age;
	@Column(columnDefinition = "boolean default false")
	private boolean isadmin;
	private String description;
	private String link;
	@Column(columnDefinition = "boolean default false")
	private boolean showprofits;
	@Lob
	private byte[] picture;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	private List<Integer> posts;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "follower"), inverseJoinColumns = @JoinColumn(name = "followed"))
	private List<String> following;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "followed"), inverseJoinColumns = @JoinColumn(name = "follower"))
	private List<String> followers;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "superfollows", joinColumns = @JoinColumn(name = "superfollower"), inverseJoinColumns = @JoinColumn(name = "superfollowed"))
	private List<String> superfollowing;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "superfollows", joinColumns = @JoinColumn(name = "superfollowed"), inverseJoinColumns = @JoinColumn(name = "superfollower"))
	private List<String> superfollowers;

	public Account() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isIsadmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isShowprofits() {
		return showprofits;
	}

	public void setShowprofits(boolean showprofits) {
		this.showprofits = showprofits;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public List<Integer> getPosts() {
		return posts;
	}

	public void setPosts(List<Integer> posts) {
		this.posts = posts;
	}

	public List<String> getFollowing() {
		return following;
	}

	public void setFollowing(List<String> following) {
		this.following = following;
	}

	public List<String> getFollowers() {
		return followers;
	}

	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}

	public List<String> getSuperfollowing() {
		return superfollowing;
	}

	public void setSuperfollowing(List<String> superfollowing) {
		this.superfollowing = superfollowing;
	}

	public List<String> getSuperfollowers() {
		return superfollowers;
	}

	public void setSuperfollowers(List<String> superfollowers) {
		this.superfollowers = superfollowers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((followers == null) ? 0 : followers.hashCode());
		result = prime * result + ((following == null) ? 0 : following.hashCode());
		result = prime * result + (isadmin ? 1231 : 1237);
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + Arrays.hashCode(picture);
		result = prime * result + ((posts == null) ? 0 : posts.hashCode());
		result = prime * result + (showprofits ? 1231 : 1237);
		result = prime * result + ((superfollowers == null) ? 0 : superfollowers.hashCode());
		result = prime * result + ((superfollowing == null) ? 0 : superfollowing.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Account other = (Account) obj;
		if (age != other.age)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (followers == null) {
			if (other.followers != null)
				return false;
		} else if (!followers.equals(other.followers))
			return false;
		if (following == null) {
			if (other.following != null)
				return false;
		} else if (!following.equals(other.following))
			return false;
		if (isadmin != other.isadmin)
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (!Arrays.equals(picture, other.picture))
			return false;
		if (posts == null) {
			if (other.posts != null)
				return false;
		} else if (!posts.equals(other.posts))
			return false;
		if (showprofits != other.showprofits)
			return false;
		if (superfollowers == null) {
			if (other.superfollowers != null)
				return false;
		} else if (!superfollowers.equals(other.superfollowers))
			return false;
		if (superfollowing == null) {
			if (other.superfollowing != null)
				return false;
		} else if (!superfollowing.equals(other.superfollowing))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + ", name=" + name + ", age=" + age
				+ ", isadmin=" + isadmin + ", description=" + description + ", link=" + link + ", showprofits="
				+ showprofits + ", picture=" + Arrays.toString(picture) + ", posts=" + posts + ", following="
				+ following + ", followers=" + followers + ", superfollowing=" + superfollowing + ", superfollowers="
				+ superfollowers + "]";
	}

}