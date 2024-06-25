package com.ap4b.pathplanner.view;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * The Class MenuBar - manages the application menu bar in JavaFX.
 */
public class MenuFX extends MenuBar {

    private final MenuItem fileExportItinerary;
    private final MenuItem fileChangeMap;
    private final MenuItem about;
    private final MenuItem exit;
    private final MenuItem theme;

    /**
     * Instantiates a new menu bar.
     */
    public MenuFX() {
        // Initialize Menus
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("?");

        // Initialize Menu Items
        fileExportItinerary = new MenuItem("Export Itinerary");
        fileChangeMap = new MenuItem("Change Map");
        exit = new MenuItem("Exit");
        theme = new MenuItem("Switch Theme");

        // Add items to File Menu
        fileMenu.getItems().addAll(fileExportItinerary, fileChangeMap, exit, theme);

        // Initialize and set Help Menu Items
        MenuItem controlsItem = new MenuItem("Controls");
        about = new MenuItem("About");
        helpMenu.getItems().addAll(controlsItem, about);

        // Add all menus to the menu bar
        this.getMenus().addAll(fileMenu, helpMenu);
    }

    /**
     * Gets the export item.
     *
     * @return the export item
     */
    public MenuItem getExportItem() {
        return fileExportItinerary;
    }

    /**
     * Gets the menu items.
     *
     * @return the menu items as an array
     */
    public MenuItem[] getMenuItems() {
        return new MenuItem[]{fileExportItinerary, fileChangeMap, about, exit,theme};
    }

    public MenuItem getChangeMapItem() {
        return fileChangeMap;
    }

    public MenuItem getAboutItem() {
        return about;
    }

    public MenuItem getExitItem() {
        return exit;
    }

    public MenuItem getThemeItem() {return theme;}
}
