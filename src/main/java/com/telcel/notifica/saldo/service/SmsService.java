package com.telcel.notifica.saldo.service;

import clientealarma.ClienteALARMA;
import com.telcel.notifica.saldo.dao.Saldos;

import java.util.List;

public class SmsService {

    public boolean enviar(Saldos saldo,
                          List<Saldos> contactos) {

        boolean enviado = false;

        try {

            for (Saldos contacto : contactos) {

                if (contacto.getTelefono() == null ||
                        contacto.getTelefono().trim().isEmpty()) {
                    continue;
                }

                String mensaje =
                        "El saldo de su bolsa prepagada para "
                                + saldo.getCliente()
                                + " es:$"
                                + saldo.getMonto()
                                + " Dls.";

                ClienteALARMA cliente = new ClienteALARMA();

                String respuesta =
                        cliente.enviarSMSConCurl(
                                contacto.getTelefono(),
                                mensaje);

                System.out.println(
                        "SMS -> "
                                + contacto.getTelefono()
                                + " Respuesta="
                                + respuesta);

                if ("0".equals(respuesta)) {
                    enviado = true;
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return enviado;

    }

}