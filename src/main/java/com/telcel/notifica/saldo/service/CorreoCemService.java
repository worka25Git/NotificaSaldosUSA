package com.telcel.notifica.saldo.service;

import com.telcel.mail.EnviaMail;
import com.telcel.notifica.saldo.dao.Saldos;
import com.telcel.notifica.saldo.utils.ConfigReader;
import com.telcel.notifica.saldo.utils.GetUtil;
import com.telcel.notifica.saldo.utils.PropiedadesSmtp;

import java.util.Set;

public class CorreoCemService {

    public boolean enviar(Set<Saldos> notificaciones,
                          int hora) {

        try {

            if (notificaciones.isEmpty()) {
                return false;
            }

            StringBuffer tabla = GetUtil.creaTablaEnvios(notificaciones);

            if (tabla.length() == 0) {
                return false;
            }

            String saludo;

            if (hora < 12) {
                saludo = "Buenos D&iacute;as";
            } else if (hora < 19) {
                saludo = "Buenas Tardes";
            } else {
                saludo = "Buenas Noches";
            }

            String firma =
                    GetUtil.getFirmaComercioElectronico(
                            ConfigReader.get("firma.telefono1"),
                            ConfigReader.get("firma.extension"),
                            ConfigReader.get("firma.telefono2"));

            String cuerpo =
                    "<html><body>"
                            + saludo
                            + "<br><br>"
                            + "Por este medio se informa el resultado del proceso de notificación de saldos de las "
                            + hora
                            + " hrs.<br><br>"
                            + tabla.toString()
                            + "<br><br>"
                            + firma
                            + "</body></html>";

            EnviaMail correo = new EnviaMail(
                    ConfigReader.get("correo.usuario"),
                    ConfigReader.get("correo.password"),
                    ConfigReader.get("correo.host"),
                    PropiedadesSmtp.generarProperties());

            correo.setFrom(
                    ConfigReader.get("correo.remitente"));

            // durante pruebas
            correo.setTo(
                    ConfigReader.get("correo.cem"));

            correo.setSubject(
                    "INFORME SOBRE NOTIFICACION DE SALDO PARA CADENAS USA DE LAS "
                            + hora
                            + " HRS.");

            correo.addContent(cuerpo);

            correo.sendMultipart();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

}