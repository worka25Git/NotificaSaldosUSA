package com.telcel.notifica.saldo.sql;

import com.telcel.notifica.saldo.utils.ConfigReader;

public final class OracleSQL {

    private static final String SCHEMA =
            ConfigReader.get("db.schema");

    private OracleSQL() {
    }


    public static final String CONSULTA_SALDOS =
            "SELECT CL.NOMBRE AS CADENA, FC.MONTO_RESTA AS SALDO " +
                    "FROM " + SCHEMA + ".CLIENTE CL, " +
                    SCHEMA + ".V_SUM_BOLSA_FACTURA FC " +
                    "WHERE CL.ID_CLIENTE = FC.ID_BOLSA";

}