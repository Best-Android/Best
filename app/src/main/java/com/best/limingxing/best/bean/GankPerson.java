package com.best.limingxing.best.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class GankPerson {
    private boolean error;
    private List<Person> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Person> getResults() {
        return results;
    }

    public void setResults(List<Person> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GankPerson{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
