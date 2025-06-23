package com.mycompany.javamed_new.services.sessioncontext;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import java.util.List;

public class WorkAreaView extends GridPane {

    public WorkAreaView() {
        setHgap(15);
        setVgap(15);
        setPadding(new Insets(20));
        getStyleClass().add("workarea-view"); // 💅 CSS opcional para estilos comunes
    }

    /**
     * Inserta contenido visual en la cuadrícula con una cantidad fija de columnas.
     *
     * @param elementos Lista de nodos que se desean mostrar.
     * @param columnas Número de columnas a usar (por defecto podrías usar 2 o 3).
     */
    public void setContent(List<Node> elementos, int columnas) {
        getChildren().clear();
        for (int i = 0; i < elementos.size(); i++) {
            int col = i % columnas;
            int row = i / columnas;
            add(elementos.get(i), col, row);
        }
    }

    /**
     * Variante rápida con una sola columna.
     * @param elementos Lista de nodos a mostrar en vertical.
     */
    public void setVerticalContent(List<Node> elementos) {
        setContent(elementos, 1);
    }

    /**
     * Variante para mostrar un solo nodo centrado.
     * @param nodo Elemento único a mostrar.
     */
    public void setCenteredContent(Node nodo) {
        getChildren().clear();
        add(nodo, 0, 0);
    }
}
