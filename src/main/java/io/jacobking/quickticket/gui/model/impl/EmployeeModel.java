package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.EntityConverter;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeModel extends ViewModel<Employee>  {

    private final StringProperty firstNameProperty = new SimpleStringProperty();
    private final StringProperty lastNameProperty = new SimpleStringProperty();
    private final StringProperty emailProperty = new SimpleStringProperty();
    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty departmentProperty = new SimpleStringProperty();

    public EmployeeModel(int id, String firstName, String lastName, String email, String title, String department) {
        super(id);
        this.firstNameProperty.setValue(firstName);
        this.lastNameProperty.setValue(lastName);
        this.emailProperty.setValue(email);
        this.titleProperty.setValue(title);
        this.departmentProperty.setValue(department);
    }

    public EmployeeModel(Employee employee) {
        this(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getTitle(),
                employee.getDepartment()
        );
    }


    public void setFirstName(final String firstName) {
        this.firstNameProperty.setValue(firstName);
    }


    public void setLastName(final String lastName) {
        this.lastNameProperty.setValue(lastName);
    }

    public void setEmail(final String email) {
        this.emailProperty.setValue(email);
    }

    public void setTitle(final String title) {
        this.titleProperty.setValue(title);
    }

    public void setDepartmentProperty(final String department) {
        this.departmentProperty.setValue(department);
    }

    public String getFirstName() {
        return firstNameProperty.getValueSafe();
    }

    public String getLastName() {
        return lastNameProperty.getValueSafe();
    }

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }

    public String getEmail() {
        return emailProperty.getValueSafe();
    }

    public String getTitle() {
        return titleProperty.getValueSafe();
    }

    public String getDepartment() {
        return departmentProperty.getValueSafe();
    }


    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public Employee toEntity() {
        return new Employee()
                .setId(this.getId())
                .setFirstName(this.getFirstName())
                .setLastName(this.getLastName())
                .setEmail(this.getEmail())
                .setDepartment(this.getDepartment())
                .setTitle(this.getDepartment());
    }
}
