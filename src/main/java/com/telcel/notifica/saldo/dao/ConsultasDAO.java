package com.telcel.notifica.saldo.dao;

import com.telcel.notifica.saldo.mapper.SaldosMapper;
import com.telcel.notifica.saldo.provider.DatabaseProvider;
import com.telcel.notifica.saldo.provider.ProviderFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConsultasDAO {

    private static final Logger logger =
            Logger.getLogger(ConsultasDAO.class.getName());

    private final DatabaseProvider provider = ProviderFactory.getProvider();

    private Connection con;


    public List<Saldos> checaSaldo() throws SQLException {

        List<Saldos> saldos = new ArrayList<>();

        String sql = provider.getConsutarSaldo();

        logger.info("sql: " + sql);

        try (Connection con = provider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql);
        ) {
            SaldosMapper mapper = provider.getSaldosMapper();

            while (rs.next()) {
                /*
                System.out.println(
                        rs.getString("CADENA")
                                + " -> "
                                + rs.getLong("SALDO"));

                 */

                saldos.add(mapper.map(rs));
            }

            logger.info("[BD] Total de saldos: " + saldos.size());

            return saldos;

        } catch (Exception e) {

            throw new SQLException("Error consultando saldos.", e);

        }

    }

}