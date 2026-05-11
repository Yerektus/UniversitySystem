package model.exceptions;

public class MaxFailsExceededException extends Exception {

	private static final long serialVersionUID = 1L;

	public MaxFailsExceededException(String message) {
        super(message);
    }
}
