package models.mongoDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.iterators.FilterIterator;
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
import com.mongodb.client.FindIterable;

import models.logData.logData;

import static com.mongodb.client.model.Filters.*;

public class mongoDB {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<logData> collection;
    private static final String now = Long.toString(System.nanoTime());

    public mongoDB() {
        String mongoUri = System.getProperty("mongodb.uri");
        
        String databaseName = System.getProperty("database.name");
        databaseName = databaseName == null ? "project1" : databaseName;
        
        String collectionName = System.getProperty("collection.name");
        collectionName = collectionName == null ? now : collectionName;


        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(mongoUri))
                            .codecRegistry(codecRegistry)
                            .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName, logData.class);
    }

    public void insertOne(logData doc) {
        collection.insertOne(doc);
    }

    public FindIterable<logData> filter(HashMap<String, Object> filter_rules) {
        /*
        filter_rules {
            "byPeriod": Boolean (true | false),
            "byPeriodStartValue": Date,
            "byPeriodEndValue": Date,

            "byRemoteUser": Boolean (true | false),
            "byRemoteUserValue": String,

            "byRemoteIp": Boolean (true | false),
            "byRemoteIpValue": String,

            "byRequest": Boolean (true | false),
            "byRequestValue": String,

            "byResponseStatusCode": Boolean (true | false),
            "byResponseStatusCodeValue": Integer,

            "byBytes": Boolean (true | false),
            "byBytesValue": Integer,

            "byReferrer": Boolean (true | false),
            "byReferrerValue": String,

            "byAgent": Boolean (true | false),
            "byAgentValue": String,

            "byRequestMethod": Boolean (true | false),
            "byRequestMethodValue": String,

            "byByRequestUrl": Boolean (true | false),
            "byByRequestUrlValue": String,

            "byHttpVer": Boolean (true | false),
            "byHttpVerValue": String,

            "byCountryShort": Boolean (true | false),
            "byCountryShortValue": String,

            "byCountryLong": Boolean (true | false),
            "byCountryLongValue": String,

            "byRegion": Boolean (true | false),
            "byRegionValue": String,

            "byCity": Boolean (true | false),
            "byCityValue": String,

            "byLatitude": Boolean (true | false),
            "byLatitudeValue": Float,

            "byLongitude": Boolean (true | false),
            "byLongitudeValue": Float,

            "byZipCode": Boolean (true | false),
            "byZipCodeValue": String,

            "byTimeZone": Boolean (true | false),
            "byTimeZoneValue": String,

        }
        */
        Boolean filter_rules_byPeriod = (Boolean) filter_rules.get("byPeriod");
        Date filter_rules_byPeriodStartValue = (Date) filter_rules.get("byPeriodStartValue");
        Date filter_rules_byPeriodEndValue = (Date) filter_rules.get("byPeriodEndValue");

        Boolean filter_rules_byRemoteIp = (Boolean) filter_rules.get("byRemoteIp");
        String filter_rules_byRemoteIpValue = (String) filter_rules.get("byRemoteIpValue");

        Boolean filter_rules_byRemoteUser = (Boolean) filter_rules.get("byRemoteUser");
        String filter_rules_byRemoteUserValue = (String) filter_rules.get("byRemoteUserValue");

        Boolean filter_rules_byRequest = (Boolean) filter_rules.get("byRequest");
        String filter_rules_byRequestValue = (String) filter_rules.get("byRequestValue");

        Boolean filter_rules_byResponseStatusCode = (Boolean) filter_rules.get("byResponseStatusCode");
        Integer filter_rules_byResponseStatusCodeValue = (Integer) filter_rules.get("byResponseStatusCodeValue");

        Boolean filter_rules_byBytes = (Boolean) filter_rules.get("byBytes");
        Integer filter_rules_byBytesValue = (Integer) filter_rules.get("byBytesValue");

        Boolean filter_rules_byReferrer = (Boolean) filter_rules.get("byReferrer");
        String filter_rules_byReferrerValue = (String) filter_rules.get("byReferrerValue");

        Boolean filter_rules_byAgent = (Boolean) filter_rules.get("byAgent");
        String filter_rules_byAgentValue = (String) filter_rules.get("byAgentValue");

        Boolean filter_rules_byRequestMethod = (Boolean) filter_rules.get("byRequestMethod");
        String filter_rules_byRequestMethodValue = (String) filter_rules.get("byRequestMethodValue");

        Boolean filter_rules_byByRequestUrl = (Boolean) filter_rules.get("byByRequestUrl");
        String filter_rules_byByRequestUrlValue = (String) filter_rules.get("byByRequestUrlValue");

        Boolean filter_rules_byHttpVer = (Boolean) filter_rules.get("byHttpVer");
        String filter_rules_byHttpVerValue = (String) filter_rules.get("byHttpVerValue");

        Boolean filter_rules_byCountryShort = (Boolean) filter_rules.get("byCountryShort");
        String filter_rules_byCountryShortValue = (String) filter_rules.get("byCountryShortValue");

        Boolean filter_rules_byCountryLong = (Boolean) filter_rules.get("byCountryLong");
        String filter_rules_byCountryLongValue = (String) filter_rules.get("byCountryLongValue");

        Boolean filter_rules_byRegion = (Boolean) filter_rules.get("byRegion");
        String filter_rules_byRegionValue = (String) filter_rules.get("byRegionValue");

        Boolean filter_rules_byCity = (Boolean) filter_rules.get("byCity");
        String filter_rules_byCityValue = (String) filter_rules.get("byCityValue");

        Boolean filter_rules_byLatitude = (Boolean) filter_rules.get("byLatitude");
        Float filter_rules_byLatitudeValue = (Float) filter_rules.get("byLatitudeValue");

        Boolean filter_rules_byLongitude = (Boolean) filter_rules.get("byLongitude");
        Float filter_rules_byLongitudeValue = (Float) filter_rules.get("byLongitudeValue");

        Boolean filter_rules_byZipCode = (Boolean) filter_rules.get("byZipCode");
        String filter_rules_byZipCodeValue = (String) filter_rules.get("byZipCodeValue");

        Boolean filter_rules_byTimeZone = (Boolean) filter_rules.get("byTimeZone");
        String filter_rules_byTimeZoneValue = (String) filter_rules.get("byTimeZoneValue");


        ArrayList<Bson> filtersList = new ArrayList<Bson>();
        if(filter_rules_byPeriod != null && filter_rules_byPeriod.equals(true))
        {
            if(filter_rules_byPeriodStartValue != null)
            {
                filtersList.add(gte("time", filter_rules_byPeriodStartValue));
            }
            if(filter_rules_byPeriodEndValue != null)
            {
                filtersList.add(lte("time", filter_rules_byPeriodEndValue));
            }
        }

        if(filter_rules_byRemoteIp != null && filter_rules_byRemoteIp.equals(true))
        {
            if(filter_rules_byRemoteIpValue != null)
            {
                filtersList.add(eq("remoteIp", filter_rules_byRemoteIpValue));
            }
        }

        if(filter_rules_byRemoteUser != null && filter_rules_byRemoteUser.equals(true))
        {
            if(filter_rules_byRemoteUserValue != null)
            {
                filtersList.add(eq("remoteUser", filter_rules_byRemoteUserValue));
            }
        }

        if(filter_rules_byRequest != null && filter_rules_byRequest.equals(true))
        {
            if(filter_rules_byRequestValue != null)
            {
                filtersList.add(eq("request", filter_rules_byRequestValue));
            }
        }

        if(filter_rules_byResponseStatusCode != null && filter_rules_byResponseStatusCode.equals(true))
        {
            if(filter_rules_byResponseStatusCodeValue != null)
            {
                filtersList.add(eq("responseStatusCode", filter_rules_byResponseStatusCodeValue));
            }
        }

        if(filter_rules_byBytes != null && filter_rules_byBytes.equals(true))
        {
            if(filter_rules_byBytesValue != null)
            {
                filtersList.add(eq("bytes", filter_rules_byBytesValue));
            }
        }

        if(filter_rules_byReferrer != null && filter_rules_byReferrer.equals(true))
        {
            if(filter_rules_byReferrerValue != null)
            {
                filtersList.add(eq("referrer", filter_rules_byReferrerValue));
            }
        }

        if(filter_rules_byAgent != null && filter_rules_byAgent.equals(true))
        {
            if(filter_rules_byAgentValue != null)
            {
                filtersList.add(eq("agent", filter_rules_byAgentValue));
            }
        }

        if(filter_rules_byRequestMethod != null && filter_rules_byRequestMethod.equals(true))
        {
            if(filter_rules_byRequestMethodValue != null)
            {
                filtersList.add(eq("requestMethod", filter_rules_byRequestMethodValue));
            }
        }

        if(filter_rules_byByRequestUrl != null && filter_rules_byByRequestUrl.equals(true))
        {
            if(filter_rules_byByRequestUrlValue != null)
            {
                filtersList.add(eq("requestUrl", filter_rules_byByRequestUrlValue));
            }
        }

        if(filter_rules_byHttpVer != null && filter_rules_byHttpVer.equals(true))
        {
            if(filter_rules_byHttpVerValue != null)
            {
                filtersList.add(eq("httpVer", filter_rules_byHttpVerValue));
            }
        }

        if(filter_rules_byCountryShort != null && filter_rules_byCountryShort.equals(true))
        {
            if(filter_rules_byCountryShortValue != null)
            {
                filtersList.add(eq("countryShort", filter_rules_byCountryShortValue));
            }
        }

        if(filter_rules_byCountryLong != null && filter_rules_byCountryLong.equals(true))
        {
            if(filter_rules_byCountryLongValue != null)
            {
                filtersList.add(eq("countryLong", filter_rules_byCountryLongValue));
            }
        }

        if(filter_rules_byRegion != null && filter_rules_byRegion.equals(true))
        {
            if(filter_rules_byRegionValue != null)
            {
                filtersList.add(eq("region", filter_rules_byRegionValue));
            }
        }

        if(filter_rules_byCity != null && filter_rules_byCity.equals(true))
        {
            if(filter_rules_byCityValue != null)
            {
                filtersList.add(eq("city", filter_rules_byCityValue));
            }
        }

        if(filter_rules_byLatitude != null && filter_rules_byLatitude.equals(true))
        {
            if(filter_rules_byLatitudeValue != null)
            {
                filtersList.add(eq("latitude", filter_rules_byLatitudeValue));
            }
        }

        if(filter_rules_byLongitude != null && filter_rules_byLongitude.equals(true))
        {
            if(filter_rules_byLongitudeValue != null)
            {
                filtersList.add(eq("longitude", filter_rules_byLongitudeValue));
            }
        }

        if(filter_rules_byZipCode != null && filter_rules_byZipCode.equals(true))
        {
            if(filter_rules_byZipCodeValue != null)
            {
                filtersList.add(eq("zipCode", filter_rules_byZipCodeValue));
            }
        }

        if(filter_rules_byTimeZone != null && filter_rules_byTimeZone.equals(true))
        {
            if(filter_rules_byTimeZoneValue != null)
            {
                filtersList.add(eq("timeZone", filter_rules_byTimeZoneValue));
            }
        }
        Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);

        FindIterable<logData> res = collection.find(query);
        return res;
    }

    public Long count(HashMap<String, Object> filter_rules) {
        /*
        filter_rules {
            "byPeriod": Boolean (true | false),
            "byPeriodStartValue": Date,
            "byPeriodEndValue": Date,

            "byRemoteUser": Boolean (true | false),
            "byRemoteUserValue": String,

            "byRemoteIp": Boolean (true | false),
            "byRemoteIpValue": String,

            "byRequest": Boolean (true | false),
            "byRequestValue": String,

            "byResponseStatusCode": Boolean (true | false),
            "byResponseStatusCodeValue": Integer,

            "byBytes": Boolean (true | false),
            "byBytesValue": Integer,

            "byReferrer": Boolean (true | false),
            "byReferrerValue": String,

            "byAgent": Boolean (true | false),
            "byAgentValue": String,

            "byRequestMethod": Boolean (true | false),
            "byRequestMethodValue": String,

            "byByRequestUrl": Boolean (true | false),
            "byByRequestUrlValue": String,

            "byHttpVer": Boolean (true | false),
            "byHttpVerValue": String,

            "byCountryShort": Boolean (true | false),
            "byCountryShortValue": String,

            "byCountryLong": Boolean (true | false),
            "byCountryLongValue": String,

            "byRegion": Boolean (true | false),
            "byRegionValue": String,

            "byCity": Boolean (true | false),
            "byCityValue": String,

            "byLatitude": Boolean (true | false),
            "byLatitudeValue": Float,

            "byLongitude": Boolean (true | false),
            "byLongitudeValue": Float,

            "byZipCode": Boolean (true | false),
            "byZipCodeValue": String,

            "byTimeZone": Boolean (true | false),
            "byTimeZoneValue": String,

        }
        */
        Boolean filter_rules_byPeriod = (Boolean) filter_rules.get("byPeriod");
        Date filter_rules_byPeriodStartValue = (Date) filter_rules.get("byPeriodStartValue");
        Date filter_rules_byPeriodEndValue = (Date) filter_rules.get("byPeriodEndValue");

        Boolean filter_rules_byRemoteIp = (Boolean) filter_rules.get("byRemoteIp");
        String filter_rules_byRemoteIpValue = (String) filter_rules.get("byRemoteIpValue");

        Boolean filter_rules_byRemoteUser = (Boolean) filter_rules.get("byRemoteUser");
        String filter_rules_byRemoteUserValue = (String) filter_rules.get("byRemoteUserValue");

        Boolean filter_rules_byRequest = (Boolean) filter_rules.get("byRequest");
        String filter_rules_byRequestValue = (String) filter_rules.get("byRequestValue");

        Boolean filter_rules_byResponseStatusCode = (Boolean) filter_rules.get("byResponseStatusCode");
        Integer filter_rules_byResponseStatusCodeValue = (Integer) filter_rules.get("byResponseStatusCodeValue");

        Boolean filter_rules_byBytes = (Boolean) filter_rules.get("byBytes");
        Integer filter_rules_byBytesValue = (Integer) filter_rules.get("byBytesValue");

        Boolean filter_rules_byReferrer = (Boolean) filter_rules.get("byReferrer");
        String filter_rules_byReferrerValue = (String) filter_rules.get("byReferrerValue");

        Boolean filter_rules_byAgent = (Boolean) filter_rules.get("byAgent");
        String filter_rules_byAgentValue = (String) filter_rules.get("byAgentValue");

        Boolean filter_rules_byRequestMethod = (Boolean) filter_rules.get("byRequestMethod");
        String filter_rules_byRequestMethodValue = (String) filter_rules.get("byRequestMethodValue");

        Boolean filter_rules_byByRequestUrl = (Boolean) filter_rules.get("byByRequestUrl");
        String filter_rules_byByRequestUrlValue = (String) filter_rules.get("byByRequestUrlValue");

        Boolean filter_rules_byHttpVer = (Boolean) filter_rules.get("byHttpVer");
        String filter_rules_byHttpVerValue = (String) filter_rules.get("byHttpVerValue");

        Boolean filter_rules_byCountryShort = (Boolean) filter_rules.get("byCountryShort");
        String filter_rules_byCountryShortValue = (String) filter_rules.get("byCountryShortValue");

        Boolean filter_rules_byCountryLong = (Boolean) filter_rules.get("byCountryLong");
        String filter_rules_byCountryLongValue = (String) filter_rules.get("byCountryLongValue");

        Boolean filter_rules_byRegion = (Boolean) filter_rules.get("byRegion");
        String filter_rules_byRegionValue = (String) filter_rules.get("byRegionValue");

        Boolean filter_rules_byCity = (Boolean) filter_rules.get("byCity");
        String filter_rules_byCityValue = (String) filter_rules.get("byCityValue");

        Boolean filter_rules_byLatitude = (Boolean) filter_rules.get("byLatitude");
        Float filter_rules_byLatitudeValue = (Float) filter_rules.get("byLatitudeValue");

        Boolean filter_rules_byLongitude = (Boolean) filter_rules.get("byLongitude");
        Float filter_rules_byLongitudeValue = (Float) filter_rules.get("byLongitudeValue");

        Boolean filter_rules_byZipCode = (Boolean) filter_rules.get("byZipCode");
        String filter_rules_byZipCodeValue = (String) filter_rules.get("byZipCodeValue");

        Boolean filter_rules_byTimeZone = (Boolean) filter_rules.get("byTimeZone");
        String filter_rules_byTimeZoneValue = (String) filter_rules.get("byTimeZoneValue");


        ArrayList<Bson> filtersList = new ArrayList<Bson>();
        if(filter_rules_byPeriod != null && filter_rules_byPeriod.equals(true))
        {
            if(filter_rules_byPeriodStartValue != null)
            {
                filtersList.add(gte("time", filter_rules_byPeriodStartValue));
            }
            if(filter_rules_byPeriodEndValue != null)
            {
                filtersList.add(lte("time", filter_rules_byPeriodEndValue));
            }
        }

        if(filter_rules_byRemoteIp != null && filter_rules_byRemoteIp.equals(true))
        {
            if(filter_rules_byRemoteIpValue != null)
            {
                filtersList.add(eq("remoteIp", filter_rules_byRemoteIpValue));
            }
        }

        if(filter_rules_byRemoteUser != null && filter_rules_byRemoteUser.equals(true))
        {
            if(filter_rules_byRemoteUserValue != null)
            {
                filtersList.add(eq("remoteUser", filter_rules_byRemoteUserValue));
            }
        }

        if(filter_rules_byRequest != null && filter_rules_byRequest.equals(true))
        {
            if(filter_rules_byRequestValue != null)
            {
                filtersList.add(eq("request", filter_rules_byRequestValue));
            }
        }

        if(filter_rules_byResponseStatusCode != null && filter_rules_byResponseStatusCode.equals(true))
        {
            if(filter_rules_byResponseStatusCodeValue != null)
            {
                filtersList.add(eq("responseStatusCode", filter_rules_byResponseStatusCodeValue));
            }
        }

        if(filter_rules_byBytes != null && filter_rules_byBytes.equals(true))
        {
            if(filter_rules_byBytesValue != null)
            {
                filtersList.add(eq("bytes", filter_rules_byBytesValue));
            }
        }

        if(filter_rules_byReferrer != null && filter_rules_byReferrer.equals(true))
        {
            if(filter_rules_byReferrerValue != null)
            {
                filtersList.add(eq("referrer", filter_rules_byReferrerValue));
            }
        }

        if(filter_rules_byAgent != null && filter_rules_byAgent.equals(true))
        {
            if(filter_rules_byAgentValue != null)
            {
                filtersList.add(eq("agent", filter_rules_byAgentValue));
            }
        }

        if(filter_rules_byRequestMethod != null && filter_rules_byRequestMethod.equals(true))
        {
            if(filter_rules_byRequestMethodValue != null)
            {
                filtersList.add(eq("requestMethod", filter_rules_byRequestMethodValue));
            }
        }

        if(filter_rules_byByRequestUrl != null && filter_rules_byByRequestUrl.equals(true))
        {
            if(filter_rules_byByRequestUrlValue != null)
            {
                filtersList.add(eq("requestUrl", filter_rules_byByRequestUrlValue));
            }
        }

        if(filter_rules_byHttpVer != null && filter_rules_byHttpVer.equals(true))
        {
            if(filter_rules_byHttpVerValue != null)
            {
                filtersList.add(eq("httpVer", filter_rules_byHttpVerValue));
            }
        }

        if(filter_rules_byCountryShort != null && filter_rules_byCountryShort.equals(true))
        {
            if(filter_rules_byCountryShortValue != null)
            {
                filtersList.add(eq("countryShort", filter_rules_byCountryShortValue));
            }
        }

        if(filter_rules_byCountryLong != null && filter_rules_byCountryLong.equals(true))
        {
            if(filter_rules_byCountryLongValue != null)
            {
                filtersList.add(eq("countryLong", filter_rules_byCountryLongValue));
            }
        }

        if(filter_rules_byRegion != null && filter_rules_byRegion.equals(true))
        {
            if(filter_rules_byRegionValue != null)
            {
                filtersList.add(eq("region", filter_rules_byRegionValue));
            }
        }

        if(filter_rules_byCity != null && filter_rules_byCity.equals(true))
        {
            if(filter_rules_byCityValue != null)
            {
                filtersList.add(eq("city", filter_rules_byCityValue));
            }
        }

        if(filter_rules_byLatitude != null && filter_rules_byLatitude.equals(true))
        {
            if(filter_rules_byLatitudeValue != null)
            {
                filtersList.add(eq("latitude", filter_rules_byLatitudeValue));
            }
        }

        if(filter_rules_byLongitude != null && filter_rules_byLongitude.equals(true))
        {
            if(filter_rules_byLongitudeValue != null)
            {
                filtersList.add(eq("longitude", filter_rules_byLongitudeValue));
            }
        }

        if(filter_rules_byZipCode != null && filter_rules_byZipCode.equals(true))
        {
            if(filter_rules_byZipCodeValue != null)
            {
                filtersList.add(eq("zipCode", filter_rules_byZipCodeValue));
            }
        }

        if(filter_rules_byTimeZone != null && filter_rules_byTimeZone.equals(true))
        {
            if(filter_rules_byTimeZoneValue != null)
            {
                filtersList.add(eq("timeZone", filter_rules_byTimeZoneValue));
            }
        }
        Bson query = filtersList.isEmpty() ? new Document() : and(filtersList);

        Long res = collection.countDocuments(query);
        return res;
    }
}
