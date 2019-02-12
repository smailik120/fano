import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class Main {
	public static void main(String args[]) {
		Table table = new Table();
		System.out.println("please enter path to file");
		ArrayList<Character> input = new ArrayList<Character>();
		Scanner sc = new Scanner(System.in);
		Stack<Table> stack = new Stack<Table>();
		char s;//current symbol that was reading from file
		Map<Character, String> map = new TreeMap<Character, String>();
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
	        	   map.put(s, "");
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
				map.put(first.get(i).getFirst(), map.get(first.get(i).getFirst()).concat("0"));
			}
			for (int i = 0; i < second.size(); i++) {
				map.put(second.get(i).getFirst(), map.get(second.get(i).getFirst()).concat("1"));
			}
			if (first.size() != 1) {
				stack.push(first);
			}
			if (second.size() != 1) {
				stack.push(second);
			}
		}
		System.out.println(map.toString());
		System.out.println("please enter path to outputfile");
		try(BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream("C:\\test\\output.txt")))
        {
			int position = 7;
            int b = 0; 
            for (int i = 0; i < text.size(); i++) {
            	String current = map.get(text.get(i));
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
            		if (position == -1) {
            			short result = (short) b;
            			System.out.println(b);
            			writer.write(result);
            			b = 0;
            			position = 7;
            		}
            	}
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        } 
	}

}
