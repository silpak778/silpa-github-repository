package com.dao;

import java.util.List;

import org.bson.Document;

import com.exceptions.DAOException;
import com.mongodb.MongoClient;

public class DataAccessManager {
	MongoClient mongo = null;
	//open connection
	public void init(String host, int port) throws DAOException{
		try {
			mongo = new MongoClient( host , port );
		} catch(Exception ex) {
			throw new DAOException(ex);
		}
	}
	
	//close connection
	public void destory() throws DAOException{
		try {
			if(mongo!=null) {
				mongo.close();
			}
		} catch(Exception ex) {
			throw new DAOException(ex);
		}
	}
	public void createCollection(String myDb, String collName) throws DAOException {
		try {
			if(mongo!=null) {
				mongo.getDatabase(myDb).createCollection(collName);
			}
		} catch(Exception ex) {
			throw new DAOException(ex);
		}
	}
	
	//insert document - row
	public int insert(String myDb, String collName, String jsonData) throws DAOException {
		try {
			if(mongo!=null) {
				mongo.getDatabase(myDb).getCollection(collName).insertOne(Document.parse(jsonData));
			}
		} catch(Exception ex) {
			throw new DAOException(ex);
		}
		return 0;
	}
	
	//insert document - row
	public int insert(String myDb, String collName, List<Document> documents) throws DAOException {
		try {
			if(mongo!=null) {
				mongo.getDatabase(myDb).getCollection(collName).insertMany(documents);
			}
		} catch(Exception ex) {
			throw new DAOException(ex);
		}
		return 0;
	}
	//retrieve document - row
}
