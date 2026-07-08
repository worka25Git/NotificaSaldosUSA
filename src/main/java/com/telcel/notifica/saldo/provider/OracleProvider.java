package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.database.OracleConnection;
import com.telcel.notifica.saldo.mapper.OracleSaldoMapper;
import com.telcel.notifica.saldo.mapper.SaldosMapper;
import com.telcel.notifica.saldo.sql.OracleSQL;

import java.sql.Connection;

public class OracleProvider implements DatabaseProvider {
    
    @Override
    public Connection getConnection() throws Exception {
        return OracleConnection.getConnection();
    }

    @Override
    public String getConsutarSaldo() {
        return OracleSQL.CONSULTA_SALDOS;
    }

    @Override
    public SaldosMapper getSaldosMapper() {
        return new OracleSaldoMapper();
    }
}