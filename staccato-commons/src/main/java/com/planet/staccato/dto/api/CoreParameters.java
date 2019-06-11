package com.planet.staccato.dto.api;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
public interface CoreParameters {

    double[] getBbox();
    void setBbox(double[] bbox);

    String getTime();
    void setTime(String time);

    Integer getLimit();
    void setLimit(Integer limit);

    Integer getPage();
    void setPage(Integer page);

    String[] getIds();
    void setIds(String[] ids);

    String[] collections();
    void setCollections(String[] collections);

    Object getIntersects();
    void setIntersects(Object intersects);

}
