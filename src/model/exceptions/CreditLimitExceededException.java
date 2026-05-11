package model.exceptions;

public class CreditLimitExceededException extends Exception {

	private static final long serialVersionUID = 1L;

	public CreditLimitExceededException(String message) {
        super(message);
    }
}