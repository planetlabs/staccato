package com.planet.staccato.model;

import java.util.Objects;

/**
 * provides the core metatdata fields plus extensions
 */
public class ItemProperties {

    protected String datetime;

    public ItemProperties datetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

    /**
     * Get start
     *
     * @return start
     **/
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemProperties itemProperties = (ItemProperties) o;
        return Objects.equals(this.datetime, itemProperties.datetime) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datetime, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemProperties {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    datetime: ").append(toIndentedString(datetime)).append("\n");
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

