package com.company.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.company.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "about", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Higher Educational Institution")
public class AboutView extends Div {

    public AboutView() {
        addClassName("about-view");
        add(new Text(""));
    }

}
