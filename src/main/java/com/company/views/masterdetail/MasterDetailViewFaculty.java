package com.company.views.masterdetail;

import java.util.Optional;

import com.company.MyCrudServiceDataProvider;

import com.company.controllers.ModeratorController;
import com.company.domain.hei.Faculty;
import com.company.services.interfaces.hei.FacultyService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.company.views.main.MainView;
import com.vaadin.flow.component.textfield.TextField;

//@Route(value = "master-detail/:samplePersonID?/:action?(edit)", layout = MainView.class)
@Route(value = "master-detail-faculty/:facultyID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Faculty")
public class MasterDetailViewFaculty extends Div implements BeforeEnterObserver {

//    private final String SAMPLEPERSON_ID = "samplePersonID";
//    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "master-detail/%d/edit";
    private final String FACULTY_ID = "facultyID";
    private final String FACULTY_EDIT_ROUTE_TEMPLATE = "master-detail-faculty/%d/edit";

//    private Grid<SamplePerson> grid = new Grid<>(SamplePerson.class, false);
    private Grid<Faculty> grid = new Grid<>(Faculty.class, false);

    private TextField name;
//    private TextField firstName;
//    private TextField lastName;
//    private TextField email;
//    private TextField phone;
//    private DatePicker dateOfBirth;
//    private TextField occupation;
//    private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

//    private BeanValidationBinder<SamplePerson> binder;
    private BeanValidationBinder<Faculty> binder;

//    private SamplePerson samplePerson;
    private Faculty faculty;

//    private SamplePersonService samplePersonService;

    private ModeratorController moderatorController;

    public MasterDetailViewFaculty(
//            @Autowired SamplePersonService samplePersonService
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
//        grid.addColumn("lastName").setAutoWidth(true);
//        grid.addColumn("email").setAutoWidth(true);
//        grid.addColumn("phone").setAutoWidth(true);
//        grid.addColumn("dateOfBirth").setAutoWidth(true);
//        grid.addColumn("occupation").setAutoWidth(true);
//        TemplateRenderer<Faculty> importantRenderer = TemplateRenderer.<Faculty>of(
//                "<iron-icon hidden='[[!item.important]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.important]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
////                .withProperty("important", SamplePerson::isImportant)
//                ;
//        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);

//        grid.setDataProvider(new CrudServiceDataProvider<>(samplePersonService));

//        grid.setDataProvider(new CrudServiceDataProvider<FacultyServiceImpl2>(facultyService));
        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getFacultyService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FACULTY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewFaculty.class);
            }
        });

        // Configure Form
//        binder = new BeanValidationBinder<>(SamplePerson.class);
        binder = new BeanValidationBinder<>(Faculty.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
//            try {
//                if (this.samplePerson == null) {
//                    this.samplePerson = new SamplePerson();
//                }
//                binder.writeBean(this.samplePerson);
//
//                samplePersonService.update(this.samplePerson);
//                clearForm();
//                refreshGrid();
//                Notification.show("SamplePerson details stored.");
//                UI.getCurrent().navigate(MasterDetailView.class);
//            } catch (ValidationException validationException) {
//                Notification.show("An exception happened while trying to store the samplePerson details.");
//            }
            try {
                if (this.faculty == null) {
                    this.faculty = new Faculty();
                }
                binder.writeBean(this.faculty);

//                facultyService.update(this.faculty);
                moderatorController.updateFaculty(this.faculty);
                clearForm();
                refreshGrid();
                Notification.show("Faculty details stored.");
                UI.getCurrent().navigate(MasterDetailViewFaculty.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the faculty details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.faculty == null) {
                return;
            }

            moderatorController.deleteFaculty(this.faculty.getId());
            clearForm();
            refreshGrid();
            Notification.show("Faculty was deleted.");
            UI.getCurrent().navigate(MasterDetailViewFaculty.class);
        });

    }

//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(FACULTY_ID);
//        if (samplePersonId.isPresent()) {
//            Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
//            if (samplePersonFromBackend.isPresent()) {
//                populateForm(samplePersonFromBackend.get());
//            } else {
//                Notification.show(
//                        String.format("The requested samplePerson was not found, ID = %d", samplePersonId.get()), 3000,
//                        Notification.Position.BOTTOM_START);
//                // when a row is selected but the data is no longer available,
//                // refresh grid
//                refreshGrid();
//                event.forwardTo(MasterDetailView.class);
//            }
//        }
//    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> facultyId = event.getRouteParameters().getLong(FACULTY_ID);
        if (facultyId.isPresent()) {
            Optional<Faculty> facultyFromBackend = moderatorController.getFaculty(facultyId.get());
            if (facultyFromBackend.isPresent()) {
                populateForm(facultyFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested faculty was not found, ID = %d", facultyId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewFaculty.class);
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
        name = new TextField("Name");
//        firstName = new TextField("First Name");
//        lastName = new TextField("Last Name");
//        email = new TextField("Email");
//        phone = new TextField("Phone");
//        dateOfBirth = new DatePicker("Date Of Birth");
//        occupation = new TextField("Occupation");
//        important = new Checkbox("Important");
//        important.getStyle().set("padding-top", "var(--lumo-space-m)");
//        Component[] fields = new Component[]{firstName, lastName, email, phone, dateOfBirth, occupation, important};
        Component[] fields = new Component[]{name};

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

    private void populateForm(Faculty value) {
        this.faculty = value;
        binder.readBean(this.faculty);

    }
}
