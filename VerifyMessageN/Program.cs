// WARNING:  This is demo code.  It is NOT Secure code.
//
// Looks in the current directory for a public key file, a message file and a 
// signature file.  If all files are found then attempts to verify the 
// message's signature using the public key. 

using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace VerifyMessageN
{
    class Program
    {
        static readonly string XML_PUBLIC_FILENAME = "XmlPublicKey.txt";
        static readonly string MESSAGE_FILENAME = "Message.txt";
        static readonly string SIGNATURE_FILENAME = "Signature.txt";

        static FileInfo keyFile;
        static FileInfo messageFile;
        static FileInfo signatureFile;

        static void Main(string[] args)
        {
            if (!SetFiles())
            {
                Print("Press any key to continue.");
                Console.ReadKey();
                return;
            }

            Print("Reading content of Public key file.");
            String keyText = File.ReadAllText(keyFile.FullName);
            Print("Got key text:");
            Print(keyText);

            Print("Getting SecurityProvider from encoded text");
            var provider = GetSecurityProvider(keyText);

            Print("Reading content of Signature file.");
            String signature = File.ReadAllText(signatureFile.FullName);
            Print("Got Signature:");
            Print(signature);

            Print("Reading content of Message file.");
            String message = File.ReadAllText(messageFile.FullName);
            Print("Got Message:");
            Print(message);

            Print("About to verify message.");

            var isValid = VerifyMessagge(provider, message, signature);
            if (isValid)
                LaunchMissiles();
            else
                DisplayFailure();

            Print("");
            Print("Done.  Press any key to continue.");
            Console.ReadKey();
        }

        private static RSACryptoServiceProvider GetSecurityProvider(string keyText)
        {
            var rsa = new RSACryptoServiceProvider();
            rsa.FromXmlString(keyText);
            return rsa;
        }

        private static bool VerifyMessagge(RSACryptoServiceProvider provider, string message, string signature)
        {
            var algorithmId = CryptoConfig.MapNameToOID("SHA512");
            var hasher = SHA512.Create();
            var messageBytes = Encoding.ASCII.GetBytes(message);
            hasher.TransformFinalBlock(messageBytes, 0, messageBytes.Length);
            var signatureBytes = Convert.FromBase64String(signature);
            var isValid = provider.VerifyHash(hasher.Hash, algorithmId, signatureBytes);
            return isValid;
        }

        static bool SetFiles()
        {
            keyFile = new FileInfo(XML_PUBLIC_FILENAME);
            if (!keyFile.Exists)
            {
                Print("Exiting early because couldn't find the Key file at:");
                Print(keyFile.FullName);
                return false;
            }

            messageFile = new FileInfo(MESSAGE_FILENAME);
            if (!messageFile.Exists)
            {
                Print("Exiting early because couldn't find the Message file at:");
                Print(messageFile.FullName);
                return false;
            }

            signatureFile = new FileInfo(SIGNATURE_FILENAME);
            if (!signatureFile.Exists)
            {
                Print("Exiting early because couldn't find the Signature file at:");
                Print(signatureFile.FullName);
                return false;
            }
            return true;
        }

        static void LaunchMissiles()
        {
            Print("Message Verified:");
            for (int i = 1; i < 11; i++)
            {
                Print("Launching Sortie " + i);
                Task.Delay(300).Wait();
            }
        }

        static void DisplayFailure()
        {
            Print("*****************************");
            Print("*                           *");
            Print("*  VERIFCATION  ! FAILED !  *");
            Print("*                           *");
            Print("*****************************");
        }

        static void Print(string line)
        {
            Console.WriteLine(line);
        }
    }
}
