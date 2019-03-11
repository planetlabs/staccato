package com.planet.staccato.model;

import java.util.Objects;

/**
 * MultiLineString
 */
public class MultiLineString extends GeoJson {

  private MultiLineString2D coordinates;

  public MultiLineString coordinates(MultiLineString2D coordinates) {
    this.coordinates = coordinates;
    return this;
  }

   /**
   * Get coordinates
   * @return coordinates
  **/
  public MultiLineString2D getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(MultiLineString2D coordinates) {
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
    MultiLineString multiLineString = (MultiLineString) o;
    return Objects.equals(this.coordinates, multiLineString.coordinates) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultiLineString {\n");
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

