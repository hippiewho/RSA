package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.math.BigInteger;
import java.security.*;

public class DigitalSignature {
	String FILE_NAME ;
	String SIGNED_FILE_NAME;
	
	public DigitalSignature(String FileName){
		this.FILE_NAME = FileName;
		this.SIGNED_FILE_NAME =  FILE_NAME + ".signed";
	}

	public void prepareFile(){
		
		File file;
		File output;
		FileInputStream fis;
		FileOutputStream fos;
		ObjectOutputStream OOS;
		
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Prepare Files I/O
			file = new File(FILE_NAME);
			fis = new FileInputStream(file);

			output = new File(SIGNED_FILE_NAME);
			fos = new FileOutputStream(output);
			OOS = new ObjectOutputStream(fos);
			OOS.flush();

			
			byte[] b = new byte[(int)file.length()];
			fis.read(b);
						
			byte[] digestedArray = digest.digest(b);
			BigInteger DigestedMessage = new BigInteger(digestedArray);
			
			KeyGen key = new KeyGen();
			key.retrieveKeys();
			//digest^signature mod e
			BigInteger signed = DigestedMessage.modPow(key.getD(), key.getN());
			System.out.println("Signature: " + signed +
					         "\nMessage: " + new BigInteger(b) +
					         "\nDigestedMessage: " + DigestedMessage);
			OOS.writeObject(signed);
			OOS.writeObject(new BigInteger(b));
			// Close all open I/O 
//			fis.close();
//			fos.close();
			OOS.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void readFile(){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			KeyGen kg = new KeyGen();
			kg.retrieveKeys();
			// Prepare Files I/O
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(SIGNED_FILE_NAME)));
			if(ois != null){
				System.out.println("OIS is open.");
			}
			BigInteger signed = (BigInteger) ois.readObject();
			BigInteger message = (BigInteger) ois.readObject();

			
			BigInteger messageSigned = signed.modPow(kg.getE(), kg.getN());
			
			byte[] digestedmessage = message.toByteArray();
			digest.update(digestedmessage);
			byte[] digestedArray = digest.digest();
			BigInteger DigestedMessage = new BigInteger(digestedArray);

			System.out.println("Signature:      " + signed +
							 "\nMessage:        " + message + 
							 "\nMessage Signed: " + messageSigned +
							 "\nMessage Digest: " + DigestedMessage);

			System.out.println("M' mod n = " + messageSigned.mod(kg.getN()));
			System.out.println("DM mod n = " + DigestedMessage.mod(kg.getN()));

			if(messageSigned.equals(DigestedMessage.mod(kg.getN()))){
				System.out.println("True");
			} else {
				System.out.println("False");
			}
			
			// Close all open I/O 
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(OptionalDataException e){
			e.printStackTrace();
			System.out.println(e.length + "\n"+ e.eof);
		} catch (IOException e){
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
