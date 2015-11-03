package com.orion.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.logging.jdk.JDKLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class DatastoreFactoryBean implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(DatastoreFactoryBean.class);

    @Value("${mongodb.host}")
    private String dbHost;
    @Value("${mongodb.port}")
    private int dbPort;
    @Value("${mongodb.name}")
    private String dbName;
    @Value("${mongodb.user}")
    private String user;
    @Value("${mongodb.password}")
    private String password;
    @Value("${mongodb.replicaSetSeeds}")
    private String replicaSetSeeds;
    @Value("${mongodb.slaveOk}")
    private boolean slaveOk;
    @Value("${mongodb.safe}")
    private boolean safe;
    @Value("${mongodb.autoConnectRetry}")
    private boolean autoConnectRetry;
    @Value("${mongodb.connectionsPerHost}")
    private int connectionsPerHost;
    @Value("${mophia.mappingPackage}")
    private String mappingPackage;

    public DatastoreFactoryBean() {
    }

    public void afterPropertiesSet() throws Exception {
        logger.info("Initializing Morphia Datastore.");
    }

    @Bean(name = "datastore")
    public Datastore getObject() throws Exception {

        Assert.notNull(mappingPackage);
        Assert.notNull(dbName);

        MongoClient mongo = initMongo();

        try {
            MorphiaLoggerFactory.registerLogger(JDKLoggerFactory.class);
        } catch (RuntimeException ignore) {
            // ignore it.
        }

        Morphia morphia = new Morphia();

        morphia.mapPackage(mappingPackage);

        Datastore datastore = morphia.createDatastore(mongo, dbName);
        datastore.ensureIndexes();
        return datastore;
    }

    private MongoClient initMongo() throws UnknownHostException {
        ServerAddress address = new ServerAddress(dbHost, dbPort);

        MongoClientOptions options = new MongoClientOptions.Builder().connectionsPerHost(connectionsPerHost).build();

        MongoCredential credential = MongoCredential.createCredential(user, dbName, password.toCharArray());

        MongoClient mongo;

        // mongo = new Mongo(address, options);
        if (StringUtils.trimToNull(replicaSetSeeds) == null) {
            mongo = new MongoClient(address, Arrays.asList(credential), options);
        } else { // use replica set
            String[] hosts = StringUtils.split(replicaSetSeeds);
            List<ServerAddress> addr = new ArrayList<ServerAddress>();
            for (String host : hosts) {
                addr.add(new ServerAddress(host));
            }
            mongo = new MongoClient(addr, Arrays.asList(credential), options);
        }

        logger.info("Mongo options = {}, wc = {}", options.toString(), options.getWriteConcern());

        return mongo;
    }

    public Class<? extends Datastore> getObjectType() {
        return Datastore.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
