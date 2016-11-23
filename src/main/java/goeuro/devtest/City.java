package goeuro.devtest;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = { "_id", "name", "type", "latitude", "longitude" })
public class City {

  @JsonProperty("_id")
  private long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("geo_position")
  private GeoLocation location;

  @JsonGetter("_id")
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public Double getLatitude() {
    return location.getLatitude();
  }

  public Double getLongitude() {
    return location.getLongitude();
  }

  /**
   * Returns comma separated values representation of an instance of {@code City}
   * in the following format: "_id,name,type,latitude,longitude".
   */
  @Override
  public String toString() {
    return String.format("%d,%s,%s,%f,%f", id, name, type, getLatitude(),
      getLongitude());
  }

  private static class GeoLocation {

    private double longitude;
    private double latitude;

    public double getLatitude() {
      return latitude;
    }

    public double getLongitude() {
      return longitude;
    }
  }
}
