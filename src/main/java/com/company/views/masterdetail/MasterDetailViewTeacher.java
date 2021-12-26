package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Department;
import com.company.domain.hei.Faculty;
import com.company.domain.people.AcademicPosition;
import com.company.domain.people.Teacher;
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

@Route(value = "master-detail-teacher/:teacherID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Teacher")
public class MasterDetailViewTeacher extends Div implements BeforeEnterObserver {

    private final String TEACHER_ID = "teacherID";
    private final String TEACHER_EDIT_ROUTE_TEMPLATE = "master-detail-teacher/%d/edit";

    private Grid<Teacher> grid = new Grid<>(Teacher.class, false);

    private TextField name;
    private ComboBox<Department> departmentSelection;
    private ComboBox<AcademicPosition> positionSelection;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Teacher> binder;

    private Teacher teacher;

    private ModeratorController moderatorController;

    public MasterDetailViewTeacher(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(Teacher.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true)
                .setHeader("Name");
        grid.addColumn(e -> e.getPosition().getName(), "position").setAutoWidth(true)
                .setHeader("Position");
        grid.addColumn(e -> e.getDepartment().getName(), "department.name").setAutoWidth(true)
                .setHeader("Department");

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getTeacherService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TEACHER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewTeacher.class);
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
                if (this.teacher == null) {
                    this.teacher = new Teacher();
                }
                binder.writeBean(this.teacher);

                moderatorController.updateTeacher(this.teacher);
                clearForm();
                refreshGrid();
                Notification.show("Teacher details stored.");
                UI.getCurrent().navigate(MasterDetailViewTeacher.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the teacher details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.teacher == null) {
                return;
            }

            moderatorController.deleteTeacher(this.teacher.getId());
            clearForm();
            refreshGrid();
            Notification.show("Teacher was deleted.");
            UI.getCurrent().navigate(MasterDetailViewTeacher.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> teacherId = event.getRouteParameters().getLong(TEACHER_ID);
        if (teacherId.isPresent()) {
            Optional<Teacher> teacherFromBackend = moderatorController.getTeacher(teacherId.get());
            if (teacherFromBackend.isPresent()) {
                populateForm(teacherFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested teacher was not found, ID = %d", teacherId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewTeacher.class);
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
        departmentSelection = new ComboBox<>("Department");
        departmentSelection.setItems(moderatorController.getAllDepartments());
        departmentSelection.setItemLabelGenerator(Department::getName);

        binder.forField(departmentSelection)
                .bind(Teacher::getDepartment, Teacher::setDepartment);

        positionSelection = new ComboBox<>("Position");
        positionSelection.setItems(AcademicPosition.values());
        positionSelection.setItemLabelGenerator(AcademicPosition::getName);

        binder.forField(positionSelection)
                .bind(Teacher::getPosition, Teacher::setPosition);

        name = new TextField("Name");
        Component[] fields = new Component[]{name, positionSelection, departmentSelection};

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

    private void populateForm(Teacher value) {
        this.teacher = value;
        binder.readBean(this.teacher);

    }
}
