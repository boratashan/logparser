package com.ef;

import com.ef.exceptions.ConfigFileException;

import java.io.InputStream;
import java.util.Properties;
/**
 * Holds application specific constants and parameters.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class AppConfig {

    private static final String CONFIG_KEY_DBURL = "dburl";
    private static final String CONFIG_KEY_DBUSER = "dbuser";
    private static final String CONFIG_KEY_DBPASSWORD = "dbpassword";
    private static final String CONFIG_FILE_NAME = "/config.properties";

    private static AppConfig instance;
    Properties prop;

    private String dbURL;
    private String dbUser;
    private String dbPassword;

    private AppConfig() throws ConfigFileException {
        prop = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE_NAME);
            try {
                prop.load(inputStream);
                this.dbURL = prop.getProperty(CONFIG_KEY_DBURL);
                this.dbUser = prop.getProperty(CONFIG_KEY_DBUSER);
                this.dbPassword = prop.getProperty(CONFIG_KEY_DBPASSWORD);
            } finally {
                inputStream.close();
            }
        } catch (java.io.IOException e) {
            throw new ConfigFileException("Configuration file is not found!", e);
        }
        ;
    }

    public synchronized static AppConfig getConfig() throws ConfigFileException {
        if (instance == null)
            instance = new AppConfig();
        return instance;
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }


}
