package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.inanimate.subject.Subject;
import com.company.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

@Route(value = "master-detail-subject/:subjectID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Subject")
public class MasterDetailViewSubject extends Div implements BeforeEnterObserver {

    private final String SUBJECT_ID = "subjectID";
    private final String SUBJECT_EDIT_ROUTE_TEMPLATE = "master-detail-subject/%d/edit";

    private Grid<Subject> grid = new Grid<>(Subject.class, false);

    private TextField name;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Subject> binder;

    private Subject subject;

    private ModeratorController moderatorController;

    public MasterDetailViewSubject(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form: initiating binder here for MasterDetailViewDepartment::createEditorLayout
        binder = new BeanValidationBinder<>(Subject.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);

        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getSubjectService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SUBJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewSubject.class);
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
                if (this.subject == null) {
                    this.subject = new Subject();
                }
                binder.writeBean(this.subject);

                moderatorController.updateSubject(this.subject);
                clearForm();
                refreshGrid();
                Notification.show("Specialty details stored.");
                UI.getCurrent().navigate(MasterDetailViewSubject.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the specialty details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.subject == null) {
                return;
            }

            moderatorController.deleteSubject(this.subject.getId());
            clearForm();
            refreshGrid();
            Notification.show("Subject was deleted.");
            UI.getCurrent().navigate(MasterDetailViewSubject.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> subjectId = event.getRouteParameters().getLong(SUBJECT_ID);
        if (subjectId.isPresent()) {
            Optional<Subject> subjectFromBackend = moderatorController.getSubject(subjectId.get());
            if (subjectFromBackend.isPresent()) {
                populateForm(subjectFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested subject was not found, ID = %d", subjectId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewSubject.class);
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

    private void populateForm(Subject value) {
        this.subject = value;
        binder.readBean(this.subject);

    }
}
