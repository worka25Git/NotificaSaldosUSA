package com.telcel.notifica.saldo.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestConsultaSaldoUSA {
  public static void main(String[] args) throws SQLException {
    ConsultasDAO cd = new ConsultasDAO();
    List<Saldos> saldos= cd.checaSaldo();
  }
}
