package com.planet.staccato.model;

import java.util.Objects;

/**
 * MultiPoint
 */
public class MultiPoint extends GeoJson {

  private MultiPoint2D coordinates;

  public MultiPoint coordinates(MultiPoint2D coordinates) {
    this.coordinates = coordinates;
    return this;
  }

   /**
   * Get coordinates
   * @return coordinates
  **/
  public MultiPoint2D getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(MultiPoint2D coordinates) {
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
    MultiPoint multiPoint = (MultiPoint) o;
    return Objects.equals(this.coordinates, multiPoint.coordinates) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultiPoint {\n");
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

