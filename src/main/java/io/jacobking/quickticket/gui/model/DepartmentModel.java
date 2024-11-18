package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.gui.NameModel;
import io.jacobking.quickticket.tables.pojos.Department;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentModel extends NameModel<Department> {

    private final IntegerProperty companyId   = new SimpleIntegerProperty();
    private final StringProperty  name        = new SimpleStringProperty();
    private final StringProperty  email       = new SimpleStringProperty();
    private final StringProperty  description = new SimpleStringProperty();

    public DepartmentModel(int id, int companyId, String name, String email, String description) {
        super(id);
        this.companyId.setValue(companyId);
        this.name.setValue(name);
        this.email.setValue(email);
        this.description.setValue(description);
    }

    public DepartmentModel(final Department department) {
        this(
                department.getId(),
                department.getCompanyId(),
                department.getName(),
                department.getEmail(),
                department.getDescription()
        );
    }

    public int getCompanyId() {
        return companyId.get();
    }

    public IntegerProperty companyIdProperty() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId.set(companyId);
    }

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override public Department toEntity() {
        return new Department()
                .setId(getId())
                .setCompanyId(getCompanyId())
                .setName(getName())
                .setEmail(getEmail())
                .setDescription(getDescription());
    }
}
