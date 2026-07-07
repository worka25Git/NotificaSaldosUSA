package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.utils.ConfigReader;

public final class ProviderFactory {

    private ProviderFactory() {
    }

    public static DatabaseProvider getProvider() {

        String motor = ConfigReader.get("db.tipo");

        switch (motor.toLowerCase()) {

            case "oracle":
                return new OracleProvider();

            case "informix":
                return new InformixProvider();

            default:
                throw new IllegalArgumentException(
                        "Motor de base de datos no soportado: " + motor);
        }
    }

}