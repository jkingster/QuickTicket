package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.Model;
import io.jacobking.quickticket.tables.pojos.Company;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyModel extends Model<Company> {
    private final StringProperty  name        = new SimpleStringProperty();
    private final StringProperty  description = new SimpleStringProperty();
    private final StringProperty  email       = new SimpleStringProperty();
    private final StringProperty  street      = new SimpleStringProperty();
    private final StringProperty  state       = new SimpleStringProperty();
    private final IntegerProperty zipCode     = new SimpleIntegerProperty();
    private final StringProperty  country     = new SimpleStringProperty();

    public CompanyModel(int id, String name, String description, String email, String street,
                        String state, int zipCode, String country) {
        super(id);
        this.name.setValue(name);
        this.description.setValue(description);
        this.email.setValue(email);
        this.street.setValue(street);
        this.state.setValue(state);
        this.zipCode.setValue(zipCode);
        this.country.setValue(country);
    }

    public CompanyModel(final Company company) {
        this(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getEmail(),
                company.getStreet(),
                company.getState(),
                company.getZipcode(),
                company.getCountry()
        );
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public int getZipCode() {
        return zipCode.get();
    }

    public IntegerProperty zipCodeProperty() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    @Override public Company toEntity() {
        return new Company()
                .setId(getId())
                .setName(getName())
                .setDescription(getDescription())
                .setEmail(getEmail())
                .setStreet(getStreet())
                .setState(getState())
                .setCountry(getCountry())
                .setZipcode(getZipCode());
    }
}
