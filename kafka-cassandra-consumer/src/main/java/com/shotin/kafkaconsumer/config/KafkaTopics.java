package com.shotin.kafkaconsumer.config;

public class KafkaTopics {
    private String bankAccountsTopic;
    private String addressesTopic;
    private String bankAccountInfosTopic;

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

    public String getBankAccountInfosTopic() {
        return bankAccountInfosTopic;
    }

    public void setBankAccountInfosTopic(String bankAccountInfosTopic) {
        this.bankAccountInfosTopic = bankAccountInfosTopic;
    }
}
