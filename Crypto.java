import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Trica on 08/03/2016.
 */
public class Crypto {


    public String encrypt(String str,boolean enDec) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException{
        String enc="",dec="";
        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey myDesKey = keygenerator.generateKey();

        Cipher desCipher;
        desCipher = Cipher.getInstance("DES");


        byte[] text = str.getBytes("UTF8");


        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        byte[] textEncrypted = desCipher.doFinal(text);

        enc = new String(textEncrypted);


        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        byte[] textDecrypted = desCipher.doFinal(textEncrypted);

        dec=new String(textDecrypted);
        if(enDec==true)
        {
            return enc ;
        }
        else{
            return dec ;
        }



    }

    public String decode(String enc, int offset) throws IOException {
        String s="";
        return encode(enc, 26-offset);
    }

    public String encode(String enc, int offset) throws IOException {
        offset = offset % 26 + 26;
        int [] arrk={4,2,6,9,3,1};
        StringBuilder encoded = new StringBuilder();
        for (char i : enc.toCharArray()) {
            if (Character.isLetter(i)) {
                if (Character.isUpperCase(i)) {
                    encoded.append((char) ('A' + (i - 'A' + offset) % 26 ));
                } else {
                    encoded.append((char) ('a' + (i - 'a' + offset) % 26 ));
                }
            } else {


                encoded.append(i);
            }
        }
        return encoded.toString();
    }

    public int encodeInt(String enc)
    {
        StringBuilder encoded = new StringBuilder();
        int n;
        for (char i : enc.toCharArray()) {
            if (i >= '0' && i <= '9') {

                encoded.append(i);

            }
        }
        n=Integer.parseInt(encoded.toString());
        return Integer.parseInt(encoded.toString());
    }
}
