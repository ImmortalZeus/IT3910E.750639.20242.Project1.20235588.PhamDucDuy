package mongodbTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

public class Test {
    private static final String now = Long.toString(System.currentTimeMillis());
    public static void main(String[] args) throws Exception {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("mongodb_config.properties").getPath();
        System.out.println(rootPath);

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("mongodb_config.properties");
        if (input == null) {
            throw new IOException("Properties file not found in classpath!");
        }
        Properties props = new Properties();
        props.load(input);

        String mongoUri = props.getProperty("mongodb.uri");
        MongoClient mongoClient = MongoClients.create(mongoUri);


        String databaseName = props.getProperty("database.name");
        databaseName = databaseName == null ? "project1" : databaseName;
        MongoDatabase database = mongoClient.getDatabase(databaseName);


        String collectionName = props.getProperty("collection.name");
        collectionName = collectionName == null ? now : collectionName;
        MongoCollection<Document> collection = database.getCollection(collectionName);


        Document doc = new Document("name", "Alice")
                                    .append("age", 30);
        collection.insertOne(doc);
        System.out.println("Inserted a document: " + doc.toJson());

        

        for (Document result : collection.find(new Document("age", new Document("$gt", 25)))) {
            System.out.println(result.toJson());
        }


        collection.updateOne(
            new Document("name", "Alice"),
            new Document("$set", new Document("age", 31))
        );
        System.out.println("Updated Alice's age to 31");


        collection.deleteOne(new Document("name", "Alice"));
        System.out.println("Deleted document where name = Alice");
    }
}
