# Java to .Net Signature

This was motivated by a question on StackOverflow: [Exporting RSA key object to XML in Java][SO]

It contains a basic sample of creating a cryptographic signature on Java and verifying 
it on .Net

  - CreateKeysJ: Use Java to create keys in Java and .Net formats. _[code](/CreateKeysJ/src/CreateKeysJ.java)_
  - SignMessageJ: Use Jave to create a signature using the private key. _[code](/SignMessageJ/src/SignMessageJ.java)_
  - VerifyMessageN: Use .Net to verifiy the message's signature. _[code](/VerifyMessageN/Program.cs)_


## WARNING

This is intended to demonstrate how 'on my machine' I got it working and
how I dealt with the platform-to-platfrom issues in the hope it might be
useful to others.  It is not intended as production code. I'm not a
security dude.  If I were I would probably spit out my coffee at how
insecure this code is with private keys being written in plain text and 
not clensing memomory where it was stored.  There are probably 57 other
problems I've no idea about.


## Environment

Windows 10.0 (Build 10240)  
Java SE Development Kit 8 Update 66 (64bit)  
.Net Framework 4.6  

[SO]:https://stackoverflow.com/questions/5178700/exporting-rsa-key-object-to-xml-in-java/33576188#33576188
