package console;

public class Message {
	
	protected String text;
	private int timesRepeated = 1;
	
	public Message(String m){
		text = m;
	}
	
	public void addText(String t){
		text += " " + t;
	}

	public String getText() {
		return timesRepeated > 1 ? text + "[x" + timesRepeated + "]" : text;
	}

	public void repeat(){
		timesRepeated++;
	}
}
