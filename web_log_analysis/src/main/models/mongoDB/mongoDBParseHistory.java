package models.mongoDB;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class mongoDBParseHistory {
    @BsonProperty("collectionName")
    private String collectionName = "-";
    @BsonProperty("filePath")
    private String filePath = "-";
    @BsonProperty("createdAt")
    private String createdAt = "-";
    
    public mongoDBParseHistory() {}

    public mongoDBParseHistory(String collectionName, String filePath, String createdAt) {
        this.collectionName = collectionName;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public String toString() {
        return "Collection Name" + " : " + this.getCollectionName() + '\n' +
                "File Path" + " : " + this.getFilePath() + '\n' +
                "Created At" + " : " + this.getCreatedAt();
    }
}
