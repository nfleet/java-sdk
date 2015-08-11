package fi.cosky.sdk;

public enum AddressResolution {
    None(0),
    Coordinate(1),
    City(2),
    PostalCode(4),
    PostalCodeAfterCity(8),
    Street(16),
    HouseNumber(32),
    Inexact(64),
    Ambiguous(128),
    HouseNumberOutOfRange(256),
    HouseNumberNotGiven(512),
    HouseNumbersMissingFromStreetData(1024),
    HouseNumberNonexisting(2048);

    private int value;

    private AddressResolution(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
