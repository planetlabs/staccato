package com.planet.staccato.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * provides the core metatdata fieldsExtension plus extensions
 */
@Data
public class ItemProperties {

    protected String datetime;

    protected String license;
    protected List<Provider> providers;
    protected String platform;
    protected List<String> instruments;
    protected String constellation;
    protected String mission;
    protected String created;
    protected String updated;

    public ItemProperties datetime(String datetime) {
        setDatetime(datetime);
        return this;
    }

    public ItemProperties license(String license) {
        setLicense(license);
        return this;
    }

    public ItemProperties providers(List<Provider> providers) {
        setProviders(providers);
        return this;
    }

    public ItemProperties addProvider(Provider provider) {
        if (providers == null) {
            providers = new ArrayList<>();
        }
        providers.add(provider);
        return this;
    }

    public ItemProperties addProviders(Collection<Provider> provider) {
        if (providers == null) {
            providers = new ArrayList<>();
        }
        providers.addAll(providers);
        return this;
    }

    public ItemProperties platform(String platform) {
        setPlatform(platform);
        return this;
    }

    public ItemProperties instruments(List<String> instruments) {
        setInstruments(instruments);
        return this;
    }

    public ItemProperties addInstrument(String instrument) {
        if (instruments == null) {
            instruments = new ArrayList<>();
        }
        instruments.add(instrument);
        return this;
    }

    public ItemProperties addInstruments(Collection<String> instrument) {
        if (instruments == null) {
            instruments = new ArrayList<>();
        }
        instruments.addAll(instruments);
        return this;
    }

    public ItemProperties constellation(String constellation) {
        setConstellation(constellation);
        return this;
    }

    public ItemProperties mission(String mission) {
        setMission(mission);
        return this;
    }

    public ItemProperties created(String created) {
        setCreated(created);
        return this;
    }

    public ItemProperties updated(String updated) {
        setUpdated(updated);
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

