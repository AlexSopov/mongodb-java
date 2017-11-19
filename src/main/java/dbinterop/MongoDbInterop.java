package dbinterop;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import model.LogModel;
import org.bson.Document;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class MongoDbInterop implements Closeable{
    private static final String T_IP = "this." + LogModel.IP;
    private static final String T_URL = "this." + LogModel.URL;
    private static final String T_TIME_STAMP = "this." + LogModel.TIME_STAMP;
    private static final String T_TIME_SPENT = "this." + LogModel.TIME_SPENT;

    private MongoCollection<Document> collection;
    private MongoDatabase dataBase;
    private MongoClient mongoClient;

    public  MongoDbInterop() {
        mongoClient = new MongoClient("localhost", 27017);
        dataBase = mongoClient.getDatabase("logsDb");
        collection = dataBase.getCollection("logs");
    }

    public boolean insert(Document document) {
        try {
            collection.insertOne(document);
            return true;
        }
        catch (MongoException mongoException) {
            System.out.println("Can not insert object");
            return false;
        }
    }
    public boolean insertMany(List<Document> documents) {
        try {
            collection.insertMany(documents);
            return true;
        }
        catch (MongoException mongoException) {
            System.out.println("Can not insert objects");
            return false;
        }
    }

    public Date getDateTime(String dateTime) {
        String date = dateTime.substring(0, dateTime.indexOf("T"));
        String time = dateTime.substring(dateTime.indexOf("T") + 1);

        List<String> dateValues = getValues(date, '-');
        int year = Integer.parseInt(dateValues.get(0));
        int month = Integer.parseInt(dateValues.get(1));
        int day = Integer.parseInt(dateValues.get(2));

        List<String> timeValues = getValues(time, ':');
        int hours = Integer.parseInt(timeValues.get(0));
        int minutes = Integer.parseInt(timeValues.get(1));
        int seconds = timeValues.get(2).contains(".") ?
                Integer.parseInt(timeValues.get(2).
                        substring(0, timeValues.get(2).indexOf("."))): Integer.parseInt(timeValues.get(2));

        return new Date(year - 1900, month - 1, day, hours, minutes, seconds);
    }
    private List<String> getValues(String csvLog, char separator) {
        String partLog = csvLog;
        List<String> values = new ArrayList<>();
        while (partLog.indexOf(separator) != -1) {
            values.add((partLog.substring(0, partLog.indexOf(separator))).trim());
            partLog = partLog.substring(partLog.indexOf(separator) + 1);
        }
        values.add(partLog.trim());
        return values;
    }

    public MongoIterable<Document> getAllLogs() {
        return collection.find();
    }
    public MongoIterable<Document> getVisitorsIpsOfUrl(String url) {
        return collection
                .find(eq(LogModel.URL, url))
                .sort(ascending(LogModel.IP))
                .projection(fields(include(LogModel.IP), excludeId()));
    }
    public MongoIterable<Document> getVisitedUrlsInPeriod(Date fromDate, Date toDate) {
        return collection.
                find(and(gte(LogModel.TIME_STAMP, fromDate), lte(LogModel.TIME_STAMP, toDate))).
                projection(fields(include(LogModel.URL), excludeId())).sort(ascending(LogModel.URL));

    }
    public MongoIterable<Document> getVisitedUrlsByIp(String ip) {
        return collection.find(eq(LogModel.IP, ip))
                .sort(ascending(LogModel.URL))
                .projection(fields(include(LogModel.URL), excludeId()));
    }

    public MongoIterable<Document> getTotalVisitCountOfUrls() {
        String destinationName = "totalVisitCountOfUrls";
        String mapFunction = String.format("function () { emit(%s, 1); }", T_URL);
        String reduceFunction = "function(key, values) { return Array.sum(values) / 1000; }";

        collection.mapReduce(mapFunction, reduceFunction).collectionName(destinationName).toCollection();
        return dataBase.getCollection(destinationName).find().sort(descending("value"));
    }
    public MongoIterable<Document> getTotalVisitTimeOfUrls() {
        String destinationName = "totalVisitTimeOfUrls";
        String mapFunction = String.format("function () { emit(%s, %s); }", T_URL, T_TIME_SPENT);
        String reduceFunction = "function(key, values) { return Array.sum(values); }";

        collection.mapReduce(mapFunction, reduceFunction).collectionName(destinationName).toCollection();
        return dataBase.getCollection(destinationName).find().sort(descending("value"));
    }
    public MongoIterable<Document> getVisitsCountOfUrlsInPeriod(Date fromDate, Date toDate) {
        String destinationName = "visitsCountOfUrlsInPeriod";
        String mapFunction = String.format("function () {" +
                "if (%s >= %s && %s <= %s)" +
                " emit(%s, 1); }", T_TIME_STAMP, fromDate.getTime(), T_TIME_STAMP, toDate.getTime(), T_URL);
        String reduceFunction = "function(key, values) { return Array.sum(values); }";

        collection.mapReduce(mapFunction, reduceFunction).collectionName(destinationName).toCollection();
        return dataBase.getCollection(destinationName).find().sort(descending("value"));
    }
    public MongoIterable<Document> getTotalVisitsCountAndTimeOfIps() {
        String destinationName = "totalVisitsCountAndTimeOfIps";
        String mapFunction = String.format("function (){ emit(%s, {totalCount: 1, totalDuration: %s}); }", T_IP, T_TIME_SPENT);
        String reduceFunction = "function(key, values) {" +
                "var totalCount = 0; " +
                "var totalDuration = 0; " +
                "for (var i in values) {" +
                "totalCount += values[i].totalCount;" +
                "totalDuration += values[i].totalDuration;}" +
                "return {totalCount: totalCount, totalDuration: totalDuration}; }";

        collection.mapReduce(mapFunction, reduceFunction).collectionName(destinationName).toCollection();
        return dataBase.getCollection(destinationName).find().sort(descending("totalCount", "totalDuration"));
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public MongoDatabase getDatabase() {
        return dataBase;
    }
    public void setDatabase(MongoDatabase database) {
        this.dataBase = database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}