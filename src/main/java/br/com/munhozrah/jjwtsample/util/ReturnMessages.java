package br.com.munhozrah.jjwtsample.util;

public enum ReturnMessages {
	  INVALID_TOKEN(401, "Disconected"),
	  LOGIN_UNAUTHORIZED(401, "Invalid Credentials"),
	  ACCESS_FORBIDDEN(403, "forbidden"),
	  SERVER_ERROR(500, "Server Error");
	  		
	  private final String description;
	  private final int returnCode; 
	  
	  private ReturnMessages(int returnCode, String description) {
		this.returnCode = returnCode;
	    this.description = description;
	  }

	  public int getReturnCode() {
		return returnCode;
	  }

	 @Override
	  public String toString() {
	    return description;
	  }
	  
	  static public String getMsg(ReturnMessages message, Object... params) {
		  return String.format(message.toString(), params);
	  }
	}