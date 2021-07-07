
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

//@Component
public class encryptDecrypt {

    /** * 1024비트 RSA 키쌍을 생성합니다. */ 
    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException { 
        SecureRandom secureRandom = new SecureRandom(); 
        KeyPairGenerator gen; gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, secureRandom); 
        KeyPair keyPair = gen.genKeyPair(); 
        return keyPair; 
    }

          
public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException { 
    Cipher cipher = Cipher.getInstance("RSA"); 
    cipher.init(Cipher.ENCRYPT_MODE, publicKey); 
    byte[] bytePlain = cipher.doFinal(plainText.getBytes()); 
    String encrypted = Base64.getEncoder().encodeToString(bytePlain); 
    return encrypted; } 
    /** * Private Key로 RAS 복호화를 수행합니다. * * @param encrypted 암호화된 이진데이터를 base64 인코딩한 문자열 입니다. * @param privateKey 복호화를 위한 개인키 입니다. * @return * @throws Exception */
     public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException { 
         Cipher cipher = Cipher.getInstance("RSA"); 
         byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
          cipher.init(Cipher.DECRYPT_MODE, privateKey);
           byte[] bytePlain = cipher.doFinal(byteEncrypted);
            String decrypted = new String(bytePlain, "utf-8"); 
            return decrypted; 
        } 
        
    public static PrivateKey getPrivateKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException { 
        final String privateKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
         KeyFactory keyFactory = KeyFactory.getInstance("RSA"); 
         PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
          return keyFactory.generatePrivate(keySpecPKCS8); } 
          
          /** * Base64 엔코딩된 공용키키 문자열로부터 PublicKey객체를 얻는다. * @param keyString * @return * @throws NoSuchAlgorithmException * @throws InvalidKeySpecException */
           public static PublicKey getPublicKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException { 
               
            final String publicKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", ""); 
            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); 
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
             return keyFactory.generatePublic(keySpecX509); 
            
            }
            static HashMap<String, String> createKeypairAsString() {
                HashMap<String, String> stringKeypair = new HashMap<>();
                try {
                    SecureRandom secureRandom = new SecureRandom();
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                    keyPairGenerator.initialize(2048, secureRandom);
                    KeyPair keyPair = keyPairGenerator.genKeyPair();
        
                    PublicKey publicKey = keyPair.getPublic();
                    PrivateKey privateKey = keyPair.getPrivate();
        
                    String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                    String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        
                    stringKeypair.put("publicKey", stringPublicKey);
                    stringKeypair.put("privateKey", stringPrivateKey);
        
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return stringKeypair;
            }
    /**
     * 암호화
     */
    static String encode(String plainData, String stringPublicKey) {
        String encryptedData = null;
        try {
            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //평문을 암호화하는 과정
            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    /**
     * 복호화
     */
    static String decode(String encryptedData, String stringPrivateKey) {
        String decryptedData = null;
        try {
            //평문으로 전달받은 개인키를 개인키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            //만들어진 개인키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //암호문을 평문화하는 과정
            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());
            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);
            decryptedData = new String(byteDecryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }
}

