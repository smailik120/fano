import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class Main {
	public static StringBuffer convertToBitString(int i) {
		StringBuffer result = new StringBuffer();
		while (i != 0) {
			result = result.append(Integer.toString(i % 2));
			i = i / 2;
		}
		return result.reverse();
	}

	public static int convertToInt(StringBuffer str) {
		int k = 0;
		for (int i = 0; i < str.length(); i++) {
			k = k + (int) Math.pow(2, str.length() - i - 1) * (str.charAt(i) - 48);
		}
		return k;
	}

	public static boolean proof(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static ArrayList<Character> convertSequence(ArrayList<StringBuffer> str) {
		int b = 0;
		int position = 7;
		int ost = 0;
		ArrayList<Character> array = new ArrayList<Character>();
		for (int i = 0; i < str.size(); i++) {
			StringBuffer current = str.get(i);
			for (int j = 0; j < current.length(); j++) {
				if (current.charAt(j) == '1') {
					b = b | (1 << position);
					position--;
				} else {
					b = b | (0 << position);
					position--;
				}
				if (position == -1 || ((i == str.size() - 1) && j == current.length() - 1 && position != -1)) {
					char result = (char) b;
					array.add(result);
					if (i == str.size() - 1 && j == current.length() - 1 && position != -1) {
						ost = position + 1;
					}
					b = 0;
					position = 7;
				}
			}
		}
		array.add((char) ost);
		return array;
	}

	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		Map<String, Character> unzippedMap = new HashMap<String, Character>();
		while (true) {
			System.out.println("please enter 1 if you want zip or enter 2 if you want unzip");
			String choose;
			choose = sc.next();
			if (choose.equals("1")) {
				Table table = new Table();
				ArrayList<Character> input = new ArrayList<Character>();
				Stack<Table> stack = new Stack<Table>();
				char s;// current symbol that was reading from file
				Map<Character, StringBuffer> map = new TreeMap<Character, StringBuffer>();
				ArrayList<Character> text = new ArrayList<Character>();
				System.out.println("please enter path to input file for zipping");
				String pathRead = sc.next();
				while (!proof(pathRead)) {
					System.out.println("please enter correct name for file");
					pathRead = sc.next();
				}
				FileReader reader = new FileReader(pathRead);
				System.out.println("please enter path to outputfile");
				String pathWrite = sc.next();
				Path path = Paths.get(pathWrite);
				while (!Files.exists(path)) {
					pathWrite = sc.next();
				}
				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pathWrite));
				long start = System.currentTimeMillis();
				int c;
				while ((c = reader.read()) != -1) {
					s = (char) c;
					text.add(s);
					input.add(s);
					if (table.containIn(s) == false) {
						table.add(new Pair<Character, Integer>(s, 1));
						map.put(s, new StringBuffer(""));
					} else {
						table.changePair((char) c);
					}
				}
				table.sort();
				stack.push(table);
				while (!stack.isEmpty()) {
					Table tab = stack.pop();
					int separateIndex = tab.separate();
					Table first = new Table();
					Table second = new Table();
					if (tab.size() >= 2) {
						first = tab.getTable(0, separateIndex);
						second = tab.getTable(separateIndex + 1, tab.size() - 1);
					}
					if (tab.size() == 1) {
						map.put(tab.get(0).getFirst(), map.get(tab.get(0).getFirst()).append("0"));
					}
					for (int i = 0; i < first.size(); i++) {
						map.put(first.get(i).getFirst(), map.get(first.get(i).getFirst()).append("0"));
					}
					for (int i = 0; i < second.size(); i++) {
						map.put(second.get(i).getFirst(), map.get(second.get(i).getFirst()).append("1"));
					}
					if (first.size() > 1) {
						stack.push(first);
					}
					if (second.size() > 1) {
						stack.push(second);
					}
				}
				ArrayList<StringBuffer> str = new ArrayList<StringBuffer>();
				writer.write(map.size());//size table
				for (Character key : map.keySet()) {
					StringBuffer bit;
					StringBuffer pres = map.get(key);
					StringBuffer len = convertToBitString(pres.length());
					int k = 0;
					for (int i = 0; i < pres.length(); i++) {
						int t = (int) Math.pow(2, pres.length() - i);
						k = t + k;
					}
					StringBuffer code = pres;
					Byte sy = ((Byte) (Character.toString(key).getBytes()[0]));
					int t = sy & 0xFF;
					bit = convertToBitString(t);
					k = 0;
					int counter = 0;
					StringBuffer zero = new StringBuffer();
					while (counter + bit.length() <= 7) {
						zero.append("0");
						counter++;
					}
					counter = 0;
					bit = zero.append(bit);
					zero = new StringBuffer("");
					while (counter + len.length() <= 7) {
						zero.append("0");
						counter++;
					}
					counter = 0;
					len = zero.append(len);
					str.add(bit);
					str.add(len);
					str.add(code);
				} // write table
				ArrayList<Character> array;
				for (int i = 0; i < text.size(); i++) {
					str.add(map.get(text.get(i)));
				}
				array = convertSequence(str);
				for (int i = 0; i < array.size(); i++) {
					writer.write(array.get(i));
				}
				writer.flush();
				writer.close();
				long finish = System.currentTimeMillis() - start;
				double time = (double) finish / 1000;
				long sizeInput = new File(pathRead).length();
				System.out.println("time execute for zipping=" + time + "seconds");
				System.out.println("size input file = " + new File(pathRead).length() + "bytes");
				System.out.println("size output file = " + new File(pathWrite).length() + "bytes");
				System.out.println("speed coding bytes in second = " + sizeInput / time);
			} else if (choose.equals("2")) {
				System.out.println("please enter path to input file for unzipping");
				String pathRead = sc.next();
				while (!proof(pathRead)) {
					System.out.println("please enter correct name for file");
					pathRead = sc.next();
				}
				DataInputStream reader = new DataInputStream(new FileInputStream(pathRead));
				System.out.println("please enter output file for unzipping");
				String pathWrite = sc.next();
				Path path = Paths.get(pathWrite);
				while (!Files.exists(path)) {
					pathWrite = sc.next();
				}
				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pathWrite));
				long start = System.currentTimeMillis();
				Byte ch;
				int lenMainString = 0;
				int lenTable = 0;
				int counter = 0;
				int codeLen = 0;
				int zeroCounter = 0;
				char currentSymbol = '0';
				int symbCounter = 0;
				int numberSymbols = 0;
				int counterSymbols = 0;
				StringBuffer lastByte = new StringBuffer();
				StringBuffer mainString = new StringBuffer("");
				while (reader.available() > 0) {
					ch = reader.readByte();
					if (counter == 0) {
						lenTable = ch & 0xFF;
					} else {
						int cur = ch & 0xFF;
						StringBuffer temp = convertToBitString(cur);
						StringBuffer zeroString = new StringBuffer("");
						while (temp.length() + zeroCounter < 8) {
							zeroCounter++;
							zeroString.append("0");
						}
						temp = zeroString.append(temp);
						mainString.append(temp);
						zeroCounter = 0;
						lastByte = temp;
					}
					counter++;
				}
				reader.close();
				numberSymbols = convertToInt(new StringBuffer(lastByte.toString()));
				lenMainString = mainString.length();
				StringBuffer current = new StringBuffer("");
				for (int i = 0; i < lenMainString - numberSymbols - 8; i++) {
					current.append(mainString.charAt(i));
					if (symbCounter / 3 < lenTable) {
						if (current.length() % 8 == 0 && symbCounter % 3 == 0) {
							currentSymbol = (char) convertToInt(current);
							current = new StringBuffer("");
							symbCounter++;
						} else if (current.length() % 8 == 0 && symbCounter % 3 == 1) {
							codeLen = convertToInt(current);
							current = new StringBuffer("");
							symbCounter++;
						} else if (symbCounter % 3 == 2 && current.length() == codeLen && codeLen != 0) {
							unzippedMap.put(current.toString(), currentSymbol);
							current = new StringBuffer("");
							symbCounter++;
							codeLen = 0;
						}
					} else {
						if(unzippedMap.get(current.toString()) != null) {
								writer.write(unzippedMap.get(current.toString()));
								current = new StringBuffer("");
								counterSymbols++;
							}
						}
					}
				writer.flush();
				writer.close();
				long finish = System.currentTimeMillis() - start;
				double time = (double) finish / 1000;
				long sizeInput = new File(pathRead).length();
				System.out.println("time execute for unzipping=" + time + "seconds");
				System.out.println("size input file = " + new File(pathRead).length() + "bytes");
				System.out.println("size output file = " + new File(pathWrite).length() + "bytes");
				System.out.println("speed coding bytes in second = " + sizeInput / time);
			} else {
				System.out.println("sorry but your try is not success, please enter 1 or 2");
			}
		}
	}
}
