package com.telcel.notifica.saldo.mapper;

import com.telcel.notifica.saldo.dao.Saldos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleFacturaMapper implements FacturaMapper {

    @Override
    public Saldos map(ResultSet rs) throws SQLException {

        return new Saldos(
               );

    }

}