package com.ap4b.pathplanner.controller;

import com.ap4b.pathplanner.view.MenuFX;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * Controls the menu interactions in the Path Planner application.
 */
public class MenuController implements EventHandler<ActionEvent> {

    private MenuFX menu;

    public MenuController(MenuFX menu) {
        this.menu = menu;
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
        }
    }

    private void handleExportItinerary() {
        System.out.println("Exporting Itinerary...");
        // Add logic to export itinerary
    }

    private void handleChangeMap() {
        System.out.println("Changing Map...");
        // Add logic to change map
    }

    private void handleAbout() {
        System.out.println("Showing About Dialog...");
        // Add logic to show about dialog
    }

    private void handleExit() {
        System.out.println("Exiting Application...");
        System.exit(0);
    }
}