package es.socialmoney.serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.socialmoney.model.Account;

public class AccountSerializer implements JsonSerializer<Account> {
	@Override
	public JsonElement serialize(Account author, Type typeOfSrc, JsonSerializationContext context ) {
		JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", author.getName());
        return jsonObject;	
	}
}
