package com.planet.staccato.model;

import java.util.Objects;

/**
 * ItemCollectionLinks
 */
public class ItemCollectionLinks {

  private String next;

  public ItemCollectionLinks next(String next) {
    this.next = next;
    return this;
  }

   /**
   * A URL to obtain the next paginated data set. If not present, the client should utilize the &#x60;nextPageToken&#x60;.
   * @return next
  **/
  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemCollectionLinks itemCollectionLinks = (ItemCollectionLinks) o;
    return Objects.equals(this.next, itemCollectionLinks.next);
  }

  @Override
  public int hashCode() {
    return Objects.hash(next);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemCollectionLinks {\n");

    sb.append("    next: ").append(toIndentedString(next)).append("\n");
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

