package com.planet.staccato.model;

/**
 * Gets or Sets ItemType
 */

public enum ItemType {
  
  POINT("Point"),
  
  MULTIPOINT("MultiPoint"),
  
  LINESTRING("LineString"),
  
  MULTILINESTRING("MultiLineString"),
  
  POLYGON("Polygon"),
  
  MULTIPOLYGON("MultiPolygon"),
  
  GEOMETRYCOLLECTION("GeometryCollection"),
  
  ITEM("Item"),
  
  ITEMCOLLECTION("ItemCollection");

  private String value;

  ItemType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ItemType fromValue(String text) {
    for (ItemType b : ItemType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

}

