package model.exceptions;

public class NotAResearcherException extends Exception {
	
	private static final long serialVersionUID = 1L;

    public NotAResearcherException(String message) {
        super(message);
    }
}