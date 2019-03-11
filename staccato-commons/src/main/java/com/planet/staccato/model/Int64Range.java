package com.planet.staccato.model;

import java.util.Objects;

/**
 * An integer range with begin and end
 */
public class Int64Range {

  private Long begin = null;
  private Long end = null;

  public Int64Range begin(Long begin) {
    this.begin = begin;
    return this;
  }

   /**
   * Get begin
   * @return begin
  **/
  public Long getBegin() {
    return begin;
  }

  public void setBegin(Long begin) {
    this.begin = begin;
  }

  public Int64Range end(Long end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
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
    Int64Range int64Range = (Int64Range) o;
    return Objects.equals(this.begin, int64Range.begin) &&
        Objects.equals(this.end, int64Range.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(begin, end);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Int64Range {\n");

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

