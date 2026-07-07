package com.telcel.notifica.saldo.dao;

import com.telcel.notifica.saldo.provider.OracleProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultasDAO {

  public List<Saldos> checaSaldo() throws SQLException {

    List<Saldos> saldos = new ArrayList<>();
/*
    String sql =
            "SELECT CL.NOMBRE AS CADENA, " +
                    "FC.MONTO_RESTA AS SALDO " +
                    "FROM VI0ADM01.CLIENTE CL, " +
                    "VI0ADM01.V_SUM_BOLSA_FACTURA FC " +
                    "WHERE CL.ID_CLIENTE = FC.ID_BOLSA";
*/

    String sql =
            "SELECT C.NOMBRE AS CADENA, " +
                    "       L.MONTO_COMPRA AS SALDO " +
                    "FROM CLIENTE C " +
                    "INNER JOIN LOG_FAC_SAP L ON C.RFC = L.RFC";

    System.out.println("sql " + sql);

    OracleProvider provider = new OracleProvider();

    try (Connection con = provider.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {

        saldos.add(
                new Saldos(
                        rs.getString("CADENA"),
                        rs.getLong("SALDO")));

      }

      System.out.println("[BD] Total de saldos: " + saldos.size());

    } catch (Exception e) {

      throw new SQLException(e);

    }

    return saldos;

  }

}