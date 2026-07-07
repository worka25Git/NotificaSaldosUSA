package com.telcel.notifica.saldo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis =
                     new FileInputStream("conf/Configuracion.conf")) {
            properties.load(fis);
            System.out.println("Configuración cargada correctamente.");

        } catch (IOException ex) {
            throw new RuntimeException(
                    "No fue posible cargar conf/Configuracion.conf",
                    ex);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void load(String s) {
    }
}