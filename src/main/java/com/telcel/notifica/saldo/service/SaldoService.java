package com.telcel.notifica.saldo.service;

import com.telcel.notifica.saldo.dao.ConsultasDAO;
import com.telcel.notifica.saldo.dao.Saldos;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class SaldoService {

    private final ConsultasDAO dao = new ConsultasDAO();

    public List<Saldos> obtenerSaldos() throws SQLException {
        return dao.checaSaldo();
    }

    public void actualizarSaldos(Set<Saldos> saldos) throws SQLException {

        // Pendiente.
        // Aquí implementaremos la actualización de BD
        // cuando terminemos EnviaNotificacionUSA.

    }

}