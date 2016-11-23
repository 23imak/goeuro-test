package goeuro.devtest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import static com.google.common.base.Preconditions.checkArgument;


public class LocationAPIClient {
  public static final String URL = "http://api.goeuro.com/api/v2/position/suggest/en/";
  public static final String CSV_EXTENSION = ".csv";

  private final static Logger LOGGER = Logger.getLogger(LocationAPIClient.class.getName());
  private static CsvMapper mapper = new CsvMapper();
  private static RestTemplate restTemplate = new RestTemplate();

  /**
   * Queries the Location JSON API.
   * @param city the query parameter.
   * @return {@code List} of objects of type {@link City}
   */
  public List<City> querySuggestedCities(String city) {
    checkArgument(city != null, "Query parameter must not be null");
    checkArgument(!city.isEmpty(), "Empty query value");
    City[] cities = restTemplate.getForObject(URL.concat("{city}"), City[].class, city);
    return Arrays.asList(cities);
  }

  /**
   * Writes list of object to a csv file.
   * @param objects {@code List} of objects to be written into the csv file.
   * @param schema schema for the csv file
   * @param fileName name of the  csv file.
   */
  public void writeToCSV(List<?> objects, CsvSchema schema, String fileName)
      throws JsonGenerationException, JsonMappingException, IOException {
    checkArgument(objects != null, "List must not be null" );
    checkArgument(objects != null && !fileName.isEmpty(), "Wrong csv file name" );
    ObjectWriter writer = mapper.writer(schema.withLineSeparator("\n"));
    writer.writeValue(new File(fileName), objects);
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("No arguments! Pass city name as a query parameter.");
      System.exit(0);
    }
    String city = args[0];
    LocationAPIClient restConsumer = new LocationAPIClient();

    try {
      CsvSchema schema = mapper.schemaFor(City.class).withHeader();
      List<City> cities = restConsumer.querySuggestedCities(city);
      restConsumer.writeToCSV(cities, schema, city.concat(CSV_EXTENSION));
    } catch (JsonGenerationException e) {
      LOGGER.severe(String.format("Exception while writing to a file. %s", e.getMessage()));
    } catch (JsonMappingException e) {
      LOGGER.severe(String.format("Exception while writing to a file. %s", e.getMessage()));
    } catch (IOException e) {
      LOGGER.severe(String.format("Exception while writing to a file. %s", e.getMessage()));
    } catch (IllegalArgumentException e) {
      LOGGER.severe(String.format("Wrong parameter. %s", e.getMessage()));
    }
  }
}
