package es.socialmoney.serializers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.socialmoney.model.Account;

public class FollowsSerializer implements JsonSerializer<Account> {
    @Override
    public JsonElement serialize(Account account, Type typeOfSrc, JsonSerializationContext context ) {

          List<Account> following = account.getFollowing();
          List<Account> followers = account.getFollowers(); 
          List<String> userfollowing = new ArrayList<String>();
          List<String> userfollowers = new ArrayList<String>(); 
          JsonObject jsonObject = new JsonObject(); 
          for (int i=0; i< following.size(); i++) {
              userfollowing.add(following.get(i).getUsername()); 
          } 
          for (int i=0; i<followers.size(); i++) { 
              userfollowers.add(followers.get(i).getUsername()); 
          }
 
        jsonObject.addProperty("userFollows", userfollowing.toString());
        jsonObject.addProperty("userFollowers", userfollowers.toString());
        return jsonObject;
    }
}