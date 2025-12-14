package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);

    public static Properties loadProperties(String env) {
        Properties prop = new Properties();
        if (env == null || env.isEmpty()) env = "staging";

        String path = System.getProperty("user.dir") + "/src/test/resources/config/" + env + ".properties";
        log.info("Loading properties from: {}", path);

        try (FileInputStream fis = new FileInputStream(path)) {
            prop.load(fis);
            log.info("Loaded properties: {}", path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }

        return prop;
    }
}
