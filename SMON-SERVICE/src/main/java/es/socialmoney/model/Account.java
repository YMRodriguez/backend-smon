package es.socialmoney.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Expose
	@Column(nullable = false, length = 24)
	private String username;
	@Column(nullable = false, length = 1000)
	private String password;
	@Expose
	@Column(nullable = false, length = 56)
	private String name;
	@Expose
	@Column(nullable = false)
	private int age;
	@Expose
	@Column(columnDefinition = "boolean default false")
	private boolean isadmin;
	@Expose
	private String description;
	@Expose
	private String link;
	@Expose
	private String timeframe;
	@Expose
	private String profit;
	@Expose
	private String accountType;
	@Expose
	@Column(columnDefinition = "boolean default false")
	private boolean showprofits;
	@Lob
	@Expose
	private byte[] picture;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "author")
	private List<Post> posts;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "follower"), inverseJoinColumns = @JoinColumn(name = "followed"))
	private List<Account> following;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "follows", joinColumns = @JoinColumn(name = "followed"), inverseJoinColumns = @JoinColumn(name = "follower"))
	private List<Account> followers;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "superfollows_pending")
	private List<Account> superfollowerspending; 

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "superfollows", joinColumns = @JoinColumn(name = "superfollower"), inverseJoinColumns = @JoinColumn(name = "superfollowed"))
	private List<Account> superfollowing;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "superfollows", joinColumns = @JoinColumn(name = "superfollowed"), inverseJoinColumns = @JoinColumn(name = "superfollower"))
	private List<Account> superfollowers;

	public Account() {
		super();
	}

	public Account(String username, String password, String name, int age) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.age = age;
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
		if (this.picture != null) {
			return picture;
		} else {
			return new byte[0];
		}
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Account> getSuperFollowersPending() {
		return superfollowerspending;
	}

	public void setSuperFollowersPending(List<Account> superfollowerspending) {
		this.superfollowerspending = superfollowerspending;
	}
	
	public List<Account> getFollowing() {
		return following;
	}

	public void setFollowing(List<Account> following) {
		this.following = following;
	}

	public List<Account> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Account> followers) {
		this.followers = followers;
	}

	public List<Account> getSuperfollowing() {
		return superfollowing;
	}

	public void setSuperfollowing(List<Account> superfollowing) {
		this.superfollowing = superfollowing;
	}

	public List<Account> getSuperfollowers() {
		return superfollowers;
	}

	public void setSuperfollowers(List<Account> superfollowers) {
		this.superfollowers = superfollowers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + ", name=" + name + ", age=" + age
				+ ", isadmin=" + isadmin + ", description=" + description + ", link=" + link + ", timeframe="
				+ timeframe + ", profit=" + profit + ", accountType=" + accountType + ", showprofits=" + showprofits
				+ ", picture=" + Arrays.toString(picture);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
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
		result = prime * result + ((profit == null) ? 0 : profit.hashCode());
		result = prime * result + (showprofits ? 1231 : 1237);
		result = prime * result + ((superfollowers == null) ? 0 : superfollowers.hashCode());
		result = prime * result + ((superfollowing == null) ? 0 : superfollowing.hashCode());
		result = prime * result + ((timeframe == null) ? 0 : timeframe.hashCode());
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
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
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
		if (profit == null) {
			if (other.profit != null)
				return false;
		} else if (!profit.equals(other.profit))
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
		if (timeframe == null) {
			if (other.timeframe != null)
				return false;
		} else if (!timeframe.equals(other.timeframe))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

}