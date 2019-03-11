package com.planet.staccato.model;

import java.util.Objects;

/**
 * A geojson 2d polygon
 */
public class GeoPolygon2D {
  /**
   * Role of geometry
   */

  private TypeEnum type;

  private Polygon2D coordinates;

  public enum TypeEnum {
    POLYGON("Polygon");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }

  public GeoPolygon2D type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Role of geometry
   * @return roles
  **/
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GeoPolygon2D coordinates(Polygon2D coordinates) {
    this.coordinates = coordinates;
    return this;
  }

   /**
   * Get coordinates
   * @return coordinates
  **/
  public Polygon2D getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Polygon2D coordinates) {
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
    GeoPolygon2D geoPolygon2D = (GeoPolygon2D) o;
    return Objects.equals(this.type, geoPolygon2D.type) &&
        Objects.equals(this.coordinates, geoPolygon2D.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, coordinates);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoPolygon2D {\n");

    sb.append("    roles: ").append(toIndentedString(type)).append("\n");
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

