package es.socialmoney.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.socialmoney.dao.PostDAOImplementation;
import es.socialmoney.model.Post;





@Path("/apirest")
public class Resources {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Post> readAll() {
		return PostDAOImplementation.getInstance().readAll();
	}
	
	
	@POST
	@Path("/createPost")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Post tnew) throws URISyntaxException {
		
	    System.out.println("gfdbjmfdkvnfdjhkvnhfjd");
		Post t = PostDAOImplementation.getInstance().create(tnew);
	
	    if (t != null) {
	            //URI uri = new URI("/SMON-SERVICE/rest/TFGs/" + t.getEmail());
	           // return Response.created(uri).build();
	    }
	    return Response.status(Response.Status.NOT_FOUND).build();

	}
	
	
	
	
	
}
