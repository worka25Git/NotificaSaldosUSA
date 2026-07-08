package com.telcel.notifica.saldo.provider;

import com.telcel.notifica.saldo.database.InformixConnection;
import com.telcel.notifica.saldo.mapper.SaldosMapper;
import com.telcel.notifica.saldo.mapper.InformixSaldoMapper;
import com.telcel.notifica.saldo.sql.InformixSQL;

import java.sql.Connection;

public class InformixProvider implements DatabaseProvider {

    @Override
    public Connection getConnection() throws Exception {
        return InformixConnection.getConnection();
    }

    @Override
    public String getConsutarSaldo() {
        return InformixSQL.CONSULTA_SALDOS;
    }

    @Override
    public SaldosMapper getSaldosMapper() {
        return new InformixSaldoMapper();
    }

}