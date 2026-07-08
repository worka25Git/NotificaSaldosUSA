package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.mapper.SaldosMapper;

import java.sql.Connection;

public interface DatabaseProvider {

    Connection getConnection() throws Exception;

    String getConsutarSaldo();

    SaldosMapper getSaldosMapper();

}
