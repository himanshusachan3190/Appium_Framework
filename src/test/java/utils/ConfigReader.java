package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader — reads key-value pairs from config.properties.
 *
 * Usage:
 *   String user = ConfigReader.get("valid.username");
 *   String pass = ConfigReader.get("valid.password");
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    // Static block — loads the file once when the class is first accessed
    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Returns the value for the given key from config.properties.
     *
     * @param key the property key (e.g. "valid.username")
     * @return the value, or null if the key doesn't exist
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
