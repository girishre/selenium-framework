package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    Properties properties;

    public ConfigReader() {

        properties = new Properties();

        try {

            FileInputStream file =
                    new FileInputStream("src/test/resources/config.properties");

            properties.load(file);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public String getBrowser() {

        return properties.getProperty("browser");
    }

    public String getUrl() {

        return properties.getProperty("url");
    }

    public int getTimeout() {

        return Integer.parseInt(properties.getProperty("timeout"));
    }
}