package es.socialmoney.dao;

import es.socialmoney.model.Account;
import java.util.List;

public interface AccountDAO {

	public Account create(Account account);

	public Account read(String username);

	public Account update(Account account);

	public Account delete(Account account);

	public List<Account> readAll();

	public List<Account> readAll(List<String> usernames);

}