package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.PathPlannerApp;
import com.ap4b.pathplanner.model.Application;
import com.ap4b.pathplanner.view.AppWindow;
import com.ap4b.pathplanner.view.FileSelectionWindow;
import com.ap4b.pathplanner.view.MenuFX;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controls the menu interactions in the Path Planner application.
 */
public class MenuController implements EventHandler<ActionEvent> {

    private MenuFX menu;
    private Application app;
    private AppWindow appWindow;

    public MenuController(MenuFX menu, Application app, AppWindow appWindow) {
        this.menu = menu;
        this.app = app;
        this.appWindow = appWindow;
        attachHandlers();
    }

    private void attachHandlers() {
        // Attach event handlers to each menu item
        menu.getExportItem().setOnAction(this);
        for (MenuItem item : menu.getMenuItems()) {
            item.setOnAction(this);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == menu.getExportItem()) {
            handleExportItinerary();
        } else if (source == menu.getChangeMapItem()) {
            handleChangeMap();
        } else if (source == menu.getAboutItem()) {
            handleAbout();
        } else if (source == menu.getExitItem()) {
            handleExit();
        } else if (source == menu.getThemeItem()) {
            handleSwitchTheme();
        }
    }

    private void handleExportItinerary() {
        System.out.println("Exporting Itinerary...");
        // Add logic to export itinerary
    }

    private void handleChangeMap() {
        app.handleMapChange();
    }

    private void handleAbout() {
        System.out.println("Showing About Dialog...");
        app.about();
    }

    private void handleExit() {
        System.out.println("Exiting Application...");
        System.exit(0);
    }

    private void handleSwitchTheme(){
        System.out.println("Switching Theme...");
        app.switchTheme();
    }
}