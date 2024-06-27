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
    private final MenuItem controlsItem;
    private final MenuItem mode;
    private Menu fileMenu;
    private Menu helpMenu;

    /**
     * Instantiates a new menu bar.
     */
    public MenuFX() {
        // Initialize Menus
        fileMenu = new Menu("File");
        helpMenu = new Menu("?");

        // Initialize Menu Items
        fileExportItinerary = new MenuItem("Export Itinerary");
        fileChangeMap = new MenuItem("Change Map");
        exit = new MenuItem("Exit");
        theme = new MenuItem("Switch Theme");
        mode = new MenuItem("Mode Edition");

        // Add items to File Menu
        fileMenu.getItems().addAll(fileExportItinerary, fileChangeMap, theme,mode, exit);

        // Initialize and set Help Menu Items
        controlsItem = new MenuItem("Controls");
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
        return new MenuItem[]{fileExportItinerary, fileChangeMap, about, exit,theme,mode};
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
    public MenuItem getModeItem() {return mode;}
    public void switchModeItem(String name_mode) {
        mode.setText(name_mode);
    }
}
