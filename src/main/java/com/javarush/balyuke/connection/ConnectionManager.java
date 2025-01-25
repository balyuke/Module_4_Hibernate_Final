package com.javarush.balyuke.connection;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.NoArgsConstructor;
import com.javarush.balyuke.domain.City;
import com.javarush.balyuke.domain.Country;
import com.javarush.balyuke.domain.CountryLanguage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConnectionManager {

    private final Properties properties;

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionManager() {
        this.properties = loadConfigProperties();
    }

    private Properties loadConfigProperties() {

            Properties properties = new Properties();
            try {
                FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties");
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return properties;
    }


    public SessionFactory createMySQLSessionFactory() {
        return new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .setProperty(Environment.DIALECT, properties.getProperty("mysql.dialect"))
                .setProperty(Environment.DRIVER, properties.getProperty("mysql.driver"))
                .setProperty(Environment.URL, properties.getProperty("mysql.url"))
                .setProperty(Environment.USER, properties.getProperty("mysql.user"))
                .setProperty(Environment.PASS, properties.getProperty("mysql.password"))
                .setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, properties.getProperty("mysql.currentSessionContextClass"))
                .setProperty(Environment.HBM2DDL_AUTO, properties.getProperty("mysql.hbm2ddlAuto"))
                .setProperty(Environment.STATEMENT_BATCH_SIZE, properties.getProperty("mysql.statementBatchSize"))
                .buildSessionFactory();
    }


    public RedisClient createRedisClient() {
        String host = properties.getProperty("redis.host");
        int port = Integer.parseInt(properties.getProperty("redis.port"));
        return RedisClient.create(RedisURI.create(host, port));
    }
}
