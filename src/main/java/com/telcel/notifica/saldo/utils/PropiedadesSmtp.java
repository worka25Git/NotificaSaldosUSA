package com.telcel.notifica.saldo.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropiedadesSmtp {

    private static final Logger logger = Logger.getLogger(PropiedadesSmtp.class.getName());


    public static Properties generarProperties() {
        Properties smtp = null;
        try {
            smtp = new Properties();
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
}
