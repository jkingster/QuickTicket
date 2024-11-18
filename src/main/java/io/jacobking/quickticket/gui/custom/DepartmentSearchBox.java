package io.jacobking.quickticket.gui.custom;

import io.jacobking.quickticket.gui.model.DepartmentModel;
import javafx.collections.ObservableList;

public class DepartmentSearchBox extends CustomSearchBox<DepartmentModel>  {
    public DepartmentSearchBox(ObservableList<DepartmentModel> items) {
        super(items);
    }
}
