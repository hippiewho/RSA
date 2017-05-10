package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;

public class DigitalSignature {
	String FILE_NAME;
	String OUTPUT_FILE_NAME;

	public void prepareFile(){
		
		try {
			File file = new File(FILE_NAME);
			File output = new File(OUTPUT_FILE_NAME);
			FileInputStream fs = new FileInputStream(file);
			FileOutputStream ofs = new FileOutputStream(output);

			byte[] b = new byte[(int)file.length()];
			
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			fs.read(b);
			
			digest.update(b);
			
			byte[] digestedArray = digest.digest();
			BigInteger bi = new BigInteger(digestedArray);
			
			KeyGen key = new KeyGen();
			key.retrieveKeys();
			BigInteger signature = key.getE();
			//digest^signature mod e
			BigInteger signed = bi.modPow(signature, key.getE());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
}
