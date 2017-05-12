package main;

import java.util.Scanner;

public class driver {

	public static void main(String[] args) {
		DigitalSignature ds;

		while(true) {
			Scanner sc = new Scanner(System.in);
			printMenu();
			int selection = sc.nextInt();
			
			switch(selection){
				case 1:
					KeyGen kg = new KeyGen();
					kg.createKeys();
					break;
				case 2:
					ds = new DigitalSignature("test.txt");
					ds.prepareFile();
					break;
				case 3:
					ds = new DigitalSignature("test.txt");
					ds.readFile();
					break;
				case 0:
					sc.close();
					return;
				default:
					sc.close();
			}
		}
		
	}
	
	static void printMenu(){
		System.out.println("Select:\n"+
						   "Press 1 to generate keys.\n" +
						   "Press 2 to Prepare Message to send.\n" +
						   "Press 3 to Read Message.\n" +
						   "Press 0 to close.");
	}
	


}
