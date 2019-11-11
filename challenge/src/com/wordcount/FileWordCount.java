package com.wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dao.DataAccessManager;
import com.exceptions.DAOException;
import com.google.gson.Gson;

public class FileWordCount {
	
	public void readFileAndInsertToDb(String fileName, String host, int port, String myDb, String collectionName) {
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		DataAccessManager dam = null;
		Gson gson = null;
		try {
			String fileType = fileName.substring(fileName.indexOf(".")+1);
			if(!"xml".equalsIgnoreCase(fileType)) {
				// Creating BufferedReader object
				reader = new BufferedReader(new FileReader(fileName));
				// Reading the first line into currentLine
				String currentLine = reader.readLine();
				while (currentLine != null) {
					// splitting the currentLine into words
					String[] words = currentLine.toLowerCase().split("\\s");
					// Iterating each word
					for (String word : words) {
						// if word is already present in wordCountMap, updating its count
						if (wordCountMap.containsKey(word)) {
							wordCountMap.put(word, wordCountMap.get(word) + 1);
						}
						// otherwise inserting the word as key and 1 as its value
						else {
							wordCountMap.put(word, 1);
						}
					}
					// Reading next line into currentLine
					currentLine = reader.readLine();
				}
			} else {
				File xmlFile = new File(fileName);
	            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
	            if (document.hasChildNodes()) {
	            	readChildNodes(document.getChildNodes(), wordCountMap);
	            }
			}
			
			dam = new DataAccessManager();
			dam.init(host, port);
			WordCount wc = null;
			String jsonData = null;
			//Storing the results in MongoDB
			gson = new Gson();
			List<org.bson.Document> documents = new ArrayList<org.bson.Document>();
			for (Map.Entry<String, Integer> en : wordCountMap.entrySet()) { 
				wc = new WordCount();
				//System.out.println(" All Key = " + en.getKey() + ", Value = " + en.getValue());
				wc.setWordName(en.getKey());
				wc.setCount(en.getValue());
				jsonData = gson.toJson(wc);
				//System.out.println(" jsonData = " + jsonData);
				documents.add(org.bson.Document.parse(jsonData));
				//dam.insert(myDb, collection, jsonData);
	        }
			dam.insert(myDb, collectionName, documents);
			
			//Most and least common words
			int maxValueInMap = Collections.max(wordCountMap.values());
			System.out.println(" Max Common Word Count = " + maxValueInMap);
			HashMap<String, Integer> sorted = sortByValue(wordCountMap);
			for (Map.Entry<String, Integer> en : sorted.entrySet()) { 
	            if(maxValueInMap == en.getValue()) {
	            	System.out.println("en.getKey() = " + en.getKey() + ", Value = " + en.getValue());
	            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		} catch(ParserConfigurationException pex) {
			pex.printStackTrace();
		} catch(SAXException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				// Closing the reader
				if(reader != null) {
					reader.close(); 
				}
				// Close Connection
				if(dam != null) {
					dam.destory();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readChildNodes(NodeList nodeList, HashMap<String, Integer> wordCountMap) {
		String xmlText = null;
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.hasChildNodes()) {
                //recursive call if the node has child nodes
            	readChildNodes(elemNode.getChildNodes(), wordCountMap);
            } else {
            	xmlText = elemNode.getTextContent();
            	String[] words = xmlText.toLowerCase().split("\\s");
				// Iterating each word
				for (String word : words) {
					// if word is already present in wordCountMap, updating its count
					if(word != null && !"".equals(word)) {
						if (wordCountMap.containsKey(word)) {
							wordCountMap.put(word, wordCountMap.get(word) + 1);
						}
						// otherwise inserting the word as key and 1 as its value
						else {
							wordCountMap.put(word, 1);
						}
					}
				}
            }
        }
    }
	
	private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> wordCountMap) { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(wordCountMap.entrySet()); 
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() { 
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to HashMap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    }
}
