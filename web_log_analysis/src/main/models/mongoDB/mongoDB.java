package models.mongoDB;

import java.lang.reflect.Array;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import controller.FilterController;
import controller.PrimaryController;

import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;

import models.logData.logData;

import static com.mongodb.client.model.Filters.*;

public class mongoDB {
    private static MongoClient mongoHistoryClient = null;
    private static MongoDatabase databaseHistory = null;
    private static MongoCollection<mongoDBParseHistory> collectionHistory = null;
    
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;
    private static MongoCollection<logData> collection = null;
    private static String now = null;

    private final void setUpHistory() {
        String mongoUri = System.getProperty("mongodb.uri");
        mongoUri = mongoUri == null ? "mongodb://localhost:27017" : mongoUri;

        String databaseHistoryName = System.getProperty("database_history.name");
        databaseHistoryName = databaseHistoryName == null ? "database_history" : databaseHistoryName;
        
        String collectionHistoryName = System.getProperty("collection_history.name");
        collectionHistoryName = collectionHistoryName == null ? "collection_history" : collectionHistoryName;


        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(mongoUri))
                            .codecRegistry(codecRegistry)
                            .build();


        if(mongoDB.mongoHistoryClient == null) {
            mongoDB.mongoHistoryClient = MongoClients.create(settings);
        }
        if(mongoDB.databaseHistory == null) {
            mongoDB.databaseHistory = mongoDB.mongoClient.getDatabase(databaseHistoryName);
        }
        if(mongoDB.collectionHistory == null) {
            mongoDB.collectionHistory = mongoDB.databaseHistory.getCollection(collectionHistoryName, mongoDBParseHistory.class);
        }
    }

    private final void setUp(String inputCollectionName) {
        mongoDB.now = mongoDB.now == null ? Long.toString(System.currentTimeMillis()) : mongoDB.now;

        String mongoUri = System.getProperty("mongodb.uri");
        mongoUri = mongoUri == null ? "mongodb://localhost:27017" : mongoUri;

        String databaseName = System.getProperty("database.name");
        databaseName = databaseName == null ? "project1" : databaseName;
        
        String collectionName = inputCollectionName == null ? System.getProperty("collection.name") : inputCollectionName;
        collectionName = collectionName == null ? mongoDB.now : collectionName;

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(mongoUri))
                            .codecRegistry(codecRegistry)
                            .build();


        if(mongoDB.mongoClient == null) {
            mongoDB.mongoClient = MongoClients.create(settings);
        }
        if(mongoDB.database == null) {
            mongoDB.database = mongoDB.mongoClient.getDatabase(databaseName);
            // mongoDB.database.drop();
        }
        if(mongoDB.collection == null) {
            mongoDB.collection = mongoDB.database.getCollection(collectionName, logData.class);
        }

        // mongoDB.mongoClient = mongoDB.mongoClient == null ? MongoClients.create(settings) : mongoDB.mongoClient;
        // mongoDB.database = mongoDB.database == null ? mongoDB.mongoClient.getDatabase(databaseName) : mongoDB.database;
        // mongoDB.collection = mongoDB.collection == null ? mongoDB.database.getCollection(collectionName, logData.class) : mongoDB.collection;
    }

    public mongoDB() {
        this.setUp(null);
        this.setUpHistory();
    }

    public mongoDB(boolean initNewCollection) {
        if(initNewCollection == true)
        {
            mongoDB.mongoClient = null;
            mongoDB.database = null;
            mongoDB.collection = null;
            mongoDB.now = null;
        }
        this.setUp(null);
        this.setUpHistory();
    }

    public mongoDB(String inputCollectionName) {
        mongoDB.collection = null;
        this.setUp(inputCollectionName);
        this.setUpHistory();
    }


    public final void insertOne(logData doc) {
        if(doc == null) return;
        mongoDB.collection.insertOne(doc);
    }

    public final void insertMany(List<logData> list) {
        if(list == null || list.isEmpty()) return;
        mongoDB.collection.insertMany(list);
    }

    public FindIterable<logData> filter(HashMap<String, Object> filter_rules) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);
    
            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
    
            FindIterable<logData> res = mongoDB.collection.find(query)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
        else
        {
            FindIterable<logData> res = mongoDB.collection.find()/*.sort(Sorts.ascending("index"))*/;
            return res; 
        }
    }

    public FindIterable<logData> filterWithLimit(HashMap<String, Object> filter_rules, Integer limit) {
        if(limit == null || limit < 0)
        {
            limit = 0;
        }

        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);
    
            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
    
            FindIterable<logData> res = mongoDB.collection.find(query).limit(limit)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
        else
        {
            FindIterable<logData> res = mongoDB.collection.find().limit(limit)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
    }

    public FindIterable<logData> filterWithSkip(HashMap<String, Object> filter_rules, Integer skip) {
        if(skip == null || skip < 0)
        {
            skip = 0;
        }

        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);

            FindIterable<logData> res = mongoDB.collection.find(query).skip(skip)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
        else
        {
            FindIterable<logData> res = mongoDB.collection.find().skip(skip)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
    }

    public FindIterable<logData> filterWithSkipAndLimit(HashMap<String, Object> filter_rules, Integer skip, Integer limit) {
        if(limit == null || limit < 0)
        {
            limit = 0;
        }
        if(skip == null || skip < 0)
        {
            skip = 0;
        }

        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);

            FindIterable<logData> res = mongoDB.collection.find(query).skip(skip).limit(limit)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
        else
        {
            FindIterable<logData> res = mongoDB.collection.find().skip(skip).limit(limit)/*.sort(Sorts.ascending("index"))*/;
            return res;
        }
    }
    
    public Integer count(HashMap<String, Object> filter_rules) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);

            Integer res = Long.valueOf(mongoDB.collection.countDocuments(query)).intValue();
            return res;
        }
        else
        {
            Integer res = Long.valueOf(mongoDB.collection.countDocuments()).intValue();
            return res;
        }
    }

    public ArrayList<Document> aggregate(HashMap<String, Object> filter_rules, String field) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
            
            List<Bson> pipeline = Arrays.asList(
                new Document("$match", query),
                new Document("$group", new Document("_id", "$" + field)
                    .append("count", new Document("$sum", 1)))
            );

            ArrayList<Document> res = mongoDB.collection.aggregate(pipeline, Document.class).into(new ArrayList<>());
            return res;
        }
        else
        {
            List<Bson> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", "$" + field)
                    .append("count", new Document("$sum", 1)))
            );

            ArrayList<Document> res = mongoDB.collection.aggregate(pipeline, Document.class).into(new ArrayList<>());
            return res;
        }
    }

    public Document getMax(HashMap<String, Object> filter_rules, String field) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
            
            List<Bson> pipeline = Arrays.asList(
                new Document("$match", query),
                new Document("$group", new Document("_id", null)
                    .append("max", new Document("$max", "$" + field)))
            );

            Document res = mongoDB.collection.aggregate(pipeline, Document.class).first();
            return res;
        }
        else
        {
            List<Bson> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", null)
                    .append("max", new Document("$max", "$" + field)))
            );

            Document res = mongoDB.collection.aggregate(pipeline, Document.class).first();
            return res;
        }
    }

    public Document getMin(HashMap<String, Object> filter_rules, String field) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
            
            List<Bson> pipeline = Arrays.asList(
                new Document("$match", query),
                new Document("$group", new Document("_id", null)
                    .append("min", new Document("$min", "$" + field)))
            );

            Document res = mongoDB.collection.aggregate(pipeline, Document.class).first();
            return res;
        }
        else
        {
            List<Bson> pipeline = Arrays.asList(
                new Document("$group", new Document("_id", null)
                    .append("min", new Document("$min", "$" + field)))
            );

            Document res = mongoDB.collection.aggregate(pipeline, Document.class).first();
            return res;
        }
    }

    public ArrayList<Document> bucket(HashMap<String, Object> filter_rules, String field, List<?> boundaries) {
        if(filter_rules != null)
        {
            ArrayList<Bson> filtersList = createFiltersList(filter_rules);

            Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);
            
            List<Bson> pipeline = Arrays.asList(
                new Document("$match", query),
                new Document("$bucket", new Document("groupBy", "$" + field)
                    .append("boundaries", boundaries)
                    .append("default", "Outside Range")
                    .append("output", new Document("count", new Document("$sum", 1)))),
                new Document("$sort", new Document("_id", 1))
            );

            ArrayList<Document> res = mongoDB.collection.aggregate(pipeline, Document.class).into(new ArrayList<>());
            return res;
        }
        else
        {
            List<Bson> pipeline = Arrays.asList(
                new Document("$bucket", new Document("groupBy", "$" + field)
                    .append("boundaries", boundaries)
                    .append("default", "Outside Range")
                    .append("output", new Document("count", new Document("$sum", 1)))),
                new Document("$sort", new Document("_id", 1))
            );

            ArrayList<Document> res = mongoDB.collection.aggregate(pipeline, Document.class).into(new ArrayList<>());
            return res;
        }
    }

    public ArrayList<Bson> createFiltersList(HashMap<String, Object> filter_rules) {
        /*
        filter_rules {
            "byIndex": Boolean (true | false),
            "byIndexValue": [{
                byIndexStartValue: Integer,
                byIndexEndValue: Integer
            }, {
                byIndexStartValue: Integer,
                byIndexEndValue: Integer
            }, 
            ....]

            "byPeriod": Boolean (true | false),
            "byPeriodValue": [{
                byPeriodStartValue: Date,
                byPeriodEndValue: Date
            }, {
                byPeriodStartValue: Date,
                byPeriodEndValue: Date
            }, 
            ....]

            "byRemoteUser": Boolean (true | false),
            "byRemoteUserValue": [value0: String, value1: String, ...],

            "byRemoteIp": Boolean (true | false),
            "byRemoteIpValue": [value0: String, value1: String, ...],

            "byRequest": Boolean (true | false),
            "byRequestValue": [value0: String, value1: String, ...],

            "byResponseStatusCode": Boolean (true | false),
            "byResponseStatusCodeValue": [value0: Integer, value1: Integer, ...],

            "byBytes": Boolean (true | false),
            "byBytesValue": [{
                byBytesStartValue: Date,
                byBytesEndValue: Date
            }, {
                byBytesStartValue: Date,
                byBytesEndValue: Date
            }, 
            ....]

            "byReferrer": Boolean (true | false),
            "byReferrerValue": [value0: String, value1: String, ...],

            "byAgent": Boolean (true | false),
            "byAgentValue": [value0: String, value1: String, ...],

            "byRequestMethod": Boolean (true | false),
            "byRequestMethodValue": [value0: String, value1: String, ...],

            "byRequestUrl": Boolean (true | false),
            "byRequestUrlValue": [value0: String, value1: String, ...],

            "byHttpVer": Boolean (true | false),
            "byHttpVerValue": [value0: String, value1: String, ...],

            "byCountryShort": Boolean (true | false),
            "byCountryShortValue": [value0: String, value1: String, ...],

            "byCountryLong": Boolean (true | false),
            "byCountryLongValue": [value0: String, value1: String, ...],

            "byRegion": Boolean (true | false),
            "byRegionValue": [value0: String, value1: String, ...],

            "byCity": Boolean (true | false),
            "byCityValue": [value0: String, value1: String, ...],

            "byBrowser": Boolean (true | false),
            "byBrowserValue": [value0: String, value1: String, ...],

            "byOS": Boolean (true | false),
            "byOSValue": [value0: String, value1: String, ...],

            "byDevice": Boolean (true | false),
            "byDeviceValue": [value0: String, value1: String, ...],
        }
        */
        if(filter_rules != null)
            {
            Boolean filter_rules_byIndex = (Boolean) filter_rules.get("byIndex");
            Map<String, Integer>[] filter_rules_byIndexValue = this.createMapArrayFromObject(Map.class, filter_rules.get("byIndexValue"));

            Boolean filter_rules_byPeriod = (Boolean) filter_rules.get("byPeriod");
            Map<String, Date>[] filter_rules_byPeriodValue = this.createMapArrayFromObject(Map.class, filter_rules.get("byPeriodValue"));
            
            Boolean filter_rules_byRemoteIp = (Boolean) filter_rules.get("byRemoteIp");
            String[] filter_rules_byRemoteIpValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRemoteIpValue"));

            Boolean filter_rules_byRemoteUser = (Boolean) filter_rules.get("byRemoteUser");
            String[] filter_rules_byRemoteUserValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRemoteUserValue"));

            Boolean filter_rules_byRequest = (Boolean) filter_rules.get("byRequest");
            String[] filter_rules_byRequestValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRequestValue"));

            Boolean filter_rules_byResponseStatusCode = (Boolean) filter_rules.get("byResponseStatusCode");
            Integer[] filter_rules_byResponseStatusCodeValue = this.createIntegerArrayFromObject(Integer.class, filter_rules.get("byResponseStatusCodeValue"));

            Boolean filter_rules_byBytes = (Boolean) filter_rules.get("byBytes");
            Map<String, Integer>[] filter_rules_byBytesValue = this.createMapArrayFromObject(Map.class, filter_rules.get("byBytesValue"));

            Boolean filter_rules_byReferrer = (Boolean) filter_rules.get("byReferrer");
            String[] filter_rules_byReferrerValue = this.createStringArrayFromObject(String.class, filter_rules.get("byReferrerValue"));

            Boolean filter_rules_byAgent = (Boolean) filter_rules.get("byAgent");
            String[] filter_rules_byAgentValue = this.createStringArrayFromObject(String.class, filter_rules.get("byAgentValue"));

            Boolean filter_rules_byRequestMethod = (Boolean) filter_rules.get("byRequestMethod");
            String[] filter_rules_byRequestMethodValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRequestMethodValue"));

            Boolean filter_rules_byRequestUrl = (Boolean) filter_rules.get("byRequestUrl");
            String[] filter_rules_byRequestUrlValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRequestUrlValue"));

            Boolean filter_rules_byHttpVer = (Boolean) filter_rules.get("byHttpVer");
            String[] filter_rules_byHttpVerValue = this.createStringArrayFromObject(String.class, filter_rules.get("byHttpVerValue"));

            Boolean filter_rules_byCountryShort = (Boolean) filter_rules.get("byCountryShort");
            String[] filter_rules_byCountryShortValue = this.createStringArrayFromObject(String.class, filter_rules.get("byCountryShortValue"));

            Boolean filter_rules_byCountryLong = (Boolean) filter_rules.get("byCountryLong");
            String[] filter_rules_byCountryLongValue = this.createStringArrayFromObject(String.class, filter_rules.get("byCountryLongValue"));

            Boolean filter_rules_byRegion = (Boolean) filter_rules.get("byRegion");
            String[] filter_rules_byRegionValue = this.createStringArrayFromObject(String.class, filter_rules.get("byRegionValue"));

            Boolean filter_rules_byCity = (Boolean) filter_rules.get("byCity");
            String[] filter_rules_byCityValue =  this.createStringArrayFromObject(String.class, filter_rules.get("byCityValue"));

            Boolean filter_rules_byBrowser = (Boolean) filter_rules.get("byBrowser");
            String[] filter_rules_byBrowserValue =  this.createStringArrayFromObject(String.class, filter_rules.get("byBrowserValue"));

            Boolean filter_rules_byOS = (Boolean) filter_rules.get("byOS");
            String[] filter_rules_byOSValue =  this.createStringArrayFromObject(String.class, filter_rules.get("byOSValue"));

            Boolean filter_rules_byDevice = (Boolean) filter_rules.get("byDevice");
            String[] filter_rules_byDeviceValue =  this.createStringArrayFromObject(String.class, filter_rules.get("byDeviceValue"));

            ArrayList<Bson> filtersList = new ArrayList<Bson>();


            ArrayList<Bson> filtersList_byIndex = new ArrayList<Bson>();
            if(filter_rules_byIndex != null && filter_rules_byIndex.equals(true))
            {
                ArrayList<Bson> tmpfilterslist = new ArrayList<Bson>();
                for(int i = 0; i < filter_rules_byIndexValue.length; i++)
                {
                    if(filter_rules_byIndexValue[i].get("byIndexStartValue") != null)
                    {
                        tmpfilterslist.add(gte("index", filter_rules_byIndexValue[i].get("byIndexStartValue")));
                    }
                    if(filter_rules_byIndexValue[i].get("byIndexEndValue") != null)
                    {
                        tmpfilterslist.add(lte("index", filter_rules_byIndexValue[i].get("byIndexEndValue")));
                    }
                    filtersList_byIndex.add(and(tmpfilterslist));
                }
            }
            if(!filtersList_byIndex.isEmpty())
            {
                filtersList.add(or(filtersList_byIndex));
            }

            ArrayList<Bson> filtersList_byPeriod = new ArrayList<Bson>();
            if(filter_rules_byPeriod != null && filter_rules_byPeriod.equals(true))
            {
                ArrayList<Bson> tmpfilterslist = new ArrayList<Bson>();
                for(int i = 0; i < filter_rules_byPeriodValue.length; i++)
                {
                    if(filter_rules_byPeriodValue[i].get("byPeriodStartValue") != null)
                    {
                        //tmpfilterslist.add(gte("time", filter_rules_byPeriodValue[i]));
                        tmpfilterslist.add(new Document("$expr", new Document("$gte", List.of(
                            "$time",
                            filter_rules_byPeriodValue[i].get("byPeriodStartValue")
                        ))));
                    }
                    if(filter_rules_byPeriodValue[i].get("byPeriodEndValue") != null)
                    {
                        //tmpfilterslist.add(lte("time", filter_rules_byPeriodValue[i]));
                        tmpfilterslist.add(new Document("$expr", new Document("$lte", List.of(
                            "$time",
                            filter_rules_byPeriodValue[i].get("byPeriodEndValue")
                        ))));
                    }
                    filtersList_byPeriod.add(and(tmpfilterslist));
                }
            }
            if(!filtersList_byPeriod.isEmpty())
            {
                filtersList.add(or(filtersList_byPeriod));
            }

            ArrayList<Bson> filtersList_byRemoteIp = new ArrayList<Bson>();
            if(filter_rules_byRemoteIp != null && filter_rules_byRemoteIp.equals(true))
            {
                for(String remoteIpValue : filter_rules_byRemoteIpValue)
                {
                    if(remoteIpValue != null)
                    {
                        filtersList_byRemoteIp.add(eq("remoteIp", remoteIpValue));
                    }
                }
            }
            if(!filtersList_byRemoteIp.isEmpty())
            {
                filtersList.add(or(filtersList_byRemoteIp));
            }

            ArrayList<Bson> filtersList_byRemoteUser = new ArrayList<Bson>();
            if(filter_rules_byRemoteUser != null && filter_rules_byRemoteUser.equals(true))
            {
                for(String remoteUserValue : filter_rules_byRemoteUserValue)
                {
                    if(remoteUserValue != null)
                    {
                        filtersList_byRemoteUser.add(eq("remoteUser", remoteUserValue));
                    }
                }
            }
            if(!filtersList_byRemoteUser.isEmpty())
            {
                filtersList.add(or(filtersList_byRemoteUser));
            }

            ArrayList<Bson> filtersList_byRequest = new ArrayList<Bson>();
            if(filter_rules_byRequest != null && filter_rules_byRequest.equals(true))
            {
                for(String requestValue : filter_rules_byRequestValue)
                {
                    if(requestValue != null)
                    {
                        filtersList_byRequest.add(eq("request", requestValue));
                    }
                }
            }
            if(!filtersList_byRequest.isEmpty())
            {
                filtersList.add(or(filtersList_byRequest));
            }

            ArrayList<Bson> filtersList_byResponseStatusCodeValue = new ArrayList<Bson>();
            if(filter_rules_byResponseStatusCode != null && filter_rules_byResponseStatusCode.equals(true))
            {
                for(Integer responseStatusCodeValue : filter_rules_byResponseStatusCodeValue)
                {
                    if(responseStatusCodeValue != null)
                    {
                        filtersList_byResponseStatusCodeValue.add(eq("responseStatusCode", responseStatusCodeValue));
                    }
                }
            }
            if(!filtersList_byResponseStatusCodeValue.isEmpty())
            {
                filtersList.add(or(filtersList_byResponseStatusCodeValue));
            }

            ArrayList<Bson> filtersList_byBytes = new ArrayList<Bson>();
            if(filter_rules_byBytes != null && filter_rules_byBytes.equals(true))
            {
                ArrayList<Bson> tmpfilterslist = new ArrayList<Bson>();
                for(int i = 0; i < filter_rules_byBytesValue.length; i++)
                {
                    if(filter_rules_byBytesValue[i].get("byBytesStartValue") != null)
                    {
                        tmpfilterslist.add(gte("index", filter_rules_byBytesValue[i].get("byBytesStartValue")));
                    }
                    if(filter_rules_byBytesValue[i].get("byBytesEndValue") != null)
                    {
                        tmpfilterslist.add(lte("index", filter_rules_byBytesValue[i].get("byBytesEndValue")));
                    }
                    filtersList_byBytes.add(and(tmpfilterslist));
                }
            }
            if(!filtersList_byBytes.isEmpty())
            {
                filtersList.add(or(filtersList_byBytes));
            }

            ArrayList<Bson> filtersList_byReferrer = new ArrayList<Bson>();
            if(filter_rules_byReferrer != null && filter_rules_byReferrer.equals(true))
            {
                for(String referrerValue : filter_rules_byReferrerValue)
                {
                    if(referrerValue != null)
                    {
                        filtersList_byReferrer.add(eq("referrer", referrerValue));
                    }
                }
            }
            if(!filtersList_byReferrer.isEmpty())
            {
                filtersList.add(or(filtersList_byReferrer));
            }

            ArrayList<Bson> filtersList_byAgent = new ArrayList<Bson>();
            if(filter_rules_byAgent != null && filter_rules_byAgent.equals(true))
            {
                for(String agentValue : filter_rules_byAgentValue)
                {
                    if(agentValue != null)
                    {
                        filtersList_byAgent.add(eq("agent", agentValue));
                    }
                }
            }
            if(!filtersList_byAgent.isEmpty())
            {
                filtersList.add(or(filtersList_byAgent));
            }

            ArrayList<Bson> filtersList_byRequestMethod = new ArrayList<Bson>();
            if(filter_rules_byRequestMethod != null && filter_rules_byRequestMethod.equals(true))
            {
                for(String requestMethodValue : filter_rules_byRequestMethodValue)
                {
                    if(requestMethodValue != null)
                    {
                        filtersList_byRequestMethod.add(eq("requestMethod", requestMethodValue));
                    }
                }
            }
            if(!filtersList_byRequestMethod.isEmpty())
            {
                filtersList.add(or(filtersList_byRequestMethod));
            }

            ArrayList<Bson> filtersList_byRequestUrl = new ArrayList<Bson>();
            if(filter_rules_byRequestUrl != null && filter_rules_byRequestUrl.equals(true))
            {
                for(String requestUrlValue : filter_rules_byRequestUrlValue)
                {
                    if(requestUrlValue != null)
                    {
                        filtersList_byRequestUrl.add(eq("requestUrl", requestUrlValue));
                    }
                }
            }
            if(!filtersList_byRequestUrl.isEmpty())
            {
                filtersList.add(or(filtersList_byRequestUrl));
            }

            ArrayList<Bson> filtersList_byHttpVer = new ArrayList<Bson>();
            if(filter_rules_byHttpVer != null && filter_rules_byHttpVer.equals(true))
            {
                for(String httpVerValue : filter_rules_byHttpVerValue)
                {
                    if(httpVerValue != null)
                    {
                        filtersList_byHttpVer.add(eq("httpVer", httpVerValue));
                    }
                }
            }
            if(!filtersList_byHttpVer.isEmpty())
            {
                filtersList.add(or(filtersList_byHttpVer));
            }

            ArrayList<Bson> filtersList_byCountryShort = new ArrayList<Bson>();
            if(filter_rules_byCountryShort != null && filter_rules_byCountryShort.equals(true))
            {
                for(String countryShortValue : filter_rules_byCountryShortValue)
                {
                    if(countryShortValue != null)
                    {
                        filtersList_byCountryShort.add(eq("countryShort", countryShortValue));
                    }
                }
            }
            if(!filtersList_byCountryShort.isEmpty())
            {
                filtersList.add(or(filtersList_byCountryShort));
            }

            ArrayList<Bson> filtersList_byCountryLong = new ArrayList<Bson>();
            if(filter_rules_byCountryLong != null && filter_rules_byCountryLong.equals(true))
            {
                for(String countryLongValue : filter_rules_byCountryLongValue)
                {
                    if(countryLongValue != null)
                    {
                        filtersList_byCountryLong.add(eq("countryLong", countryLongValue));
                    }
                }
            }
            if(!filtersList_byCountryLong.isEmpty())
            {
                filtersList.add(or(filtersList_byCountryLong));
            }

            ArrayList<Bson> filtersList_byRegion = new ArrayList<Bson>();
            if(filter_rules_byRegion != null && filter_rules_byRegion.equals(true))
            {
                for(String regionValue : filter_rules_byRegionValue)
                {
                    if(regionValue != null)
                    {
                        filtersList_byRegion.add(eq("region", regionValue));
                    }
                }
            }
            if(!filtersList_byRegion.isEmpty())
            {
                filtersList.add(or(filtersList_byRegion));
            }

            ArrayList<Bson> filtersList_byCity = new ArrayList<Bson>();
            if(filter_rules_byCity != null && filter_rules_byCity.equals(true))
            {
                for(String cityValue : filter_rules_byCityValue)
                {
                    if(cityValue != null)
                    {
                        filtersList_byCity.add(eq("city", cityValue));
                    }
                }
            }
            if(!filtersList_byCity.isEmpty())
            {
                filtersList.add(or(filtersList_byCity));
            }

            ArrayList<Bson> filtersList_byBrowser = new ArrayList<Bson>();
            if(filter_rules_byBrowser != null && filter_rules_byBrowser.equals(true))
            {
                for(String browserValue : filter_rules_byBrowserValue)
                {
                    if(browserValue != null)
                    {
                        filtersList_byBrowser.add(eq("browser", browserValue));
                    }
                }
            }
            if(!filtersList_byBrowser.isEmpty())
            {
                filtersList.add(or(filtersList_byBrowser));
            }

            ArrayList<Bson> filtersList_byOS = new ArrayList<Bson>();
            if(filter_rules_byOS != null && filter_rules_byOS.equals(true))
            {
                for(String osValue : filter_rules_byOSValue)
                {
                    if(osValue != null)
                    {
                        filtersList_byOS.add(eq("os", osValue));
                    }
                }
            }
            if(!filtersList_byOS.isEmpty())
            {
                filtersList.add(or(filtersList_byOS));
            }

            ArrayList<Bson> filtersList_byDevice = new ArrayList<Bson>();
            if(filter_rules_byDevice != null && filter_rules_byDevice.equals(true))
            {
                for(String deviceValue : filter_rules_byDeviceValue)
                {
                    if(deviceValue != null)
                    {
                        filtersList_byDevice.add(eq("device", deviceValue));
                    }
                }
            }
            if(!filtersList_byDevice.isEmpty())
            {
                filtersList.add(or(filtersList_byDevice));
            }

            return filtersList;
        }
        else
        {
            ArrayList<Bson> res = new ArrayList<Bson>();
            res.add((new Document()));
            return res;
        }
    }

    public FindIterable<mongoDBParseHistory> getHistory() {
        FindIterable<mongoDBParseHistory> res = mongoDB.collectionHistory.find()/*.sort(Sorts.ascending("index"))*/;
        return res;
    }

    public final void addHistory(String collectionName, String filepath, String createdAt) {
        mongoDB.collectionHistory.insertOne((new mongoDBParseHistory(collectionName, filepath, createdAt)));
    }

    public final void saveToMongodb(List<logData> dataArrayList, String filepath) {
        this.insertMany(dataArrayList);
        this.addHistory(mongoDB.collection.getNamespace().getCollectionName(), filepath, mongoDB.now);
    }

    public final void deleteCollection(String collectionName) {
        MongoCollection<logData> collection_tmp = database.getCollection(collectionName, logData.class);
        collection_tmp.drop();
        mongoDB.collectionHistory.deleteMany(Filters.eq("collectionName", collectionName));
        if(collectionName.equals(mongoDB.collection.getNamespace().getCollectionName()))
        {
            PrimaryController.resetData();
            FilterController.resetData();
            mongoDB.mongoClient = null;
            mongoDB.database = null;
            mongoDB.collection = null;
            mongoDB.now = null;
            this.setUp(null);
            this.setUpHistory();
        }
    }

    public final void deleteCurrentCollection() {
        mongoDB.collection.drop();
        mongoDB.collectionHistory.deleteMany(Filters.eq("collectionName", mongoDB.collection.getNamespace().getCollectionName()));
        PrimaryController.resetData();
        FilterController.resetData();
        mongoDB.mongoClient = null;
        mongoDB.database = null;
        mongoDB.collection = null;
        mongoDB.now = null;
        this.setUp(null);
        this.setUpHistory();
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        // example : MyObject obj = createInstance(MyObject.class);
        return clazz.getDeclaredConstructor().newInstance(); // Assumes a no-arg constructor
    }

    private <T> T createInstance(Class<T> clazz, Class<?>[] paramTypes, Object[] args) throws Exception {
        // example : MyObject obj = createInstance(MyObject.class, new Class[]{String.class, int.class}, new Object[]{"Alice", 42});
        return clazz.getDeclaredConstructor(paramTypes).newInstance(args);
    }

    private <T> T[] createArray(Class<T> clazz, int length) {
        // example : MyObject[] array = createArray(MyObject.class, 3);
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, length);
        return array;
    }

    private <T> T[] createStringArrayFromObject(Class<? extends String> clazz, Object obj) {
        if (obj instanceof List<?>) {
            obj = ((List<?>) obj).toArray(createArray(Object.class, 1));
        }
        if (obj instanceof Object[]) {
            Object[] source = (Object[]) obj;
            @SuppressWarnings("unchecked")
            T[] array = (T[]) Array.newInstance(clazz, source.length);
            for(int i = 0; i < source.length; i++)
            {
                if(clazz.isInstance(source[i]))
                {
                    array[i] = (T) source[i];
                }
            }
            return array;
        } else {
            return null;
        }
    }

    private <T> T[] createIntegerArrayFromObject(Class<? extends Integer> clazz, Object obj) {
        if (obj instanceof List<?>) {
            obj = ((List<?>) obj).toArray(createArray(Object.class, 1));
        }
        if (obj instanceof Object[]) {
            Object[] source = (Object[]) obj;
            @SuppressWarnings("unchecked")
            T[] array = (T[]) Array.newInstance(clazz, source.length);
            for(int i = 0; i < source.length; i++)
            {
                if(clazz.isInstance(source[i]))
                {
                    array[i] = (T) source[i];
                }
            }
            return array;
        } else {
            return null;
        }
    }

    private <K, V> Map<K, V>[] createMapArrayFromObject(Class<?> clazz, Object obj) {
        if (obj instanceof List<?>) {
            obj = ((List<?>) obj).toArray(createArray(Object.class, 1));
        }
        if (obj instanceof Object[]) {
            Object[] source = (Object[]) obj;
            @SuppressWarnings("unchecked")
            Map<K, V>[] array = (Map<K, V>[]) Array.newInstance(clazz, source.length);
            for(int i = 0; i < source.length; i++)
            {
                if(clazz.isInstance(source[i]))
                {
                    array[i] = (Map<K, V>) source[i];
                }
            }
            return array;
        } else {
            return null;
        }
    }
}
