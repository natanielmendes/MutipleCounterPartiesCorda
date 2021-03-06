package com.genpact.agreementnegotiation.state;

public enum AgreementEnumState {
    INITIAL("Initiated"),
    AMEND("Amended"),
    PARTIAL_ACCEPTED("Partially Agreed"),
    //REMOVED("Removed"),
    REMOVED("On Hold"),
    FULLY_ACCEPTED("Agreed");

    private final String name;
    AgreementEnumState(String s) {
        name = s;
    }
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }
    public String toString() {
        return this.name;
    }

}