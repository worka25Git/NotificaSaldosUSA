package com.telcel.notifica.saldo.service;

import com.telcel.mail.EnviaMail;
import com.telcel.notifica.saldo.utils.ConfigReader;
import com.telcel.notifica.saldo.dao.Saldos;
import com.telcel.notifica.saldo.utils.GetUtil;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorreoService {

    private static final Logger logger = Logger.getLogger(CorreoService.class.getName());

    public boolean enviar(
            Saldos saldo,
            List<Saldos> contactos,
            int hora) {

        try {

            String correos = obtenerCorreos(contactos);

            if (correos.isEmpty()) {

                System.out.println(
                        "No hay correos para "
                                + saldo.getCliente());

                return false;
            }

            String saludo = obtenerSaludo(hora);

            String firma =
                    GetUtil.getFirmaComercioElectronico(
                            ConfigReader.get("firma.telefono1"),
                            ConfigReader.get("firma.extension"),
                            ConfigReader.get("firma.telefono2"));

            String mensaje = construirMensaje(
                            saldo.getCliente(),
                            saldo.getMonto(),
                            saludo);

            EnviaMail correo = new EnviaMail(
                            ConfigReader.get("correo.usuario"),
                            ConfigReader.get("correo.password"),
                            ConfigReader.get("correo.host"),
                    generarProperties()
            );

            correo.setFrom(ConfigReader.get("correo.remitente"));

            correo.setTo(correos);

            correo.setSubject("NOTIFICACION DE SALDO PARA "
                            + saldo.getCliente());

            correo.addContent(
                    mensaje
                            + "</br></br><table align='center'><tr><td align='center'>"
                            + firma
                            + "</td></tr></table></div></body></html>");

            correo.sendMultipart();

            System.out.println(
                    "Correo enviado a "
                            + saldo.getCliente());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    private Properties generarProperties() {
        Properties smtp = new Properties();
        try {
            String port = ConfigReader.get("correo.smtp.port");
            if (port != null && !port.isBlank()) {
                smtp.put("mail.smtp.port", port);
            }

            String auth = ConfigReader.get("correo.smtp.auth");
            if (auth != null && !auth.isBlank()) {
                smtp.put("mail.smtp.auth", auth);
            }

            String tls = ConfigReader.get("correo.smtp.starttls.enable");
            if (tls != null && !tls.isBlank()) {
                smtp.put("mail.smtp.starttls.enable", tls);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE,
                    "Error al generar las propiedas para el envio del correo ",
                    ex);
        }
        return smtp;
    }

    private String obtenerCorreos(List<Saldos> contactos) {

        StringBuilder correos = new StringBuilder();
        for (Saldos contacto : contactos) {
            if (contacto.getCorreo() != null
                    && !contacto.getCorreo().trim().isEmpty()) {
                if (correos.length() > 0) {
                    correos.append(",");
                }

                correos.append(contacto.getCorreo());
            }
        }
        return correos.toString();

    }

    private String obtenerSaludo(int hora) {

        if (hora < 12) {
            return "Buenos D&iacute;as";
        }

        if (hora < 19) {
            return "Buenas Tardes";
        }
        return "Buenas Noches";

    }

    private String construirMensaje(
            String cadena,
            long monto,
            String saludo) {

        return "<html><head></head><body>"
                + "<div style=\"color:navy;font-family:Arial;font-size:18px\">"
                + saludo
                + ":<br><br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "El saldo de su bolsa prepagada para Venta de Tiempo Aire para "
                + "<b>"
                + cadena
                + "</b> es: "
                + "<b>$"
                + monto
                + "</b> Dls.<br><br>";

    }

}