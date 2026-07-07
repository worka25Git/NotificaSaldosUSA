package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.database.OracleConnection;
import com.telcel.notifica.saldo.mapper.FacturaMapper;
import com.telcel.notifica.saldo.mapper.OracleFacturaMapper;
import com.telcel.notifica.saldo.sql.OracleSQL;

import java.sql.Connection;

public class OracleProvider implements DatabaseProvider {

    @Override
    public Connection getConnection() throws Exception {
        return OracleConnection.getConnection();
    }

    @Override
    public String getConsultarFacturas() {
        return OracleSQL.CONSULTA_FACTURAS;
    }

    @Override
    public String getActualizarFactura() {
        return OracleSQL.ACTUALIZA_FACTURA;
    }

    @Override
    public FacturaMapper getFacturaMapper() {
        return new OracleFacturaMapper();
    }

}