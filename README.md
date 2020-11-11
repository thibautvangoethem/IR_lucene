# IR_lucene
This project is written in java 11 with the help of maven as dependency/build system.

## run the project
Maven setup:  
The pom.xml can be found in the root of the project.  
Import processes will be different depending on your IDE, Here are instructions for [intelij](https://www.jetbrains.com/help/idea/maven-support.html#javaee_maven), [eclipse](https://vaadin.com/learn/tutorials/import-maven-project-eclipse])  
Running this project without a proper IDE will be difficult due to the fact that this project has multiple main classes and can thus not be compilled to a single executable jar file.  
  
Indexing:   
For this you must have the stackoverflow datadump installed and decompressed somewhere on your computer.  
1) Start by getting the path of this dump and putting it in the settings.xml file that can be found in src/main/resources.  
extra) This must not be the full datadump, a subset of the datadump can be made as xml and it's path can also be entered in this settings.xml file.  
2) Navigate to the src/main/java/xml_parser package.  
3) Execute the XmlToLucene.java file as main class and let it run. This process will take up to 4 hours on the full datadump.  
4) This will create a index folder in the root of this project, this index folder will logically be the lucene index.  
  
Querying:  
Make sure the indexing part has been done first.  
1) Navigate to the src/main/java/lucene_assignment package.  
2) Execute the QuerySearcher.java file as main class.  
3) You will be prompted for a query in the console.  
4) This query will be run on the index you previously made. This should not take more than 10 seconds for most queries.    
5) Once it is done The results of your query can be found in the results map, Open the index.html file to see it.  

 
