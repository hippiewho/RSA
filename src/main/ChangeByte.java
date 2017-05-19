package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Darren76
 */
public class ChangeByte {

	public static void corrupt(Scanner input) throws IOException {
		byte[] bytes;

		try {
			System.out.println("Enter the file name. ");
			String filename = input.next();

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filename)));

			// Read signature and BigInteger representation of message
			// from file
			BigInteger signed = (BigInteger) ois.readObject();
			BigInteger message = (BigInteger) ois.readObject();
			bytes = new byte[signed.toByteArray().length + message.toByteArray().length];

			System.arraycopy(signed.toByteArray(), 0, bytes, 0, signed.toByteArray().length);
			System.arraycopy(message.toByteArray(), 0, bytes, signed.toByteArray().length,
					message.toByteArray().length);

			System.out.println("Which byte would you want to replace?\nFrom 0 to " + bytes.length);
			int changeByteinput = input.nextInt();
			Random rndm = new Random();
			byte n = (byte) rndm.nextInt(126);
			bytes[changeByteinput] = n;
			ois.close();

			ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			OOS.write(bytes);
			OOS.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}