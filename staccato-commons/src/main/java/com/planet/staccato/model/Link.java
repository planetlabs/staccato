package com.planet.staccato.model;

import java.util.Objects;

/**
 * Link
 */
public class Link {

  private String href;
  private String rel;
  private String type;
  private String hreflang;
  private String title;

  public static Link build() {
    return new Link();
  }

  public Link href(String href) {
    this.href = href;
    return this;
  }

   /**
   * Get href
   * @return href
  **/
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public Link rel(String rel) {
    this.rel = rel;
    return this;
  }

   /**
   * Get rel
   * @return rel
  **/
  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public Link type(String type) {
    this.type = type;
    return this;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Link title(String title) {
    this.title = title;
    return this;
  }

  public Link hreflang(String hreflang) {
    this.hreflang = hreflang;
    return this;
  }

   /**
   * Get hreflang
   * @return hreflang
  **/
  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.href, link.href) &&
        Objects.equals(this.rel, link.rel) &&
        Objects.equals(this.type, link.type) &&
        Objects.equals(this.hreflang, link.hreflang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, rel, type, hreflang);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");

    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
    sb.append("    roles: ").append(toIndentedString(type)).append("\n");
    sb.append("    hreflang: ").append(toIndentedString(hreflang)).append("\n");
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

