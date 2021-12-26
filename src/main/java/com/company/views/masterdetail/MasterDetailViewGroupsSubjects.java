package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.hei.Department;
import com.company.domain.inanimate.Group;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.Specialty;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.domain.inanimate.subject.Subject;
import com.company.domain.people.Student;
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

@Route(value = "master-detail-groups-subjects/:groupsSubjectsID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Groups-Subjects")
public class MasterDetailViewGroupsSubjects extends Div implements BeforeEnterObserver {

    private final String groupsSubjectsID = "groupsSubjectsID";
    private final String GROUPS_SUBJECTS_EDIT_ROUTE_TEMPLATE = "master-detail-groups-subjects/%d/edit";

    private Grid<GroupsSubjects> grid = new Grid<>(GroupsSubjects.class, false);

    private ComboBox<Teacher> teacherComboBox;
    private ComboBox<Subject> subjectComboBox;
    private ComboBox<Group> groupComboBox;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<GroupsSubjects> binder;

    private GroupsSubjects groupsSubjects;

    private ModeratorController moderatorController;

    public MasterDetailViewGroupsSubjects(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(GroupsSubjects.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn(e -> e.getSubject().getName(), "subject.name").setAutoWidth(true)
                .setHeader("Subject");
        grid.addColumn(e -> e.getTeacher().getName(), "teacher.name").setAutoWidth(true)
                .setHeader("Teacher");
        grid.addColumn(e -> e.getGroup().getName(), "group.name").setAutoWidth(true)
                .setHeader("Group");

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getGroupsSubjectsService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(GROUPS_SUBJECTS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewGroupsSubjects.class);
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
                if (this.groupsSubjects == null) {
                    this.groupsSubjects = new GroupsSubjects();
                }
                binder.writeBean(this.groupsSubjects);

                moderatorController.updateGroupsSubjects(this.groupsSubjects);

                clearForm();
                refreshGrid();
                Notification.show("Group's subject details stored.");
                UI.getCurrent().navigate(MasterDetailViewGroupsSubjects.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the group's subject details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.groupsSubjects == null) {
                return;
            }

            moderatorController.deleteGroupsSubject(this.groupsSubjects.getId());
            clearForm();
            refreshGrid();
            Notification.show("Group's subject was deleted.");
            UI.getCurrent().navigate(MasterDetailViewGroupsSubjects.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> groupsSubjectsId = event.getRouteParameters().getLong(groupsSubjectsID);
        if (groupsSubjectsId.isPresent()) {
            Optional<GroupsSubjects> groupsSubjectsFromBackend = moderatorController.getGroupsSubject(groupsSubjectsId.get());
            if (groupsSubjectsFromBackend.isPresent()) {
                populateForm(groupsSubjectsFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested group's subject was not found, ID = %d", groupsSubjectsId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewGroupsSubjects.class);
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

        teacherComboBox = new ComboBox<>("Teacher");
        teacherComboBox.setItems(moderatorController.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getName);

        binder.forField(teacherComboBox)
                .bind(GroupsSubjects::getTeacher, GroupsSubjects::setTeacher);

        subjectComboBox = new ComboBox<>("Subject");
        subjectComboBox.setItems(moderatorController.getAllSubjects());
        subjectComboBox.setItemLabelGenerator(Subject::getName);

        binder.forField(subjectComboBox)
                .bind(GroupsSubjects::getSubject, GroupsSubjects::setSubject);

        groupComboBox = new ComboBox<>("Group");
        groupComboBox.setItems(moderatorController.getAllGroups());
        groupComboBox.setItemLabelGenerator(Group::getName);

        binder.forField(groupComboBox)
                .bind(GroupsSubjects::getGroup, GroupsSubjects::setGroup);

        Component[] fields = new Component[]{subjectComboBox, teacherComboBox, groupComboBox};

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

    private void populateForm(GroupsSubjects value) {
        this.groupsSubjects = value;
        binder.readBean(this.groupsSubjects);

    }
}
