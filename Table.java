import java.util.ArrayList;

public class Table extends ArrayList<Pair<Character, Integer>> {
	public void sort() {
		for(int i = 0; i < this.size(); i++) {
			for(int j = i; j < this.size(); j++) {
				if (this.get(j).getSecond().compareTo(this.get(i).getSecond()) == 1) {
					Pair<Character, Integer> temp = this.get(j);
					this.set(j, this.get(i));
					this.set(i, temp);
				}
			}
		}
	}
	
	public void changePair(Character ch) {
		for(Pair<Character, Integer> pairFirst: this) {
			if(pairFirst.getFirst().equals(ch)) {
				pairFirst.setSecond(pairFirst.getSecond() + 1);
				break;
			}
		}
	}
	
	public boolean containIn(Character d) {
		for(Pair<Character, Integer> pairFirst: this) {
			if(pairFirst.getFirst().equals(d)) {
				return true;
			}
		}
		return false;
	}
	
	public int sum(int begin, int end) {
		int sum = 0;
		for(int i = begin; i <= end; i++) {
			sum = sum + this.get(i).getSecond();
		}
		return sum;
	}
	
	public int separate() {
		int min = 10000000;
		int index = 0;
		for(int i = 0; i < this.size(); i++) {
			if(Math.abs(sum(0,i) - sum(i + 1, this.size() -1)) < min) {
				min = Math.abs(sum(0,i) - sum(i + 1, this.size() -1));
				index = i;
			}
		}
		return index;
	}
	
	public Table getTable(int begin, int end) {
		Table table = new Table();
		for(int i = begin; i <= end; i++) {
			table.add(this.get(i));
		}
		return table;
	}
}
