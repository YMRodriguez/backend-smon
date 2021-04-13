
package es.socialmoney.dao;

import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

import es.socialmoney.model.Account;
import es.socialmoney.model.Post;

 
public class SessionFactoryService {


 private SessionFactory sessionFactory;

  private static SessionFactoryService sfs;


 private SessionFactoryService() {
	 Configuration configuration = new Configuration().configure();
     configuration.addAnnotatedClass(Account.class);
     configuration.addAnnotatedClass(Post.class);
     sessionFactory = configuration.buildSessionFactory();
    
  }
 public static SessionFactory get() {

    if( null == sfs )

      sfs = new SessionFactoryService();

    return sfs.sessionFactory;

  }
}