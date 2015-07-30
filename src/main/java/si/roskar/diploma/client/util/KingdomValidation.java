package si.roskar.diploma.client.util;

public class KingdomValidation{
	private boolean isValid = false;
	private String invalidMessage = null;
	
	public KingdomValidation(){
		
	}

	public KingdomValidation(boolean isValid, String invalidMessage){
		super();
		this.isValid = isValid;
		this.invalidMessage = invalidMessage;
	}

	public boolean isValid(){
		return isValid;
	}

	public void setValid(boolean isValid){
		this.isValid = isValid;
	}

	public String getInvalidMessage(){
		return invalidMessage;
	}

	public void setInvalidMessage(String invalidMessage){
		this.invalidMessage = invalidMessage;
	}
}
