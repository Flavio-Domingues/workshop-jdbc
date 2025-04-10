package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ValidationException(String msg) {
		super(msg);

	}
	private Map<String, String> error = new HashMap<>();

	public Map<String, String> getError() {
		return error;
	}
	
	public void addError(String filds, String message){
		error.put(filds, message);
	}
}
