package es.socialmoney.servlets;

import javax.crypto.Cipher;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;

import es.socialmoney.dao.AccountDAOImplementation;
import es.socialmoney.model.Account;
import es.socialmoney.serializers.FollowsSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;



@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private KeyPair getKeyPairPEM() throws Exception {
    	ClassLoader classLoader = getClass().getClass().getClassLoader();
    	File file = new File(classLoader.getResource("clave.pem").getFile());
    	FileReader fileReader = new FileReader(file);
    	PEMParser pemParser = new PEMParser(fileReader);
        Object pemKeyPair = (Object) pemParser.readObject();
        PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build("PASSWORD".toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        KeyPair keyPair = converter.getKeyPair(((PEMEncryptedKeyPair)pemKeyPair).decryptKeyPair(decProv));
        pemParser.close();
        return keyPair;
     }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        JsonReader jsonReader = Json.createReader(new StringReader(data));
        JsonObject jsonObject = jsonReader.readObject();
        Account account = AccountDAOImplementation.getInstance().read(jsonObject.getString("username"));
        String plainText = null;
        String plainText2 = null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            KeyPair ks = this.getKeyPairPEM();
            RSAPrivateKey privKey = (RSAPrivateKey) ks.getPrivate();
            byte[] cipherText = Hex.decodeHex(jsonObject.getString("password").toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plain = cipher.doFinal(cipherText);
            plainText = new String(Base64.decodeBase64(plain));
            System.out.println(plainText);
         } catch (Exception ex) {System.out.println(ex);}
        if(account!=null) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            KeyPair ks = this.getKeyPairPEM();
            RSAPrivateKey privKey = (RSAPrivateKey) ks.getPrivate();
            byte[] cipherText = Hex.decodeHex(account.getPassword().toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plain = cipher.doFinal(cipherText);
            plainText2 = new String(Base64.decodeBase64(plain));
            System.out.println(plainText);
         } catch (Exception ex) {System.out.println(ex);}
        }
        
        if(plainText != null & plainText2 != null & plainText.equals(plainText2)) {
        	
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(account);
            
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Account.class, new FollowsSerializer());
			Gson gson2 = gsonBuilder.create();
			String jsonuserfollows = gson2.toJson(account);
			
            jsonObject = Json.createObjectBuilder()
                        .add("code",200)
                        .add("account",json)
                        .add("userFollows",jsonuserfollows)
                        .build();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");	
            req.getSession().setAttribute("loggedin",true);
            req.getSession().setAttribute("account", account);
            resp.getWriter().write(jsonObject.toString());
        }else{
            jsonObject = Json.createObjectBuilder()
                    .add("code",404)
                    .build();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");	
            resp.getWriter().write(jsonObject.toString());
        }
    }

}
