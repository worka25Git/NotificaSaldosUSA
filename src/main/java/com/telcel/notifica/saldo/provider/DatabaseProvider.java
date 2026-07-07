package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.mapper.FacturaMapper;

import java.sql.Connection;

public interface DatabaseProvider {

    Connection getConnection() throws Exception;

    String getConsultarFacturas();

    String getActualizarFactura();

    FacturaMapper getFacturaMapper();

}
