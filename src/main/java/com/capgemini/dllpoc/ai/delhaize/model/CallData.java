package com.capgemini.dllpoc.ai.delhaize.model;

public record CallData(
        String lang,
        String accountNumber,
        String name,
        String problemDescription
) {
    @Override
    public String toString() {
        return "CallData{" +
                "lang='" + lang + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", problemDescription='" + problemDescription + '\'' +
                '}';
    }
}
