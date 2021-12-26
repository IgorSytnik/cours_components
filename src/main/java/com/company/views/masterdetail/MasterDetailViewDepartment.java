package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "master-detail-department/:departmentID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Department")
public class MasterDetailViewDepartment extends Div implements BeforeEnterObserver {

    private final String DEPARTMENT_ID = "departmentID";
    private final String DEPARTMENT_EDIT_ROUTE_TEMPLATE = "master-detail-department/%d/edit";

    private Grid<Department> grid = new Grid<>(Department.class, false);

    private TextField name;
//    private TextField facultyName;
    private ComboBox<Faculty> facultySelection;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Department> binder;

    private Department department;

    private ModeratorController moderatorController;

    public MasterDetailViewDepartment(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(Department.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true)
                .setHeader("Name");
        grid.addColumn(e -> e.getFaculty().getName(), "faculty.name").setAutoWidth(true)
                .setHeader("Faculty");

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getDepartmentService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(DEPARTMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewDepartment.class);
            }
        });

        // Configure Form
//        binder = new BeanValidationBinder<>(Department.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.department == null) {
                    this.department = new Department();
                }
                binder.writeBean(this.department);

                moderatorController.updateDepartment(this.department);
                clearForm();
                refreshGrid();
                Notification.show("Department details stored.");
                UI.getCurrent().navigate(MasterDetailViewDepartment.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the department details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.department == null) {
                return;
            }

            moderatorController.deleteDepartment(this.department.getId());
            clearForm();
            refreshGrid();
            Notification.show("Department was deleted.");
            UI.getCurrent().navigate(MasterDetailViewDepartment.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> departmentId = event.getRouteParameters().getLong(DEPARTMENT_ID);
        if (departmentId.isPresent()) {
            Optional<Department> departmentFromBackend = moderatorController.getDepartment(departmentId.get());
            if (departmentFromBackend.isPresent()) {
                populateForm(departmentFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested department was not found, ID = %d", departmentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewDepartment.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        facultySelection = new ComboBox<>("Faculty");
        facultySelection.setItems(moderatorController.getAllFaculties());
        facultySelection.setItemLabelGenerator(Faculty::getName);

        binder.forField(facultySelection)
                .bind(Department::getFaculty, Department::setFaculty);

        name = new TextField("Name");
//        facultyName = new TextField("Faculty");
        Component[] fields = new Component[]{name, facultySelection};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(delete, save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Department value) {
        this.department = value;
        binder.readBean(this.department);

    }
}
