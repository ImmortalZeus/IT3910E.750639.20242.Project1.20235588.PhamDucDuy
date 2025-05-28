package models.parsers;

import java.util.concurrent.atomic.*;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;

import models.logData.logData;
import models.mongoDB.mongoDB;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResultAggregator {
    //private final static Queue<logData> dataQueue = new ConcurrentLinkedQueue<>();
    private static List<logData> dataArrayList = null;
    private static mongoDB mongodb = null;
    private static AtomicInteger succeed = null;
    private static AtomicInteger fail = null;

    private void setUp() {
        ResultAggregator.dataArrayList = ResultAggregator.dataArrayList == null ? Collections.synchronizedList(new ArrayList<>()) : ResultAggregator.dataArrayList;
        ResultAggregator.mongodb = ResultAggregator.mongodb == null ? new mongoDB(true) : ResultAggregator.mongodb;
        ResultAggregator.succeed = ResultAggregator.succeed == null ? new AtomicInteger(0) : ResultAggregator.succeed;
        ResultAggregator.fail = ResultAggregator.fail == null ? new AtomicInteger(0) : ResultAggregator.fail;
    }

    public ResultAggregator() {
        this.setUp();
    }

    public ResultAggregator(boolean initNewResultAggregator) {
        if(initNewResultAggregator == true)
        {
            ResultAggregator.dataArrayList = null;
            ResultAggregator.mongodb = null;
            ResultAggregator.succeed = null;
            ResultAggregator.fail = null;
        }
        this.setUp();
    }

    public void addSucceed() {
        ResultAggregator.succeed.incrementAndGet();
    }

    public void addFail() {
        ResultAggregator.fail.incrementAndGet();
    }

    public void collect(logData e) {
        ResultAggregator.dataArrayList.add(e);
    }

    public void saveToMongodb() {
        //ArrayList<logData> dataArrayList = new ArrayList<>(dataQueue);
        synchronized (ResultAggregator.dataArrayList) {
            ResultAggregator.dataArrayList.sort((a, b) -> {return a.getIndex().compareTo(b.getIndex());});
            ResultAggregator.mongodb.insertMany(ResultAggregator.dataArrayList);
            ResultAggregator.dataArrayList.clear();
        } 
        //dataQueue.clear();
    }

    public Integer getSucceed() {
        return ResultAggregator.succeed.get();
    }

    public Integer getFail() {
        return ResultAggregator.fail.get();
    }
}