import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Generator {
	public static void main(String args[]) throws IOException {
		int min = 0;
		int max = 255;
		Random rnd = new Random(System.currentTimeMillis());
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream("C:\\test\\random.txt"));
		int number = 0;
		for(int i = 1; i <= 10000; i++) {
			number = min + rnd.nextInt(max - min + 1);
			if(number != 151 && number != 152) {
				writer.write(number);
			}
		}
		writer.close();
	}
}
