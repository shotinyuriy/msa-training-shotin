package com.shotin.kafkaproducer.model;

public class BankAccountSchedulerConfig {
    private Boolean enabled;
    private Integer batchSize;
    private String lastnameRegExp;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public String getLastnameRegExp() {
        return lastnameRegExp;
    }

    public void setLastnameRegExp(String lastnameRegExp) {
        this.lastnameRegExp = lastnameRegExp;
    }
}
