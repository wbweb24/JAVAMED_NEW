package com.mycompany.javamed_new.errors;

import java.sql.SQLException;

public class ErrorsHandler {
    public static void handle(Throwable e) {
        System.out.println("[🔥 ERROR DETECTADO] " + e.getMessage());

        if (e instanceof SQLException) {
            System.out.println("[⚠️ ERROR DE BASE DE DATOS] Verifica credenciales o disponibilidad del servidor.");
        } else if (e instanceof IllegalArgumentException) {
            System.out.println("[⚠️ ERROR DE VALIDACIÓN] Parámetros inválidos en la solicitud.");
        } else if (e instanceof NullPointerException) {
            System.out.println("[❌ ERROR DE DATOS] Intento de acceso a valor nulo.");
        } else {
            System.out.println("[🚨 ERROR GENERAL] Revisa la aplicación.");
        }

        e.printStackTrace(); // Para debugging
    }

    public static void setGlobalHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("[⚠️ ERROR NO MANEJADO] en hilo " + thread.getName());
            handle(throwable);
        });
    }
}
