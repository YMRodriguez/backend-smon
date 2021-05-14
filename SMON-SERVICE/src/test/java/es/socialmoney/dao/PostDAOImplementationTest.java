package es.socialmoney.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import es.socialmoney.model.Account;
import es.socialmoney.model.Post;


class PostDAOImplementationTest {
	
	@Test
    final void testAccount() {

        AccountDAO accountdao = AccountDAOImplementation.getInstance();
        Account account = new Account();

        // metodo create
        account.setUsername("primerUsuarioSmon");
        account.setPassword("primerUsuarioSmon");
        account.setName("primerUsuarioSmon");
        account.setAge(Integer.parseInt("25"));
        accountdao.create(account);
        
        //testing read
		Account account2 = accountdao.read("primerUsuarioSmon");
        assertEquals(account2.getUsername(), account.getUsername());
        assertEquals(account2.getName(), "primerUsuarioSmon"); 
        
        //testing update
        account.setPassword("contraseña2");
        accountdao.update(account);
        account2 = accountdao.read("primerUsuarioSmon");
        assertEquals(account2.getPassword(), "contraseña2");
        
        //testing delete
        accountdao.delete(account);
        account2 = accountdao.read("primerUsuarioSmon");
        assertNull(account2);
        
}
	
	
	@Test
    final void testPost() {
		// necesitamos una cuenta para hacer el post, ha testAccount
		
        AccountDAO accountdao = AccountDAOImplementation.getInstance();
        Account account = new Account();

        account.setUsername("primerUsuarioSmon");
        account.setPassword("primerUsuarioSmon");
        account.setName("primerUsuarioSmon");
        account.setAge(Integer.parseInt("25"));
        accountdao.create(account);		

        PostDAO postdao = PostDAOImplementation.getInstance();
        Post post = new Post();
        
        //testing create
        post.setTitle("primeraPublicacion");
        post.setAuthor(accountdao.read("primerUsuarioSmon"));
        post.setContent("primerContenidoPublicacion");
        post.setDate(LocalDate.now());
        post.setIsexclusive(Boolean.parseBoolean("true"));
        post.setIsfundan(Boolean.parseBoolean("true"));
        post.setIsopinion(Boolean.parseBoolean("true"));
        post.setIstecan(Boolean.parseBoolean("false"));
        postdao.create(post);
        //comprobamos que id es igual que el id del post que se ha creado
        assertEquals(post.getId(), 1);
        
        //testing read
    	Post post2 = postdao.read(1);
    	assertEquals(post2.getTitle(), post.getTitle());
        assertEquals(post2.getContent(), post.getContent());   
        
        //testing update
        post.setTitle("nuevoTitulo2");
        postdao.update(post);
        assertNotEquals(post2.getTitle(), "nuevoTitulo2");              
        
        //testing delete
        postdao.delete(post);
        post2 = postdao.read(1);
        assertNull(post2);
        

        
        

}
	
	
	

	


}

