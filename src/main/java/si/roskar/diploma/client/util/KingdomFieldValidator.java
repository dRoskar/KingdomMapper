package si.roskar.diploma.client.util;

public class KingdomFieldValidator{
	
	// types
	public final static String	TYPE_USERNAME				= "username";
	public final static String	TYPE_PASSWORD				= "password";
	public final static String	TYPE_OBJECT_NAME			= "objectName";
	public final static String	TYPE_LABEL					= "description";
	public final static String	TYPE_DESCRIPTION			= "description";
	
	// default length limits
	private final static int	defaultUsernameMinLength	= 3;
	private final static int	defaultUsernameMaxLength	= 20;
	private final static int	defaultPasswordMinLength	= 6;
	private final static int	defaultPasswordMaxLength	= 60;
	private final static int	defaultObjectNameMinLength	= 3;
	private final static int	defaultObjectNameMaxLength	= 30;
	private final static int	defaultLabelMinLength		= 1;
	private final static int	defaultLabelMaxLength		= 100;
	private final static int	defaultDescriptionMinLength	= 3;
	private final static int	defaultDescriptionMaxLength	= 4000;
	
	// regex
	private final static String	REGEX_USERNAME				= "^[a-zA-Z0-9]+$";
	private final static String	REGEX_PASSWORD				= "^[a-zA-Z0-9ŠĐČĆŽšđčćž !@'\\-\\?\\*\\./_#]+$";
	private final static String	REGEX_OBJECT_NAME			= "^[a-zA-Z0-9ŠĐČĆŽšđčćž _\\-]+$";
	private final static String	REGEX_LABEL					= "^[a-zA-Z0-9ŠĐČĆŽšđčćž !@'\\-\\?\\*\\.\\\\\\&\\|\\+\\(\\)\\[\\]\\{\\}\\$€=#,/_:;]+$";
	private final static String	REGEX_DESCRIPTION			= "^[a-zA-Z0-9ŠĐČĆŽšđčćž !@'\\-\\?\\*\\.\\\\\\&\\|\\+\\(\\)\\[\\]\\{\\}\\$€=#,/_:;]+$";
	
	// messages
	private final static String	TOO_SHORT					= "Value needs to be at least %d characters long";
	private final static String	TOO_LONG					= "Value can not exceed %d characters";
	private final static String	INVALID_CHARS				= "Value contains invalid characters";
	private final static String	REQUIRED					= "This field is required";
	private final static String	TYPE_ERROR					= "ERROR: Invalid validation type";
	
	private KingdomFieldValidator(){
		
	}
	
	public static KingdomValidation validate(String input, String type, int minLength, int maxLength, boolean notNull){
		return _validate(input, type, minLength, maxLength, notNull);
	}
	
	public static KingdomValidation validate(String input, String type, boolean notNull){
		if(type.equals(TYPE_USERNAME)){
			return _validate(input, type, defaultUsernameMinLength, defaultUsernameMaxLength, notNull);
		}else if(type.equals(TYPE_PASSWORD)){
			return _validate(input, type, defaultPasswordMinLength, defaultPasswordMaxLength, notNull);
		}else if(type.equals(TYPE_OBJECT_NAME)){
			return _validate(input, type, defaultObjectNameMinLength, defaultObjectNameMaxLength, notNull);
		}else if(type.equals(TYPE_LABEL)){
			return _validate(input, type, defaultLabelMinLength, defaultLabelMaxLength, notNull);
		}else if(type.equals(TYPE_DESCRIPTION)){
			return _validate(input, type, defaultDescriptionMinLength, defaultDescriptionMaxLength, notNull);
		}
		
		return new KingdomValidation(false, TYPE_ERROR);
	}
	
	private static KingdomValidation _validate(String input, String type, int minLength, int maxLength, boolean notNull){
		
		// null validation
		if(notNull){
			if(input == null || input.equals("")){
				return new KingdomValidation(false, REQUIRED);
			}
		}else{
			if(input == null || input.equals("")){
				return new KingdomValidation(true, "");
			}
		}
		
		// length validation
		if(input.length() < minLength){
			return new KingdomValidation(false, TOO_SHORT.replaceAll("\\%d", Integer.toString(minLength)));
		}
		
		if(input.length() > maxLength){
			return new KingdomValidation(false, TOO_LONG.replaceAll("\\%d", Integer.toString(maxLength)));
		}
		
		// character validation
		if(type.equals(TYPE_USERNAME)){
			if(input.matches(REGEX_USERNAME)){
				return new KingdomValidation(true, "");
			}else{
				return new KingdomValidation(false, INVALID_CHARS);
			}
		}else if(type.equals(TYPE_PASSWORD)){
			if(input.matches(REGEX_PASSWORD)){
				return new KingdomValidation(true, "");
			}else{
				return new KingdomValidation(false, INVALID_CHARS);
			}
		}else if(type.equals(TYPE_OBJECT_NAME)){
			if(input.matches(REGEX_OBJECT_NAME)){
				return new KingdomValidation(true, "");
			}else{
				return new KingdomValidation(false, INVALID_CHARS);
			}
		}else if(type.equals(TYPE_LABEL)){
			if(input.matches(REGEX_LABEL)){
				return new KingdomValidation(true, "");
			}else{
				return new KingdomValidation(false, INVALID_CHARS);
			}
		}else if(type.equals(TYPE_DESCRIPTION)){
			if(input.matches(REGEX_DESCRIPTION)){
				return new KingdomValidation(true, "");
			}else{
				return new KingdomValidation(false, INVALID_CHARS);
			}
		}
		
		return new KingdomValidation(false, TYPE_ERROR);
	}
};
