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
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
/**
 * Servlet implementation class DeleteAccount
 */
@WebServlet("/deleteAccount")
public class DeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */    
    private KeyPair getKeyPairPEM() throws Exception {
	        FileReader fileReader = new FileReader("/home/ramos/clave.pem");
	        PEMParser pemParser = new PEMParser(fileReader);
	        Object pemKeyPair = (Object) pemParser.readObject();
	        PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build("PASSWORD".toCharArray());
	        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
	        KeyPair keyPair = converter.getKeyPair(((PEMEncryptedKeyPair)pemKeyPair).decryptKeyPair(decProv));
	        pemParser.close();
	        return keyPair;
	     }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JsonReader jsonReader = Json.createReader(new StringReader(data));
		JsonObject jsonObject = jsonReader.readObject();

		System.out.println(jsonObject);
        Account account = AccountDAOImplementation.getInstance().read(jsonObject.getString("username"));
        String plainText = null;
        String plainText2 = null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            KeyPair ks = this.getKeyPairPEM();
            RSAPrivateKey privKey = (RSAPrivateKey) ks.getPrivate();
            byte[] cipherText = Hex.decodeHex(jsonObject.getString("passwordDelete").toCharArray());
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
            System.out.println(plainText2);
         } catch (Exception ex) {System.out.println(ex);}
        } 
        
        if(plainText != null & plainText2 != null & plainText.equals(plainText2)) {
        	System.out.println(plainText);
        	System.out.println(plainText2);
		Account accounted = AccountDAOImplementation.getInstance().delete(account);
		
		if (accounted != null) {

			jsonObject = Json.createObjectBuilder().add("code", 200).build();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObject.toString());

		} else {
			jsonObject = Json.createObjectBuilder().add("code", 404).build();
		}
		

		
	}

}}
