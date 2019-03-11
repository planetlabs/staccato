package com.planet.staccato.model;

import java.util.Objects;

/**
 * A double range with begin and end
 */
public class DoubleRange {

  private Double begin;
  private Double end;

  public DoubleRange begin(Double begin) {
    this.begin = begin;
    return this;
  }

   /**
   * Get begin
   * @return begin
  **/
  public Double getBegin() {
    return begin;
  }

  public void setBegin(Double begin) {
    this.begin = begin;
  }

  public DoubleRange end(Double end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  public Double getEnd() {
    return end;
  }

  public void setEnd(Double end) {
    this.end = end;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoubleRange doubleRange = (DoubleRange) o;
    return Objects.equals(this.begin, doubleRange.begin) &&
        Objects.equals(this.end, doubleRange.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(begin, end);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DoubleRange {\n");

    sb.append("    begin: ").append(toIndentedString(begin)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
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

