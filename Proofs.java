import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Proofs {
	public static boolean proof(String path) {
		File file = new File(path);
		return file.exists();
	}
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter first file");
		String pathRead = sc.next();
		while (!proof(pathRead)) {
			System.out.println("please enter correct name for file");
			pathRead = sc.next();
		}
		FileReader readerFrom = new FileReader(pathRead);
		System.out.println("please enter second file");
		pathRead = sc.next();
		while (!proof(pathRead)) {
			System.out.println("please enter correct name for file");
			pathRead = sc.next();
		}
		FileReader readerTo = new FileReader(pathRead);
		int c;
		int k;
		int counter = 0;
		int proof = 0;
		while (((c = readerFrom.read()) != -1) &&  ((k = readerTo.read()) != -1)) {
			if (c == k) {
				proof++;
			}
			else {
				System.out.println(counter + " " + c + " " + k);
			}
			counter++;
		}
		readerFrom.close();
		readerTo.close();
		sc.close();
		System.out.println(counter + " " + proof);
		if(proof == counter) {
			System.out.println("files equals");
		}
	}
}
