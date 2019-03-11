package com.planet.staccato.model;

import java.util.Objects;

/**
 * LineString
 */
public class LineString extends GeoJson {

  private LineString2D coordinates;

  public LineString coordinates(LineString2D coordinates) {
    this.coordinates = coordinates;
    return this;
  }

   /**
   * Get coordinates
   * @return coordinates
  **/
  public LineString2D getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(LineString2D coordinates) {
    this.coordinates = coordinates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LineString lineString = (LineString) o;
    return Objects.equals(this.coordinates, lineString.coordinates) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LineString {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

