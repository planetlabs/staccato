package com.planet.staccato.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * West, South, East, North
 */
public class Bbox extends ArrayList<BigDecimal> {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  @Override
  public String toString() {
    if (size() == 4) {
      return get(0) + "," + get(1) + "," + get(2) + "," + get(3);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("class Bbox {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

