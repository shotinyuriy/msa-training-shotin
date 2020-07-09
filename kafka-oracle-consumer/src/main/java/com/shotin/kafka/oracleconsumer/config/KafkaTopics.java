package com.shotin.kafka.oracleconsumer.config;

public class KafkaTopics {
    private String bankAccountsTopic;
    private String addressesTopic;

    public String getBankAccountsTopic() {
        return bankAccountsTopic;
    }

    public void setBankAccountsTopic(String bankAccountsTopic) {
        this.bankAccountsTopic = bankAccountsTopic;
    }

    public String getAddressesTopic() {
        return addressesTopic;
    }

    public void setAddressesTopic(String addressesTopic) {
        this.addressesTopic = addressesTopic;
    }
}
