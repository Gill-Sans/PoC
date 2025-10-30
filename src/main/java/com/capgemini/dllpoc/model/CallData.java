package com.capgemini.dllpoc.model;

public class CallData {
    private String lang;
    private String accountNumber;
    private String name;
    private String problemDescription;

    public CallData(String lang) {
        this.lang = lang;
    }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }

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
