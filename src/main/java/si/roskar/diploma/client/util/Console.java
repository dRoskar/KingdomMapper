package si.roskar.diploma.client.util;

public class Console{
	private Console(){
		
	}
	
	public static native void print(String text)
	/*-{
		console.log(text);
	}-*/;
}
