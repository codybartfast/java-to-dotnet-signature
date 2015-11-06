// WARNING:  This is demo code.  It is NOT Secure code.
//
// Creates private and public keys and saves them to files in the current
// directory in Java and .Net formats

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    static final String KEY_ALGORITHM = "RSA";
    static final int KEY_LENGTH = 1024;

    static final String ENCODED_PRIVATE_FILENAME = "EncodedPrivateKey.txt";
    static final String ENCODED_PUBLIC_FILENAME = "EncodedPublicKey.txt";
    static final String XML_PRIVATE_FILENAME = "XmlPrivateKey.txt";
    static final String XML_PUBLIC_FILENAME = "XmlPublicKey.txt";

    static final String NL = System.getProperty("line.separator");
    
    public static void main(String[] args) throws Exception {

        print("About to create KeyPair.");
        KeyPair keyPair = createKeyPair(KEY_LENGTH);

        print("Created KeyPair.");
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        print("About to write PRIVATE key in Encoded (Java) format.");
        String privateKeyAsEncoded = getPrivateKeyAsEncoded(privateKey);
        print("Private key in Encoded format:" + NL + privateKeyAsEncoded);
        writeFile(privateKeyAsEncoded, ENCODED_PRIVATE_FILENAME);

        print("About to write PUBLIC key in Encoded (Java) format.");
        String publicKeyAdEncoded = getPublicKeyAsEncoded(publicKey);
        print("Public key in Encoded format:" + NL + publicKeyAdEncoded);
        writeFile(publicKeyAdEncoded, ENCODED_PUBLIC_FILENAME);

        print("About to write PRIVATE key in XML (.Net) format.");
        String privateKeyAsXml = getPrivateKeyAsXml(privateKey);
        print("Private key in XML format:" + NL + privateKeyAsXml);
        writeFile(privateKeyAsXml, XML_PRIVATE_FILENAME);

        print("About to write PUBLIC key in XML (.Net) format.");
        String publicKeyAsXml = getPublicKeyAsXml(publicKey);
        print("Public key in XML format:" + NL + publicKeyAsXml);
        writeFile(publicKeyAsXml, XML_PUBLIC_FILENAME);

        print("Done.");
    }

    static KeyPair createKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keygen.initialize(keyLength, new SecureRandom());
        KeyPair keyPair = keygen.generateKeyPair();
        return keyPair;
    }

    static String getPrivateKeyAsEncoded(PrivateKey privateKey){
        byte[] privateKeyEncodedBytes = privateKey.getEncoded();
        return getBase64(privateKeyEncodedBytes);
    }

    static String getPublicKeyAsEncoded(PublicKey publicKey){
        byte[] publicKeyEncoded = publicKey.getEncoded();
        return getBase64(publicKeyEncoded);
    }

    static String getPrivateKeyAsXml(PrivateKey privateKey)throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPrivateCrtKeySpec spec = keyFactory.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>" + NL);
        sb.append(getElement("Modulus", spec.getModulus()));
        sb.append(getElement("Exponent", spec.getPublicExponent()));
        sb.append(getElement("P", spec.getPrimeP()));
        sb.append(getElement("Q", spec.getPrimeQ()));
        sb.append(getElement("DP", spec.getPrimeExponentP()));
        sb.append(getElement("DQ", spec.getPrimeExponentQ()));
        sb.append(getElement("InverseQ", spec.getCrtCoefficient()));
        sb.append(getElement("D", spec.getPrivateExponent()));
        sb.append("</RSAKeyValue>");

        return sb.toString();
    }

    static String getPublicKeyAsXml(PublicKey publicKey)throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPublicKeySpec spec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>" + NL);
        sb.append(getElement("Modulus", spec.getModulus()));
        sb.append(getElement("Exponent", spec.getPublicExponent()));
        sb.append("</RSAKeyValue>");

        return sb.toString();
    }

    static String getElement(String name, BigInteger bigInt) throws Exception {
        byte[] bytesFromBigInt = getBytesFromBigInt(bigInt);
        String elementContent = getBase64(bytesFromBigInt);
        return String.format("  <%s>%s</%s>%s", name, elementContent, name, NL);
    }

    static byte[] getBytesFromBigInt(BigInteger bigInt){
        byte[] bytes = bigInt.toByteArray();
        int length = bytes.length;

        // This is a bit ugly.  I'm not 100% sure of this but I presume
        // that as Java represents the values using BigIntegers, which are
        // signed, the byte representation contains an 'extra' byte that
        // contains the bit which indicates the sign.
        //
        // In any case, it is creates arrays of 129 bytes rather than the
        // expected 128 bytes.  So if it the array's length is odd and the
        // leading byte is zero then trim the leading byte.
        if(length % 2 != 0 && bytes[0] == 0) {
            bytes = Arrays.copyOfRange(bytes, 1, length);
        }

        return bytes;
    }

    static String getBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    static void writeFile(String text, String filename)throws Exception{
        try(PrintWriter writer = new PrintWriter(filename)){
            writer.write(text);
        }
    }

    static void print(String line){
        System.out.println(line);
    }
}
