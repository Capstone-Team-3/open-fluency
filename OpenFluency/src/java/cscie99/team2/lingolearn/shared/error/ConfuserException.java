package cscie99.team2.lingolearn.shared.error;

/**
 * This exception is thrown by the confuser algorithm code and the status
 * message should be checked to determine the exact error.
 */
@SuppressWarnings("serial")
public class ConfuserException extends Exception {
	/**
	 * Constructor.
	 */
	public ConfuserException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 */
	public ConfuserException(String message, Throwable cause) {
		super(message, cause);
	}
}
