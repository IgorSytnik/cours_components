package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Department;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.people.Student;
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

// TODO: 31.05.2021 ListHasStudents should be creating automatically when a
//  new student and a new groupssubject is created
@Route(value = "master-detail-student/:studentID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-student")
public class MasterDetailViewStudent extends Div implements BeforeEnterObserver {

    private final String studentID = "studentID";
    private final String STUDENT_EDIT_ROUTE_TEMPLATE = "master-detail-student/%d/edit";

    private Grid<Student> grid = new Grid<>(Student.class, false);

    private TextField name;
    private ComboBox<Group> groupSelection;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Student> binder;

    private Student student;

    private ModeratorController moderatorController;

    public MasterDetailViewStudent(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(Student.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true)
                .setHeader("Name");
        grid.addColumn(e -> e.getGroup().getName(), "group.name").setAutoWidth(true)
                .setHeader("Group");

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getStudentService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(STUDENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewStudent.class);
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
                if (this.student == null) {
                    this.student = new Student();
                }
                binder.writeBean(this.student);

                moderatorController.updateStudent(this.student);

                clearForm();
                refreshGrid();
                Notification.show("Student details stored.");
                UI.getCurrent().navigate(MasterDetailViewStudent.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the student details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.student == null) {
                return;
            }

            moderatorController.deleteStudent(this.student.getId());
            clearForm();
            refreshGrid();
            Notification.show("Student was deleted.");
            UI.getCurrent().navigate(MasterDetailViewStudent.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> studentId = event.getRouteParameters().getLong(studentID);
        if (studentId.isPresent()) {
            Optional<Student> studentFromBackend = moderatorController.getStudent(studentId.get());
            if (studentFromBackend.isPresent()) {
                populateForm(studentFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested student was not found, ID = %d", studentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewStudent.class);
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

        groupSelection = new ComboBox<>("Group");
        groupSelection.setItems(moderatorController.getAllGroups());
        groupSelection.setItemLabelGenerator(Group::getName);

        binder.forField(groupSelection)
                .bind(Student::getGroup, Student::setGroup);

        name = new TextField("Name");
        Component[] fields = new Component[]{name, groupSelection};

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

    private void populateForm(Student value) {
        this.student = value;
        binder.readBean(this.student);

    }
}
