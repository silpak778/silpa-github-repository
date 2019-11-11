import com.wordcount.FileWordCount;

public class FileWordCountTest {

	public static void main(String[] args) throws Exception {
		FileWordCount fwc = new FileWordCount();
		//Reading the file and store each word count in database
		try {
			String source = System.getProperty("source");
			String mongo = System.getProperty("mongo");
			String database = System.getProperty("database");
			String collection = System.getProperty("collection");
			
			if(source != null && !"".equals(source)) {
				if((mongo != null && !"".equals(mongo))
						&& (database != null && !"".equals(database))
						&& (collection != null && !"".equals(collection))) {
					String[] hostAndPort = mongo.split(":");
					fwc.readFileAndInsertToDb(source, hostAndPort[0], Integer.parseInt(hostAndPort[1]), database, collection);
				} else {
					throw new Exception("Please specify the host & port of mongo db server or database and collection to sore the results...");
				}
			} else {
				throw new Exception("Please specify the source file to read...");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
