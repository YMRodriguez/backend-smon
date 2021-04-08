package es.socialmoney.dao;

import es.socialmoney.model.Post;
import java.util.List;

public interface PostDAO {
	
	public Post create(Post post);
	public Post read(int id);
	public Post update(Post post);
	public Post delete(Post post);
	public List<Post> readAll();
	public List<Post> readAll(String username);
	public List<Post> readAll(List<String> usernames);
}