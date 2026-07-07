package com.telcel.notifica.saldo.service;

import com.telcel.mail.EnviaMail;
import com.telcel.notifica.saldo.dao.Saldos;
import com.telcel.notifica.saldo.utils.ConfigReader;
import com.telcel.notifica.saldo.utils.GetUtil;

import java.util.List;

public class CorreoSaldoAgotadoService {

    public boolean enviar(Saldos saldo,
                          List<Saldos> contactos,
                          int hora) {

        try {
            String correos = obtenerCorreos(contactos);
            if (correos.isEmpty()) {
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

            String mensaje =
                    "<html><head></head><body>"
                            + "<div style=\"color:navy;font-family:Arial;font-size:18px\">"
                            + saludo
                            + ":<br><br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "CEM informa que el saldo de su bolsa prepagada para <b>"
                            + saldo.getCliente()
                            + "</b> se ha agotado, por lo que le sugerimos realizar una nueva compra y validar que ésta se abone a su bolsa.<br><br>";

            EnviaMail correo = new EnviaMail(
                    ConfigReader.get("correo.usuario"),
                    ConfigReader.get("correo.password"),
                    null,
                    null);

            correo.setFrom(
                    ConfigReader.get("correo.remitente"));

            correo.setTo(correos);

            correo.setSubject(
                    "NOTIFICACION DE SALDO AGOTADO PARA "
                            + saldo.getCliente());

            correo.addContent(
                    mensaje
                            + "</br></br><table align='center'><tr><td align='center'>"
                            + firma
                            + "</td></tr></table></div></body></html>");

            correo.sendMultipart();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

    private String obtenerCorreos(List<Saldos> contactos) {

        StringBuilder sb = new StringBuilder();

        for (Saldos contacto : contactos) {

            if (contacto.getCorreo() == null ||
                    contacto.getCorreo().trim().isEmpty()) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(",");
            }

            sb.append(contacto.getCorreo());

        }

        return sb.toString();

    }

}