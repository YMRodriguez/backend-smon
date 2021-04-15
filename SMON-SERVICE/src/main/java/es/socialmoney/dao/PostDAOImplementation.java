package es.socialmoney.dao;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import es.socialmoney.model.Post;

public class PostDAOImplementation implements PostDAO {

	private static PostDAOImplementation instance = null;

	private PostDAOImplementation() {

	}

	public static PostDAOImplementation getInstance() {
		if (instance == null)
			instance = new PostDAOImplementation();
		return instance;
	}

	@Override
	public Post create(Post post) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.save(post);
		session.getTransaction().commit();
		session.close();
		return post;
	}

	@Override
	public Post read(int id) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		Post post = session.get(Post.class, id);
		session.getTransaction().commit();
		session.close();
		return post;
	}

	@Override
	public Post update(Post post) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.saveOrUpdate(post);
		session.getTransaction().commit();
		session.close();
		return post;

	}

	@Override
	public Post delete(Post post) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.delete(post);
		session.getTransaction().commit();
		session.close();
		return post;
	}

	@Override
	public List<Post> readAll() {
		List<Post> posts = new ArrayList<Post>();
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		posts.addAll(session.createQuery("from Post").list());
		session.getTransaction().commit();
		session.close();
		return posts;
	}

	@Override
    public List<Post> readAll(String username) {
        List<Post> res = new ArrayList<Post>();
        for (Post post : this.readAll()) {
        	String a = post.getAuthor().getUsername();
        	String b = username;
            if (post.getAuthor().getUsername().equals(username))
                res.add(post);
        	}
        return res;

    }

	@Override
	public List<Post> readAll(List<String> usernames) {
		List<Post> res = new ArrayList<Post>();
		for (String username : usernames) {
			for (Post post : this.readAll(username))
				res.add(post);
		}
		return res;

	}

}
