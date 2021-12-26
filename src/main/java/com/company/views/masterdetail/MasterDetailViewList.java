package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.controllers.TeacherController;
import com.company.domain.inanimate.GroupsSubjects;
import com.company.domain.inanimate.subject.Grade;
import com.company.domain.inanimate.subject.GradeDate;
import com.company.domain.inanimate.subject.ListHasStudents;
import com.company.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

// TODO: 01.06.2021 Error when selected 
@Route(value = "master-detail-list/:listHasStudentsID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "master-detail-list/:dateID?/:action?(create)", layout = MainView.class)
@PageTitle("Master-Detail-List")
public class MasterDetailViewList extends Div implements BeforeEnterObserver {

    private final String listHasStudentsID = "listHasStudentsID";
    private final String dateID = "dateID";
    private final String LIST_EDIT_ROUTE_TEMPLATE = "master-detail-list/%d/edit";
    private final String LIST_CREATE_ROUTE_TEMPLATE = "master-detail-list/%d/create";
//    private final String LIST_DELETE_ROUTE_TEMPLATE = "master-detail-list/delete";

    private Grid<GroupsSubjects> groupsSubjectsGrid = new Grid<>(GroupsSubjects.class, false);
    private Grid<ListHasStudents> listHasStudentsGrid1 = new Grid<>(ListHasStudents.class, false);

    private DatePicker dateOfGrade;
    private Checkbox checkboxA1;
    private Checkbox checkboxA2;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button deleteDate = new Button("Delete");
    private Button saveDate = new Button("Save");
    private Button addDate = new Button("+");

    private BeanValidationBinder<GroupsSubjects> binderGroupsSubjects;
    private BeanValidationBinder<ListHasStudents> binderList;
    private BeanValidationBinder<GradeDate> binderGradeDate;

    private GroupsSubjects groupsSubjects;
    private ListHasStudents listHasStudents;
    private GradeDate gradeDate;

    private ModeratorController moderatorController;
    private TeacherController teacherController;

    public MasterDetailViewList(
            @Autowired ModeratorController moderatorController,
            @Autowired TeacherController teacherController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        this.teacherController = teacherController;

        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binderGroupsSubjects = new BeanValidationBinder<>(GroupsSubjects.class);
        binderList = new BeanValidationBinder<>(ListHasStudents.class);
        binderGradeDate = new BeanValidationBinder<>(GradeDate.class);

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);

        createGridLayout(splitLayout);

        add(splitLayout);
        // Configure Grid
        groupsSubjectsGrid.addColumn(e -> e.getSubject().getName(), "subject.name")
                .setAutoWidth(true)
                .setHeader("Subject");
        groupsSubjectsGrid.addColumn(e -> e.getGroup().getName(), "group.name")
                .setAutoWidth(true)
                .setHeader("Group");
        groupsSubjectsGrid.addColumn(e -> e.getGroup().getYear(), "group.year")
                .setAutoWidth(true)
                .setHeader("Group year");
        groupsSubjectsGrid.setItemDetailsRenderer(
                new ComponentRenderer<>((groupsSubjects) -> {
                    Grid<ListHasStudents> listHasStudentsGrid = new Grid<>(ListHasStudents.class, false);
                    listHasStudentsGrid.addColumn(e -> e.getStudent().getName(), "student.name")
                            .setAutoWidth(true)
                            .setHeader("Student");
                    listHasStudentsGrid.addColumn(e -> e.getAttest1() == null ?
                                "-" : e.getAttest1() ? "a" : "n/a", "attest1")
                            .setAutoWidth(true)
                            .setHeader("First attestation");
                    listHasStudentsGrid.addColumn(e -> e.getAttest1() == null ?
                                "-" : e.getAttest2() ? "a" : "n/a", "attest2")
                            .setAutoWidth(true)
                            .setHeader("Second attestation");
                    for (GradeDate gradeDate1 : groupsSubjects.getGradeDateList()) {
                        listHasStudentsGrid.addColumn(e -> {
                            Optional<Grade> g;
                            g = e.getGrades().stream()
                                    .filter(grade -> grade.getGradeDate().equals(gradeDate1))
                                        .findAny();
                            return g.map(grade -> String.valueOf(grade.getGrade())).orElse("-");
                        }, "grades.grade")
                                .setAutoWidth(true)
                                .setHeader(gradeDate1.getDate().toString());
                    }
                    addDate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    listHasStudentsGrid.addColumn(e -> "")
                            .setAutoWidth(true)
                            .setHeader(addDate);
                    ListDataProvider<ListHasStudents> dataProvider =
                            DataProvider.ofCollection(groupsSubjects.getListHasStudentsList());
                    dataProvider.setSortOrder(e -> e.getStudent().getName(),
                            SortDirection.ASCENDING);
                    listHasStudentsGrid.setDataProvider(dataProvider);
                    listHasStudentsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
                    listHasStudentsGrid.setHeightByRows(true);
                    listHasStudentsGrid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                            createEditorLayout(splitLayout);
                            UI.getCurrent().navigate(String.format(LIST_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
                        } else {
                            clearForm();
                            UI.getCurrent().navigate(MasterDetailViewList.class);
                        }
                    });
                    return listHasStudentsGrid;
        }
        , (grid, groupsSubjects) -> {
                    Grid<ListHasStudents> listHasStudentsGrid = (Grid<ListHasStudents>) grid;
                    ListDataProvider<ListHasStudents> dataProvider =
                            DataProvider.ofCollection(groupsSubjects.getListHasStudentsList());
                    dataProvider.setSortOrder(e -> e.getStudent().getName(),
                            SortDirection.ASCENDING);
                    listHasStudentsGrid.setDataProvider(dataProvider);
                    listHasStudentsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
                    listHasStudentsGrid.setHeightByRows(true);
                    listHasStudentsGrid.select(null);
                    return listHasStudentsGrid;
                }
                ));

        groupsSubjectsGrid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getGroupsSubjectsService()));
        groupsSubjectsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        groupsSubjectsGrid.setHeightFull();

        // when a row is selected or deselected, populate form
        groupsSubjectsGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(LIST_CREATE_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewList.class);
            }
        });

        // Configure Form

        // Bind fields. This where you'd define e.g. validation rules

//        binderGradeDate.bindInstanceFields(this);
//        binderGroupsSubjects.bindInstanceFields(this);
//        binderList.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.listHasStudents == null) {
                    Notification.show("AAAAAAAAAAAAAAAAAAAAAAAA");
                    return;
                }
                binderList.writeBean(this.listHasStudents);

                moderatorController.updateListHasStudents(this.listHasStudents);

                clearForm();
                refreshGrid();
                Notification.show("Group's subject details stored.");
                UI.getCurrent().navigate(MasterDetailViewList.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the group's subject details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.listHasStudents == null) {
                return;
            }

            moderatorController.deleteListHasStudent(this.listHasStudents.getId());
            clearForm();
            refreshGrid();
            Notification.show("Group's subject was deleted.");
            UI.getCurrent().navigate(MasterDetailViewList.class);
        });

        deleteDate.addClickListener(e -> {
            if (this.gradeDate == null) {
                return;
            }

            teacherController.deleteGradeDate(this.gradeDate.getId());
            clearForm();
            refreshGrid();
            Notification.show("Date was deleted.");
            UI.getCurrent().navigate(MasterDetailViewList.class);
        });

        addDate.addClickListener(e ->
//                splitLayout
            createEditorLayoutDate(splitLayout)
        );

        saveDate.addClickListener(e -> {
            try {
//                if (this.gradeDate == null) {
//                    Notification.show("Nothing happend.");
//                    return;
//                }
                binderGradeDate.writeBean(this.gradeDate);

                teacherController.updateGradeDate(this.gradeDate);

                clearForm();
                refreshGrid();
                Notification.show("Date stored.");
                UI.getCurrent().navigate(MasterDetailViewList.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store date.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> listId = event.getRouteParameters().getLong(listHasStudentsID);
        if (listId.isPresent()) {
            Optional<ListHasStudents> listFromBackend = moderatorController.getListHasStudent(listId.get());
            if (listFromBackend.isPresent()) {
                populateFormList(listFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested list was not found, ID = %d", listId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewList.class);
            }
        } else {
            listId = event.getRouteParameters().getLong(dateID);
            if (listId.isPresent()) {
                Optional<GroupsSubjects> groupsSubjectsFromBackend = moderatorController.getGroupsSubject(listId.get());
                if (groupsSubjectsFromBackend.isPresent()) {
                    populateFormDate(groupsSubjectsFromBackend.get());
                } else {
                    Notification.show(
                            String.format("The requested group's subject was not found, ID = %d", listId.get()), 3000,
                            Notification.Position.BOTTOM_START);
                    // when a row is selected but the data is no longer available,
                    // refresh grid
                    refreshGrid();
                    event.forwardTo(MasterDetailViewList.class);
                }
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

        checkboxA1 = new Checkbox("First Attestation");
        binderList.forField(checkboxA1)
                .bind(ListHasStudents::getAttest1, ListHasStudents::setAttest1);
        checkboxA2 = new Checkbox("Second Attestation");
        binderList.forField(checkboxA2)
                .bind(ListHasStudents::getAttest2, ListHasStudents::setAttest2);
//        teacherComboBox = new ComboBox<>("Teacher");
//        teacherComboBox.setItems(moderatorController.getAllTeachers());
//        teacherComboBox.setItemLabelGenerator(Teacher::getName);
//
//        binder.forField(teacherComboBox)
//                .bind(GroupsSubjects::getTeacher, GroupsSubjects::setTeacher);
//
//        subjectComboBox = new ComboBox<>("Subject");
//        subjectComboBox.setItems(moderatorController.getAllSubjects());
//        subjectComboBox.setItemLabelGenerator(Subject::getName);
//
//        binder.forField(subjectComboBox)
//                .bind(GroupsSubjects::getSubject, GroupsSubjects::setSubject);
//
//        groupComboBox = new ComboBox<>("Group");
//        groupComboBox.setItems(moderatorController.getAllGroups());
//        groupComboBox.setItemLabelGenerator(Group::getName);
//
//        binder.forField(groupComboBox)
//                .bind(GroupsSubjects::getGroup, GroupsSubjects::setGroup);

        Component[] fields = new Component[]{checkboxA1, checkboxA2};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createEditorLayoutDate(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        dateOfGrade = new DatePicker("Date Of Grade");
        binderGradeDate.forField(dateOfGrade)
                .bind(e -> e.getDate() == null ?
                                LocalDate.now() :
                                new java.sql.Date(e.getDate().getTime()).toLocalDate(),
                        (e, d) -> e.setDate(java.sql.Date.valueOf(d)));

        Component[] fields = new Component[]{dateOfGrade};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayoutDates(editorLayoutDiv);

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

    private void createButtonLayoutDates(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveDate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteDate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(deleteDate, saveDate, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(groupsSubjectsGrid);
    }

    private void refreshGrid() {
        groupsSubjectsGrid.select(null);
        groupsSubjectsGrid.getDataProvider().refreshAll();

    }

    private void clearForm() {
        populateFormDate(null);
        populateFormList(null);
    }

    private void populateFormList(ListHasStudents value) {
        this.listHasStudents = value;
        binderList.readBean(this.listHasStudents);
    }

    private void populateFormDate(GroupsSubjects value) {
        this.groupsSubjects = value;
        this.gradeDate = new GradeDate();
        this.gradeDate.setGroupsSubjects(groupsSubjects);
        binderGroupsSubjects.readBean(this.groupsSubjects);
        binderGradeDate.readBean(this.gradeDate);
    }
}
