package es.socialmoney.serializers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.socialmoney.model.Account;

public class SuperfollowsSerializer implements JsonSerializer<Account> {
    @Override
    public JsonElement serialize(Account account, Type typeOfSrc, JsonSerializationContext context ) {

          List<Account> pending = account.getSuperFollowersPending();
          List<Account> followers = account.getSuperfollowers(); 
          List<String> pendingusers = new ArrayList<String>();
          List<String> userfollowers = new ArrayList<String>(); 
          JsonObject jsonObject = new JsonObject(); 
          for (int i=0; i< pending.size(); i++) {
        	  pendingusers.add(pending.get(i).getUsername()); 
          } 
          for (int i=0; i<followers.size(); i++) { 
              userfollowers.add(followers.get(i).getUsername()); 
          }
 
        jsonObject.addProperty("userSuperFollowsPending", pendingusers.toString());
        jsonObject.addProperty("userSuperFollowers", userfollowers.toString());
        return jsonObject;
    }
}