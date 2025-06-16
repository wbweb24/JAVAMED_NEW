package com.mycompany.javamed_new.services.sessioncontext;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class WorkArea {
    private static final Map<String, Node[]> views = new HashMap<>();
    private static final Map<String, Node[]> subMenus = new HashMap<>();

    static {
        // 🔹 Definiendo vistas según el menú seleccionado
        views.put("Inicio", new Node[]{new VBox(new Button("Bienvenido"))});
        views.put("Agenda", new Node[]{new VBox(new Button("Ver citas"), new Button("Nueva cita"))});
        views.put("Clientes", new Node[]{new VBox(new Button("Lista de clientes"), new Button("Buscar cliente"))});
        views.put("Nuevo Usuario", new Node[]{new VBox(new Button("Registrar usuario"))});

        // 🔹 Definiendo submenús según el menú principal
        subMenus.put("Agenda", new Node[]{new Button("Ver agenda"), new Button("Añadir evento")});
        subMenus.put("Clientes", new Node[]{new Button("Buscar clientes"), new Button("Historial")});
        subMenus.put("Base de Datos", new Node[]{new Button("Nueva Alta"), new Button("Consultar")});
    }

    public static Node[] getViewForMenuOption(String menuOption) {
        return views.getOrDefault(menuOption, new Node[]{new VBox(new Button("Vista no encontrada"))});
    }

    public static Node[] getSubMenu(String mainMenuOption) {
        return subMenus.getOrDefault(mainMenuOption, new Node[]{new Button("Submenú no disponible")});
    }
}
