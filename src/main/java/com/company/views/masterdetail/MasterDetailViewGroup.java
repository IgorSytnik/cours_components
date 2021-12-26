package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Department;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.Specialty;
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

@Route(value = "master-detail-group/:groupID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Group")
public class MasterDetailViewGroup extends Div implements BeforeEnterObserver {

    private final String groupID = "groupID";
    private final String GROUP_EDIT_ROUTE_TEMPLATE = "master-detail-group/%d/edit";

    private Grid<Group> grid = new Grid<>(Group.class, false);

    private TextField name;
    private ComboBox<Integer> yearSelection;
    private ComboBox<Department> departmentSelection;
    private ComboBox<Specialty> specialtySelection;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Group> binder;

    private Group group;

    private ModeratorController moderatorController;

    public MasterDetailViewGroup(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(Group.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true)
                .setHeader("Name");
        grid.addColumn("year").setAutoWidth(true)
                .setHeader("Year");
        grid.addColumn(e -> e.getDepartment().getName(), "department.name").setAutoWidth(true)
                .setHeader("Department");
        grid.addColumn(e -> e.getSpecialty().getName(), "specialty.name").setAutoWidth(true)
                .setHeader("Specialty");

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getGroupService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(GROUP_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewGroup.class);
            }
        });

        // Configure Form

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.group == null) {
                    this.group = new Group();
                }
                binder.writeBean(this.group);

                moderatorController.updateGroup(this.group);
                clearForm();
                refreshGrid();
                Notification.show("Group details stored.");
                UI.getCurrent().navigate(MasterDetailViewGroup.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the group details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.group == null) {
                return;
            }

            moderatorController.deleteGroup(this.group.getId());
            clearForm();
            refreshGrid();
            Notification.show("Group was deleted.");
            UI.getCurrent().navigate(MasterDetailViewGroup.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> groupId = event.getRouteParameters().getLong(groupID);
        if (groupId.isPresent()) {
            Optional<Group> groupFromBackend = moderatorController.getGroup(groupId.get());
            if (groupFromBackend.isPresent()) {
                populateForm(groupFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested group was not found, ID = %d", groupId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewGroup.class);
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

        yearSelection = new ComboBox<>("Year");
        yearSelection.setPreventInvalidInput(true);
        yearSelection.setItems(1, 2, 3, 4, 5, 6);
//        yearSelection.addCustomValueSetListener(e -> yearSelection.setValue(e.getSource().getValue()));

        binder.forField(yearSelection)
                .bind(Group::getYear, Group::setYear);

        departmentSelection = new ComboBox<>("Department");
        departmentSelection.setItems(moderatorController.getAllDepartments());
        departmentSelection.setItemLabelGenerator(Department::getName);

        binder.forField(departmentSelection)
                .bind(Group::getDepartment, Group::setDepartment);

        specialtySelection = new ComboBox<>("Specialty");
        specialtySelection.setItems(moderatorController.getAllSpecialties());
        specialtySelection.setItemLabelGenerator(Specialty::getName);

        binder.forField(specialtySelection)
                .bind(Group::getSpecialty, Group::setSpecialty);

        name = new TextField("Name");
        Component[] fields = new Component[]{name, yearSelection, departmentSelection, specialtySelection};

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

    private void populateForm(Group value) {
        this.group = value;
        binder.readBean(this.group);

    }
}
