package es.socialmoney.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import es.socialmoney.model.Account;

public class AccountDAOImplementation implements AccountDAO {

	private static AccountDAOImplementation instance = null;

	private AccountDAOImplementation() {

	}

	public static AccountDAOImplementation getInstance() {
		if (instance == null)
			instance = new AccountDAOImplementation();
		return instance;
	}

	@Override
	public Account create(Account account) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.save(account);
		session.getTransaction().commit();
		session.close();
		return account;
	}

	@Override
	public Account read(String username) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		Account account = session.get(Account.class, username);
		session.getTransaction().commit();
		session.close();
		return account;
	}

	@Override
	public Account update(Account account) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.saveOrUpdate(account);
		session.getTransaction().commit();
		session.close();
		return account;

	}

	@Override
	public Account delete(Account account) {
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		session.delete(account);
		session.getTransaction().commit();
		session.close();
		return account;
	}
	
	@Override
	public List<Account> readAll() {
		List<Account> accounts = new ArrayList<Account>();
		Session session = SessionFactoryService.get().openSession();
		session.beginTransaction();
		accounts.addAll(session.createQuery("from Account").list());
		session.getTransaction().commit();
		session.close();
		return accounts;
	}

	@Override
	public List<Account> readAll(List<String> usernames) {
		List<Account> res = new ArrayList<Account>();
		for (String username : usernames)
			res.add(this.read(username));
		return res;
	}

}
