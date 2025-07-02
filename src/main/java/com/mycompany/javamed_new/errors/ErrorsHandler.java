package com.mycompany.javamed_new.errors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.logging.*;

public class ErrorsHandler {

    private static final boolean IS_PRODUCTION = "production".equalsIgnoreCase(System.getenv("APP_ENV"));
    private static final boolean LOG_TO_FILE = !IS_PRODUCTION;

    private static final Logger logger = Logger.getLogger(ErrorsHandler.class.getName());

    private static final Map<String, String> mensajes = Map.ofEntries(
        // Autenticación
        Map.entry("login.success",         "✅ Login exitoso: %s"),
        Map.entry("login.failure",         "❌ Login fallido: %s"),
        Map.entry("login.empty.fields",    "⚠️ Usuario o contraseña vacíos."),
        Map.entry("login.failure.generic", "❌ Fallo de autenticación genérico"),

        // App y vistas
        Map.entry("app.dependencies.injected", "⚙️ Dependencias inyectadas."),
        Map.entry("view.primary.loaded",       "🖥️ Vista principal cargada correctamente."),
        Map.entry("css.loaded",                "🎨 CSS aplicado."),
        Map.entry("css.not.found",             "❌ CSS no encontrado."),
        Map.entry("scene.null",                "⚠️ Escena o BorderPane nulo."),
        Map.entry("scene.secondary.error",     "❌ Error al cambiar de vista."),

        // Base de datos
        Map.entry("db.connected",        "🔌 Conexión establecida a %s"),
        Map.entry("db.schema.updated",   "🗂️ Esquema de base de datos actualizado."),
        Map.entry("db.insert.success",   "✅ Datos insertados en tabla %s"),
        Map.entry("db.insert.error",     "❌ Error al insertar en tabla %s: %s")
    );

    static {
        if (LOG_TO_FILE) {
            try {
                FileHandler handler = new FileHandler("javamed.log", 1024 * 1024, 5, true);
                handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);
                logger.setUseParentHandlers(false);
            } catch (IOException e) {
                System.err.println("⚠️ No se pudo crear el archivo de log.");
            }
        }
    }

    public static void log(String clave, Object... datos) {
        log(Level.INFO, clave, datos);
    }

    public static void log(Level nivel, String clave, Object... datos) {
        String plantilla = mensajes.getOrDefault(clave, "🟡 Evento indefinido: " + clave);
        Object[] procesados = new Object[datos.length];
        for (int i = 0; i < datos.length; i++) {
            procesados[i] = IS_PRODUCTION ? sanitizar(datos[i]) : datos[i];
        }
        String mensajeFinal = String.format(plantilla, procesados);
        logger.log(nivel, mensajeFinal);
    }

    public static void handle(Throwable e) {
        handle(e, false);
    }

    public static void handle(Throwable e, boolean mostrarDetalles) {
        String resumen = "🚨 Excepción: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        logger.severe(resumen);
        if (!IS_PRODUCTION || mostrarDetalles) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        }
    }

    public static void setGlobalHandler() {
        Thread.setDefaultUncaughtExceptionHandler((hilo, ex) -> {
            logger.severe("⚠️ ERROR global en hilo: " + hilo.getName());
            handle(ex);
        });
    }

    private static String sanitizar(Object dato) {
        if (dato == null) return "[nulo]";
        String s = dato.toString().toLowerCase();

        if (s.matches(".*@.*\\..*")) return "[email]";
        if (s.matches("(\\d{1,3}\\.){3}\\d{1,3}")) return "[ip]";
        if (esUsuario(s)) return anonymize(s);

        return "[dato]";
    }

    private static boolean esUsuario(String s) {
        return s.matches("[a-zA-Z0-9._-]{3,}");
    }

    private static String anonymize(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return "user_" + bytesToHex(hash).substring(0, 8);
        } catch (Exception e) {
            return "user_anon";
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
