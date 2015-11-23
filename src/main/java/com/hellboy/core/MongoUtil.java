package com.hellboy.core;

import com.google.common.collect.Lists;
import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.Document;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ERP-05 on 2015/10/10.
 */
public class MongoUtil<T> {

    //private static final  String ip="192.168.31.168";
    private static final  String ip="192.168.4.134";
    public static void main(String[] args) throws IOException {

    }

    public static void saveMoneyFlow(List<MoneyFlow> data,String name){
        MongoClient mongoClient = new MongoClient(ip , 27017 );
        MongoDatabase database = mongoClient.getDatabase("stock");
        MongoCollection<Document> collection = database.getCollection(name);
        collection.drop();
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documents = Lists.newArrayList();
        data.stream().map(x->{
                Document document = new Document("date",x.getDate());
                document.append("time",x.getTime())
                        .append("name",x.getStock().getName())
                        .append("num", x.getStock().getNum())
                        .append("trade",x.getTrade())
                        .append("changeratio",x.getChangeratio())
                        .append("mainnetmount",x.getMainnetmount())
                        .append("netamount",x.getNetamount())
                        .append("turnover",x.getTurnover())
                        .append("volume",x.getVolume());
                documents.add(document);
                return x;
        }).collect(Collectors.toList());
        collection.insertMany(documents);
    }

    public static void saveMoneyFlowHistory(List<MoneyFlow> data,String name){
        MongoClient mongoClient = new MongoClient(ip , 27017 );
        MongoDatabase database = mongoClient.getDatabase("stock");
        MongoCollection<Document> collection = database.getCollection(name);
        //collection.drop();
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documents = Lists.newArrayList();
        data.stream().map(x->{
            Document document = new Document("date",x.getDate());
            document.append("time",x.getTime())
                    .append("name", x.getStock().getName())
                    .append("num", x.getStock().getNum())
                    .append("trade", x.getTrade())
                    .append("changeratio", x.getChangeratio())
                    .append("mainnetmount", x.getMainnetmount())
                    .append("netamount", x.getNetamount())
                    .append("turnover", x.getTurnover())
                    .append("volume", x.getVolume());
            documents.add(document);
            return x;
        }).collect(Collectors.toList());
        collection.insertMany(documents);
    }

    public static List<Stock> getStock() throws IOException {
            MongoClient mongoClient = new MongoClient( ip , 27017 );
            MongoDatabase database = mongoClient.getDatabase("stock");
            MongoCollection<Document> collection = database.getCollection("stock");

            List<Stock> stocks = Lists.newArrayList();
            for (Document cur : collection.find()) {
                Stock stock = new Stock();
                stock.setName(cur.getString("name"));
                stock.setNum(cur.getString("num"));
                stock.setIndustry(cur.getString("industry"));
                stock.setArea(cur.getString("area"));
                stocks.add(stock);
            }
            return stocks;
    }



    public static List<MoneyFlow> getMoneyFlow() throws IOException {
        MongoClient mongoClient = new MongoClient(ip , 27017 );
        MongoDatabase database = mongoClient.getDatabase("stock");
        MongoCollection<Document> collection = database.getCollection("moneyflow");

   /*     ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, Money.class);*/
        List<MoneyFlow> moneyFlows = Lists.newArrayList();
        FindIterable<Document> iterable =
                collection.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                MoneyFlow moneyFlow = new MoneyFlow();
                moneyFlow.setNum(document.getString("num"));
                moneyFlow.setName(document.getString("name"));
                moneyFlow.setChangeratio(document.getDouble("changeratio"));
                moneyFlow.setTrade(document.getDouble("trade"));
                moneyFlow.setMainnetmount(document.getDouble("mainnetmount"));
                moneyFlows.add(moneyFlow);
            }
        });
        return moneyFlows;
    }
    public static List<MoneyFlow> getMoneyFlowHistory(String num) throws IOException {
        MongoClient mongoClient = new MongoClient(ip , 27017 );
        MongoDatabase database = mongoClient.getDatabase("stock");
        MongoCollection<Document> collection = database.getCollection("moneyflowhistory");

   /*     ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, Money.class);*/
        List<MoneyFlow> moneyFlows = Lists.newArrayList();

        FindIterable<Document> iterable =
                collection.find(new Document("num", num));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                MoneyFlow moneyFlow = new MoneyFlow();
                moneyFlow.setNum(document.getString("num"));
                moneyFlow.setName(document.getString("name"));
                moneyFlow.setChangeratio(document.getDouble("changeratio"));
                moneyFlow.setTrade(document.getDouble("trade"));
                moneyFlow.setMainnetmount(document.getDouble("mainnetmount"));
                moneyFlow.setTime(document.getString("time"));
                moneyFlow.setDate(document.getString("date"));
                moneyFlows.add(moneyFlow);
            }
        });
        return moneyFlows;
    }
}
