package com.ap4b.pathplanner.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * The Class PanelView in JavaFX.
 */
public class PanelView extends ScrollPane {

    private Map map;

    /**
     * Instantiates a new panel view.
     *
     * @param map the map
     */
    public PanelView(Map map) {
        super(map);  // Assurez-vous que Map étend quelque chose de visualisable, comme Pane.
        this.map = map;
        setPannable(true);
        setFitToWidth(true);  // Assurez-vous que la largeur du contenu correspond à celle du viewport.
        setFitToHeight(true);  // Assurez-vous que la hauteur du contenu correspond à celle du viewport.
        setContent(map);  // Définit le contenu du ScrollPane.
        //getStylesheets().add("path/to/your/stylesheet.css"); // Optionnel pour les styles.
    }

    /**
     * Gets the map.
     *
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Move map.
     *
     * @param x the x offset
     * @param y the y offset
     */
    public void moveMap(int x, int y) {
        double hValue = getHvalue() + x;  // Calcule la nouvelle valeur horizontale.
        double vValue = getVvalue() + y;  // Calcule la nouvelle valeur verticale.
        setHvalue(Math.max(0, Math.min(hValue, 1)));  // Assurez-vous que la valeur reste dans les limites.
        setVvalue(Math.max(0, Math.min(vValue, 1)));  // Assurez-vous que la valeur reste dans les limites.
    }
}
