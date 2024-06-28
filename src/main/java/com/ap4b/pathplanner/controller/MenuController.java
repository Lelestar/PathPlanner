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
        for (MenuItem item : menu.getMenuItems()) {
            item.setOnAction(this);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == menu.getChangeMapItem()) {
            handleChangeMap();
        } else if (source == menu.getAboutItem()) {
            handleAbout();
        } else if (source == menu.getControlsItem()) {
            handleControls();
        } else if (source == menu.getExitItem()) {
            handleExit();
        } else if (source == menu.getThemeItem()) {
            handleSwitchTheme();
        } else if (source == menu.getModeItem()) {
            handleSwitchMode();
        }
    }

    private void handleChangeMap() {
        app.handleMapChange();
    }

    private void handleAbout() {
        System.out.println("Showing About Dialog...");
        app.about();
    }

    public void handleControls() {
        System.out.println("Showing Controls Dialog...");
        app.controls();
    }

    private void handleExit() {
        System.out.println("Exiting Application...");
        System.exit(0);
    }

    private void handleSwitchTheme(){
        System.out.println("Switching Theme...");
        app.switchTheme();
    }

    private void handleSwitchMode(){
        System.out.println("Switching Mode...");
        app.switchMode();
        appWindow.getMap().switchEditMode(app.getRoadNetwork());
        if (app.isModeIsEdition()){
            menu.switchModeItem("Use mode");
        } else {
            menu.switchModeItem("Edit mode");
        }
    }
}