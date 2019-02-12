import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
		Map<Character, String> map = new TreeMap<Character, String>();
		try(FileReader reader = new FileReader(sc.nextLine()))
	    {
	        int c;
	        while((c=reader.read())!=-1) {
	        	char s = (char) c;
	        	input.add(s);
	           if(table.containIn(s) == false) {
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
			for(int i = 0; i < first.size(); i++) {
				map.put(first.get(i).getFirst(), map.get(first.get(i).getFirst()).concat("0"));
			}
			for(int i = 0; i < second.size(); i++) {
				map.put(second.get(i).getFirst(), map.get(second.get(i).getFirst()).concat("1"));
			}
			if(first.size() != 1) {
				stack.push(first);
			}
			if(second.size() != 1) {
				stack.push(second);
			}
		}
		System.out.println(map.toString());
		System.out.println("please enter path to outputfile");
		try(FileWriter writer = new FileWriter(sc.nextLine()))
        {
            Byte b = 0; 
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        } 
	}

}
