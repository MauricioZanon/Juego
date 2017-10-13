package console;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Console {
	
	private static LinkedList<Message> messages = new LinkedList<Message>();
	
	public static void add(Message m){
		if(!messages.isEmpty() && messages.getFirst().text.equals(m.text)){
			messages.getFirst().repeat();
		}
		else{
			messages.addFirst(m);
		}
	}

	public static List<Message> getMessages() {
		return messages;
	}
	
	public static ArrayList<Message> getLattestMessages(){
		ArrayList<Message> list = new ArrayList<Message>();
		for(Message m : messages){
			list.add(m);
			if(list.size() == 25){
				return list;
			}
		}
		return list;
	}
	
}
