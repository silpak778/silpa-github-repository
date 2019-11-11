This readme file contains instructions to execute the program.

1)To run the program and save results we need to create a database myWordDb in mongodb.
2) we need a collection in the mongodb with name wordcount
3)we need to run the jar in the command prompt with following details

java -Dsource=sample.xml -Dmongo=localhost:27017 -Ddatabase=myWordDb -Dcollection=wordcount -jar challenge.jar

4)we need to assign file to the source parameter, database name to database parameter
collection name to collection parameter and at last the jar file.
5)The word counts get inserted to the collection.
6)You can see the wordname and the count in the collection as fields.

Architecture/Technology


Its a plain java project with java files divided into seperate packages which can be run on any machine and the result 
will be stored in the mongodb.
