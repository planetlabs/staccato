package com.boundlessgeo.staccato.model;

import java.util.Objects;

/**
 * begin and end timestamps adhering to RFC3339
 */
public class TimeRange {

  private String begin;
  private String end;

  public TimeRange begin(String begin) {
    this.begin = begin;
    return this;
  }

   /**
   * Get begin
   * @return begin
  **/
  public String getBegin() {
    return begin;
  }

  public void setBegin(String begin) {
    this.begin = begin;
  }

  public TimeRange end(String end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
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
    TimeRange timeRange = (TimeRange) o;
    return Objects.equals(this.begin, timeRange.begin) &&
        Objects.equals(this.end, timeRange.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(begin, end);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeRange {\n");

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

