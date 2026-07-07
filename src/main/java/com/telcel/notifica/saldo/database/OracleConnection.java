package com.telcel.notifica.saldo.database;

import com.telcel.notifica.saldo.utils.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnection {

    private OracleConnection() {
    }

    public static Connection getConnection() throws Exception {

        Class.forName(ConfigReader.get("db.oracle.driver"));

        String url = ConfigReader.get("db.oracle.url");
        String user = ConfigReader.get("db.oracle.user");
        String password = ConfigReader.get("db.oracle.password");

        if (url == null || url.isBlank()) {
            throw new IllegalStateException("No se configuró 'db.oracle.url'");
        }
        if (user == null || user.isBlank()) {
            throw new IllegalStateException("No se configuró 'db.oracle.user'");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("No se configuró 'db.oracle.password'");
        }
        return DriverManager.getConnection(url, user, password);
    }

}