package com.exceptions;

public class DAOException extends Exception implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public DAOException() {}
	
	public DAOException(String str) {
	    super(str);
	}
	
	public DAOException(Exception ex) {
	    super(ex);
	}
}
