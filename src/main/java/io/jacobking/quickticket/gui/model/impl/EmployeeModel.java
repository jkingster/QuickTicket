package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeModel extends ViewModel<Employee> {

    private final StringProperty  firstNameProperty    = new SimpleStringProperty();
    private final StringProperty  lastNameProperty     = new SimpleStringProperty();
    private final StringProperty  emailProperty        = new SimpleStringProperty();
    private final StringProperty  titleProperty        = new SimpleStringProperty();
    private final IntegerProperty companyIdProperty    = new SimpleIntegerProperty();
    private final IntegerProperty departmentIdProperty = new SimpleIntegerProperty();
    private final IntegerProperty profilePictureId     = new SimpleIntegerProperty();

    public EmployeeModel(int id, String firstName, String lastName, String email, String title, int companyId, int departmentId, int profilePictureId) {
        super(id);
        this.firstNameProperty.setValue(firstName);
        this.lastNameProperty.setValue(lastName);
        this.emailProperty.setValue(email);
        this.titleProperty.setValue(title);
        this.companyIdProperty.setValue(companyId);
        this.departmentIdProperty.setValue(departmentId);
        this.profilePictureId.setValue(profilePictureId);
    }

    public EmployeeModel(Employee employee) {
        this(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getTitle(),
                employee.getCompanyId(),
                employee.getDepartmentId(),
                employee.getProfilePicture()
        );
    }


    public String getFirstName() {
        return firstNameProperty.getValueSafe();
    }

    public void setFirstName(final String firstName) {
        this.firstNameProperty.setValue(firstName);
    }

    public String getLastName() {
        return lastNameProperty.getValueSafe();
    }

    public void setLastName(final String lastName) {
        this.lastNameProperty.setValue(lastName);
    }

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }

    public String getEmail() {
        return emailProperty.getValueSafe();
    }

    public void setEmail(final String email) {
        this.emailProperty.setValue(email);
    }

    public String getTitle() {
        return titleProperty.getValueSafe();
    }

    public void setTitle(final String title) {
        this.titleProperty.setValue(title);
    }

    public int getCompanyIdProperty() {
        return companyIdProperty.get();
    }

    public IntegerProperty companyIdPropertyProperty() {
        return companyIdProperty;
    }

    public void setCompanyIdProperty(int companyIdProperty) {
        this.companyIdProperty.set(companyIdProperty);
    }

    public int getDepartmentIdProperty() {
        return departmentIdProperty.get();
    }

    public IntegerProperty departmentIdPropertyProperty() {
        return departmentIdProperty;
    }

    public void setDepartmentIdProperty(int departmentIdProperty) {
        this.departmentIdProperty.set(departmentIdProperty);
    }

    public int getProfilePictureId() {
        return profilePictureId.get();
    }

    public IntegerProperty profilePictureIdProperty() {
        return profilePictureId;
    }

    public void setProfilePictureId(int profilePictureId) {
        this.profilePictureId.set(profilePictureId);
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public Employee toEntity() {
        return new Employee()
                .setId(getId())
                .setFirstName(getFirstName())
                .setLastName(getLastName())
                .setEmail(getEmail())
                .setTitle(getTitle())
                .setCompanyId(getCompanyIdProperty())
                .setDepartmentId(getDepartmentIdProperty())
                .setProfilePicture(getProfilePictureId());
    }
}
