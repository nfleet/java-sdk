package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
public class AddressData {
	
	private double Confidence;
	private String Resolution;
	
	private String Region;
	private String Country;
	private String City;
	private String PostalCode;
	private String Street;
	private int HouseNumber;
	private String ApartmentLetter;
	private int ApartmentNumber;
	
	private String AdditionalInfo;
	
	public double getConfidence() {
		return Confidence;
	}
	public void setConfidence(double confidence) {
		Confidence = confidence;
	}
	public String getResolution() {
		return Resolution;
	}
	public void setResolution(String resolution) {
		Resolution = resolution;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public int getHouseNumber() {
		return HouseNumber;
	}
	public void setHouseNumber(int houseNumber) {
		HouseNumber = houseNumber;
	}
	public String getApartmentLetter() {
		return ApartmentLetter;
	}
	public void setApartmentLetter(String apartmentLetter) {
		ApartmentLetter = apartmentLetter;
	}
	public int getApartmentNumber() {
		return ApartmentNumber;
	}
	public void setApartmentNumber(int apartmentNumber) {
		ApartmentNumber = apartmentNumber;
	}
	public String getAdditionalInfo() {
		return AdditionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		AdditionalInfo = additionalInfo;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getPostalCode() {
		return PostalCode;
	}
	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}
	public String getStreet() {
		return Street;
	}
	public void setStreet(String street) {
		Street = street;
	}
}
