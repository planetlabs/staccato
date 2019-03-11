package com.planet.staccato.model;

import java.util.Objects;

/**
 * root geojson object
 */

public class GeoJson {

  private ItemType type;

  public GeoJson type(ItemType type) {
    this.type = type;
    return this;
  }

   /**
   * Get roles
   * @return roles
  **/
  public ItemType getType() {
    return type;
  }

  public void setType(ItemType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoJson geoJson = (GeoJson) o;
    return Objects.equals(this.type, geoJson.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoJson {\n");

    sb.append("    roles: ").append(toIndentedString(type)).append("\n");
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

