package main;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class KeyGen {

	private BigInteger p,
					   q,
					   n,
					   d,
					   e,
					   totient_n,
					   one;
	private final int BIT_SIZE = 512;
	private final String PUBLIC_FILE_NAME = "pubkey.rsa";
	private final String PRIVATE_FILE_NAME = "privkey.rsa";

	public boolean retrieveKeys(){
		return inputFromFile();
	}

	public boolean createKeys(){
		Random rand = new Random();
		p = BigInteger.probablePrime(BIT_SIZE, rand);
		q = BigInteger.probablePrime(BIT_SIZE, rand);
		one = new BigInteger("1");
		n = p.multiply(q);

		totient_n = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));

		//Pick e to be a random prime between 1 and �(n), such that gcd(e, �(n)) = . e 
		//Calculate  d = e-1 mod �(n)  
		while(!(e = new BigInteger(BIT_SIZE, rand)).gcd(totient_n).equals(one)){}
		
		try{
		d = e.modInverse(totient_n);
		} catch(Exception e){
			System.out.println(e.toString());
		}
		System.out.println("p =    " + p + "\n" +
				"q =    " + q + "\n" +
				"n =    " + n + "\n" +
				"(t)n = " + totient_n + "\n" +
				"e =    " + e + "\n" +
				"d =    " + d + "\n");
		return outputToFile(e, d, n);
	}
	
	private boolean outputToFile(BigInteger e , BigInteger d, BigInteger n){
		try {
			FileOutputStream publicfs = new FileOutputStream(PUBLIC_FILE_NAME);
			FileOutputStream privatefs = new FileOutputStream(PRIVATE_FILE_NAME);
			
			ObjectOutputStream publicos = new ObjectOutputStream(publicfs);
			ObjectOutputStream privateos = new ObjectOutputStream(privatefs);
			
			publicos.writeObject(e);
			publicos.writeObject(n);
			
			privateos.writeObject(d);
			privateos.writeObject(n);
			
			publicos.close();
			privateos.close();
			return true;
		} catch (FileNotFoundException exception) {
			System.out.println("File Not Found.\nError:");
			exception.printStackTrace();
			return false;
		} catch (IOException exception) {
			System.out.println("Output Error.\nError:");
			exception.printStackTrace();
			return false;
		}
	}

	private boolean inputFromFile(){
		try {
			FileInputStream publicfs = new FileInputStream(PUBLIC_FILE_NAME);
			FileInputStream privatefs = new FileInputStream(PRIVATE_FILE_NAME);
			
			ObjectInputStream publicis = new ObjectInputStream(publicfs);
			ObjectInputStream privateis = new ObjectInputStream(privatefs);
			
			this.e = (BigInteger) publicis.readObject();
			this.n = (BigInteger) publicis.readObject();
			
			this.d = (BigInteger) privateis.readObject();
			this.n = (BigInteger) privateis.readObject();
			
			publicis.close();
			privateis.close();
			return true;
		} catch (FileNotFoundException exception) {
			System.out.println("One of the files could not be found.\nError:");
			exception.printStackTrace();
			return false;
		} catch (IOException exception) {
			System.out.println("Input Error.\nError:");
			exception.printStackTrace();
			return false;
		} catch (ClassNotFoundException exception){
			System.out.println("Class Not Found.\nError:");
			exception.printStackTrace();
			return false;
		}
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
			
		if(this.q != null){
			output.append("q = ");
			output.append(this.q);
		} else {
			output.append("q = null");		
		}
		
		if(this.n != null){
			output.append(" n = ");
			output.append(this.n);
		} else {
			output.append(" n = null");		
		}
		
		if(this.d != null){
			output.append(" d = ");
			output.append(this.d);
		} else {
			output.append(" d = null");		
		}
		
		return output.toString();
	}
	
	public BigInteger getE(){
		return e;
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getD() {
		return d;
	}
}
