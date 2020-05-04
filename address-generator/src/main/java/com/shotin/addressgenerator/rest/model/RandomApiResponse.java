package com.shotin.addressgenerator.rest.model;

import java.util.List;

public class RandomApiResponse {
    private List<RandomNumericResult> results;

    public List<RandomNumericResult> getResults() {
        return results;
    }

    public void setResults(List<RandomNumericResult> results) {
        this.results = results;
    }
}
