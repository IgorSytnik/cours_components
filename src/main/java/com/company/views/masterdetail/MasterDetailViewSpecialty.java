package com.company.views.masterdetail;

import com.company.MyCrudServiceDataProvider;
import com.company.controllers.ModeratorController;
import com.company.domain.inanimate.Specialty;
import com.company.services.interfaces.inanimate.SpecialtyService;
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

@Route(value = "master-detail-specialty/:specialtyID?/:action?(edit)", layout = MainView.class)
@PageTitle("Master-Detail-Specialty")
public class MasterDetailViewSpecialty extends Div implements BeforeEnterObserver {

    private final String SPECIALTY_ID = "specialtyID";
    private final String SPECIALTY_EDIT_ROUTE_TEMPLATE = "master-detail-specialty/%d/edit";

    private Grid<Specialty> grid = new Grid<>(Specialty.class, false);

    private TextField name;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Specialty> binder;

    private Specialty specialty;

    private ModeratorController moderatorController;

    public MasterDetailViewSpecialty(
            @Autowired ModeratorController moderatorController
    ) {
        addClassNames("master-detail-view", "flex", "flex-col", "h-full");
        this.moderatorController = moderatorController;
        // Configure Form
        binder = new BeanValidationBinder<>(Specialty.class);
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.setDataProvider(new MyCrudServiceDataProvider<>(moderatorController.getSpecialtyService()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SPECIALTY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterDetailViewSpecialty.class);
            }
        });

        // Configure Form
//        binder = new BeanValidationBinder<>(Specialty.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.specialty == null) {
                    this.specialty = new Specialty();
                }
                binder.writeBean(this.specialty);

                moderatorController.updateSpecialty(this.specialty);
                clearForm();
                refreshGrid();
                Notification.show("Specialty details stored.");
                UI.getCurrent().navigate(MasterDetailViewSpecialty.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the specialty details.");
            }
        });

        delete.addClickListener(e -> {
            if (this.specialty == null) {
                return;
            }

            moderatorController.deleteSpecialty(this.specialty.getId());
            clearForm();
            refreshGrid();
            Notification.show("Specialty was deleted.");
            UI.getCurrent().navigate(MasterDetailViewSpecialty.class);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> specialtyId = event.getRouteParameters().getLong(SPECIALTY_ID);
        if (specialtyId.isPresent()) {
            Optional<Specialty> specialtyFromBackend = moderatorController.getSpecialty(specialtyId.get());
            if (specialtyFromBackend.isPresent()) {
                populateForm(specialtyFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested specialty was not found, ID = %d", specialtyId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterDetailViewSpecialty.class);
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

    private void populateForm(Specialty value) {
        this.specialty = value;
        binder.readBean(this.specialty);

    }
}
