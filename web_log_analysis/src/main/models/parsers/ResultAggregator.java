package models.parsers;

import java.util.concurrent.atomic.*;

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
    private final static List<logData> dataArrayList = Collections.synchronizedList(new ArrayList<>());
    private final static mongoDB mongodb = new mongoDB();
    private AtomicInteger succeed = new AtomicInteger(0);
    private AtomicInteger fail = new AtomicInteger(0);

    public void addSucceed() {
        succeed.incrementAndGet();
    }

    public void addFail() {
        fail.incrementAndGet();
    }

    public void collect(logData e) {
        dataArrayList.add(e);
    }

    public void saveToMongodb() {
        //ArrayList<logData> dataArrayList = new ArrayList<>(dataQueue);
        synchronized (dataArrayList) {
            dataArrayList.sort((a, b) -> {return a.getIndex().compareTo(b.getIndex());});
            mongodb.insertMany(dataArrayList);
            dataArrayList.clear();
        } 
        //dataQueue.clear();
    }

    public Integer getSucceed() {
        return succeed.get();
    }

    public Integer getFail() {
        return fail.get();
    }
}