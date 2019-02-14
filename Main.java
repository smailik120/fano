import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class Main {
	public static StringBuffer convertToBitString(int i) {
		StringBuffer result = new StringBuffer();
		while(i != 0) {
			result = result.append(Integer.toString(i % 2)); 
			i = i / 2;
		}
		return result.reverse();
	}
	
	public static int convertToInt(StringBuffer str) {
		int k = 0;
		for(int i = 0; i < str.length(); i++) {
			k = k + (int) Math.pow(2, str.length() - i - 1) * (str.charAt(i) -48);
		}
		return k;
	}
	
	public static ArrayList<Character> convertSequence(ArrayList<StringBuffer> str) {
		int b = 0;
		int position = 7;
		ArrayList<Character> array = new ArrayList<Character>();
		for (int i = 0; i < str.size(); i++) {
			StringBuffer current = str.get(i);
        	for (int j = 0; j < current.length(); j++) {
        		if (current.charAt(j) == '1') {
        			b = b | (1 << position);
        			//System.out.println(b);
        			position--;
        		}
        		else {
        			b = b | (0 << position);
        			position--;
        		}
        		if (position == -1 || ((i == str.size() - 1) && j == current.length() - 1 && position != -1)) {
        			char result = (char) b;
        			//System.out.println(result);
        			//writer.write(result);
        			array.add(result);
        			b = 0;
        			position = 7;
        		}
        	}
        }
		return array;
	}
	public static void main(String args[]) {
		Table table = new Table();
		System.out.println("please enter path to file");
		ArrayList<Character> input = new ArrayList<Character>();
		Scanner sc = new Scanner(System.in);
		Stack<Table> stack = new Stack<Table>();
		char s;//current symbol that was reading from file
		Map<Character, StringBuffer> map = new TreeMap<Character, StringBuffer>();
		Map<Character, StringBuffer> unzippedMap = new TreeMap<Character, StringBuffer>();
		ArrayList<Character> text = new ArrayList<Character>();
		try(FileReader reader = new FileReader("C:\\test\\fano.txt"))
	    {
	        int c;
	        while((c=reader.read())!=-1) {
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
	        for(int index = 0; index < table.size(); index++) {
	            System.out.println(table.get(index).getFirst() + " " + table.get(index).getSecond());
	        }
	    }
	    catch(IOException ex){
	        System.out.println(ex.getMessage());
	    } 
		stack.push(table);
		while(!stack.isEmpty()) {
			Table tab = stack.pop();
			int separateIndex = tab.separate();
			Table first = tab.getTable(0, separateIndex);
			Table second = tab.getTable(separateIndex + 1, tab.size() - 1);
			for (int i = 0; i < first.size(); i++) {
				map.put(first.get(i).getFirst(), map.get(first.get(i).getFirst()).append("0"));
			}
			for (int i = 0; i < second.size(); i++) {
				map.put(second.get(i).getFirst(), map.get(second.get(i).getFirst()).append("1"));
			}
			if (first.size() != 1) {
				stack.push(first);
			}
			if (second.size() != 1) {
				stack.push(second);
			}
		}
		System.out.println("please enter path to outputfile");
		System.out.println(map.toString());
		try(BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream("C:\\test\\output.txt")))
        {
			 ArrayList<StringBuffer> str = new ArrayList<StringBuffer>();
			writer.write(map.size());
            for (Character key: map.keySet()) {
            	StringBuffer bit;
    			StringBuffer pres =  map.get(key);
    			StringBuffer len = convertToBitString(pres.length());
    			//writer.write(array.get(0));
    			int k = 0;
    			for(int i = 0; i < pres.length(); i++) {
    				int t = (int) Math.pow(2,pres.length() - i);
    				k = t + k;
    			}
    			StringBuffer code = pres;
    			Byte c = ((Byte) (Character.toString(key).getBytes()[0]));
    			int t = c& 0xFF;
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
    			System.out.println("pres" + bit + " " + len + " " + code);
    		}//write table
            ArrayList<Character> array;
            //for(int i = 0; i < array.size(); i++) {
            	//writer.write(array.get(i));
           // }
            for(int i = 0; i < text.size(); i++) {
            	str.add(map.get(text.get(i)));
            }
            array = convertSequence(str);
            for(int i = 0; i < array.size(); i++) {
            	writer.write(array.get(i));
            }
            writer.write(input.size());
            writer.flush();
            writer.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
		
		try(DataInputStream reader = new DataInputStream(new FileInputStream("C:\\test\\output.txt"))) {
			System.out.println(reader.available());
			Byte ch;
			ArrayList<Integer> list = new ArrayList<Integer>();
			int lenMainString = 0;
			int lenTable = 0;
			int counter = 0; 
			int codeLen = 0;
			int zeroCounter = 0;
			char currentSymbol ='0';
			int symbCounter = 0;
			int numberSymbols = 0;
			int counterSymbols = 0;
			StringBuffer mainString = new StringBuffer("");
			StringBuffer tableBit = new StringBuffer("");
	        while(reader.available() > 0) {
	        	ch = reader.readByte();
	        	if (counter == 0) {
	        		lenTable = ch & 0xFF;
	        	}
	        	else {
	        		int cur = ch & 0xFF;
	        		StringBuffer temp = convertToBitString(cur);
		        	tableBit.append(convertToBitString(cur));
		        	StringBuffer zeroString = new StringBuffer("");
		        	while(temp.length() + zeroCounter < 8) {
		        		zeroCounter++;
		        		zeroString.append("0");
		        	}
		        	temp = zeroString.append(temp);
		        	mainString.append(temp);
		        	zeroCounter = 0;
	        	}
	        	counter++;
	        	System.out.println(ch & 0xFF);
	        }
	        reader.close();
	        numberSymbols = convertToInt(new StringBuffer(mainString.substring(mainString.length() - 8)));
	        lenMainString = mainString.length();
	        StringBuffer current = new StringBuffer("");
	        System.out.println(mainString.toString());
	        for (int i = 0; i < lenMainString - 1; i++) {
	        	current.append(mainString.charAt(i));
	        	if (symbCounter / 3 < lenTable) {
	        		if (current.length() % 8 == 0 && symbCounter % 3 == 0) {
	        			currentSymbol = (char) convertToInt(current);
	        			current = new StringBuffer("");
	        			symbCounter++;
	        		}
	        	
	        		else if (current.length() % 8 == 0 && symbCounter % 3 == 1) {
	        			codeLen =  convertToInt(current);
	        			current = new StringBuffer("");
	        			symbCounter++;
	        		}
	        		else if (symbCounter % 3 == 2 && current.length() == codeLen && codeLen != 0) {
	        			unzippedMap.put(currentSymbol, current);
	        			current = new StringBuffer("");
	        			symbCounter++;
	        			codeLen = 0;
	        		}
	        	}
	        	else {
	        		for(Character t: unzippedMap.keySet()) {
	        			if(unzippedMap.get(t).toString().equals(current.toString()) && counterSymbols < numberSymbols) {
	        				System.out.println(t);
	        				current = new StringBuffer("");
	        				counterSymbols++;
	        				break;
	        			}
	        		}
	        	}
	        }
	     }
		 catch(IOException ex){
	            System.out.println(ex.getMessage());
	        }
	}

}
