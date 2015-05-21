package ttt.consoleVersion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  // configurable variables
  public static int BOARD_ROWS;
  public static int BOARD_COLUMNS;
  public static boolean ENABLE_BOT;

  /**
   * This method loads the config properties file
   * 
   * @param file
   */
  public void load(String file) {
    Properties properties = new Properties();
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);

    try {
      if (inputStream != null) {
        properties.load(inputStream);
      } else {
        throw new FileNotFoundException("property file '" + file + "' not found in the classpath");
      }
    } catch (IOException io) {
      io.printStackTrace();
    }
    BOARD_ROWS = Integer.parseInt(properties.getProperty("rows"));
    BOARD_COLUMNS = Integer.parseInt(properties.getProperty("columns"));
    ENABLE_BOT = Boolean.parseBoolean(properties.getProperty("enable_bot"));
  }
}
