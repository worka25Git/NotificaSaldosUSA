package com.telcel.notifica.saldo.service;

import clientealarma.ClienteALARMA;
import com.telcel.notifica.saldo.dao.Saldos;
import com.telcel.notifica.saldo.utils.ConfigReader;

import java.util.List;
import java.util.logging.Logger;

public class SmsService {

    private static final Logger logger =
            Logger.getLogger(SmsService.class.getName());

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

                String url = ConfigReader.get("sms.url");
                logger.info("URL conf: " + url);
                if (url.isBlank()) {
                    url = "http://intranet.telcel.com:9046/despachador-sms-ws/mensaje";
                    logger.info("URL: " + url);
                }

                ClienteALARMA cliente = new ClienteALARMA(url);


                String respuesta =
                        cliente.enviarSMSConCurl(
                                contacto.getTelefono(),
                                mensaje);

                logger.info(
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