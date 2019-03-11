package com.planet.staccato.model;

import java.util.Objects;

/**
 * GeometryCollection
 */
public class GeometryCollection extends GeoJson {

  private GeometryCollection2D geometries;

  public GeometryCollection geometries(GeometryCollection2D geometries) {
    this.geometries = geometries;
    return this;
  }

   /**
   * Get geometries
   * @return geometries
  **/
  public GeometryCollection2D getGeometries() {
    return geometries;
  }

  public void setGeometries(GeometryCollection2D geometries) {
    this.geometries = geometries;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeometryCollection geometryCollection = (GeometryCollection) o;
    return Objects.equals(this.geometries, geometryCollection.geometries) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(geometries, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeometryCollection {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    geometries: ").append(toIndentedString(geometries)).append("\n");
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

