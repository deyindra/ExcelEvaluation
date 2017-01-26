package org.idey.excel.expression.tokenizer;

/**
 * @author i.dey
 * This exception is being thrown whenever {@link ExpressionTokenizer} finds unknown function or variable.
 * @see IllegalArgumentException
 */
public class IllegalFunctionOrVariableException extends IllegalArgumentException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	protected static final String ERROR_MESSAGE_FORMAT = "Unknown function or variable '%s' at pos %d in expression '%s'";
	private final String message;
	private final String expression;
	private final String token;
	private final int position;

	public IllegalFunctionOrVariableException(String expression, int position, int length) {
		this.expression = expression;
		this.token = token(expression, position, length);
		this.position = position;
		this.message = String.format(ERROR_MESSAGE_FORMAT,token,position,expression);
	}

	private String token(String expression, int position, int length) {
		int len = expression.length();
		int end = position + length;
		if (len < end) {
			end = len;
		}
		return expression.substring(position, end);
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @return Expression which contains unknown function or variable
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @return The name of unknown function or variable
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return The position of unknown function or variable
	 */
	public int getPosition() {
		return position;
	}
}
