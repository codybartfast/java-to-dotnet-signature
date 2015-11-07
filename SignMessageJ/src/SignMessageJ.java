// WARNING:  This is demo code.  It is NOT Secure code.
//
// If a private key file is found in the current directory then create a
// new message, create a signature using the private key and write the
// message and signature to the current directory.

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


public class SignMessageJ {

    static final String KEY_ALGORITHM = "RSA";
    static final String SIGNATURE_ALGORITHM = "SHA512withRSA";

    static final String ENCODED_PRIVATE_FILENAME = "EncodedPrivateKey.txt";
    static final String MESSAGE_FILENAME = "Message.txt";
    static final String SIGNATURE_FILENAME = "Signature.txt";
    static final String NL = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        File keyFile = new File(ENCODED_PRIVATE_FILENAME);
        String fullPath = keyFile.getCanonicalPath();
        if(! keyFile.exists()) {
            print("Exiting early because couldn't find a key file at: "
                    + NL + fullPath);
            return;
        }

        print("Reading content of PRIVATE key file.");
        String keyText = readPrivateKey(fullPath);
        print("Got key text:" + NL + keyText);

        print("Getting key from encoded text");
        PrivateKey key = getKeyFromEncodedText(keyText);

        print("About to write Message.");
        String message = createMessage();
        print("Message:" + NL + message);
        writeFile(message, MESSAGE_FILENAME);

        print("About to write Signature.");
        String signature = getSignatureText(key, message);
        print("Signature:" + NL + signature);
        writeFile(signature, SIGNATURE_FILENAME);
    }

    static PrivateKey getKeyFromEncodedText(String encodedText) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(encodedText);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey key = factory.generatePrivate(spec);
        return key;
    }

    static String getSignatureText(PrivateKey key, String message) throws Exception {
        byte[] bytes = message.getBytes();
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(key, new SecureRandom());
        signature.update(bytes);
        byte[] signatureBytes = signature.sign();
        String signatureText = Base64.getEncoder().encodeToString(signatureBytes);
        return signatureText;
    }

    static String createMessage() throws Exception{
        // Not using NL here as don't want message to change depening on platform.
        String message =
            "============================================================================\r\n"
            + " EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM - EAM\r\n"
            + "FROM: NNMC.Java\r\n"
            + "TO: U.S.S Alabama.Net\r\n"
            + "----------------------------------------------------------------------------\r\n"
            + "Rebel-controlled missiles being fueled. Launch codes compromised, dissidents \r\n"
            + "threaten to launch at continental United Servers, set DEFCON 2. Immediately \r\n"
            + "launch ten Trident missile sorties.\r\n"
            + "============================================================================\r\n";
        return message;
    }

    static String readPrivateKey(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path).get(0);
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
