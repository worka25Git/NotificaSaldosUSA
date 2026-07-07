package com.telcel.notifica.saldo.envio;

import com.telcel.notifica.saldo.service.*;
import com.telcel.notifica.saldo.utils.ConfigReader;
import com.telcel.notifica.saldo.dao.Saldos;
import com.telcel.notifica.saldo.utils.ConstanteNum;
import com.telcel.notifica.saldo.utils.GetUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class EnviaNotificacionUSA {

    private static final Logger logger =
            Logger.getLogger(EnviaNotificacionUSA.class.getName());

    private static Handler handler;

    private static final SimpleFormatter formatter =
            new SimpleFormatter();

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
        Locale.setDefault(new Locale("ES", "MX"));
    }

    private List<Saldos> consultaSaldo = new ArrayList<>();

    private List<Saldos> destinatarios = new ArrayList<>();

    private SaldoService saldoService = new SaldoService();

    private final Set<Saldos> notificaCEM = new HashSet<>();

    private final String[] Hora1 = {"1"};

    private final String[] Hora2 = {"2", "3"};

    private final String[] Hora3 = {"1", "2", "3"};

    private final String[] Hora4 = {"4", "5"};

    private final String[] Hora5 = {"4", "5", "6"};

    private final String[] Hora6 = {"1", "4", "5"};

    private final String[] Hora7 = {"5", "6"};

    private final String[] Hora8 = {"3", "5"};


    public static void main(String[] args) {
        try {

            ConfigReader.load("./conf/Configuracion.conf");
            EnviaNotificacionUSA app =
                    new EnviaNotificacionUSA();
            app.obtenerSaldos();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void obtenerSaldos() throws Exception {

        inicializarLogger();

        try {

            logger.info("========================INICIA PROCESO==========================");

            int hora =
                    Calendar.getInstance()
                            .get(Calendar.HOUR_OF_DAY);

            logger.info("COMENZANDO A OBTENER LOS CONTACTOS...");
            destinatarios = obtenerDestinatarios(hora);

            logger.info("TERMINO DE OBTENER LOS CONTACTOS...");

            logger.info("COMENZANDO A OBTENER SALDOS...");
            consultaSaldo = saldoService.obtenerSaldos();

            logger.info("FIN DE OBTENCION DE SALDOS...");

            if (destinatarios.isEmpty()) {
                logger.info("No hay contactos para este horario: " + hora + " hrs.");
                return;
            }

            if (consultaSaldo.isEmpty()) {
                logger.info("No hay saldos para enviar.");
                return;
            }

            for (Saldos saldo : consultaSaldo) {
                procesarSaldo(saldo, hora);
            }

            notificarCEM(hora);

        } finally {

            cerrarLogger();

        }

    }


    private void procesarSaldo(Saldos saldo, int hora) {

        try {
            List<Saldos> contactosCadena = new ArrayList<>();
            logger.info("Procesando cadena: " + saldo.getCliente());

            for (Saldos contacto : destinatarios) {
                String cadenaDestino = contacto.getCliente().trim().toUpperCase();
                String cadenaSaldo = saldo.getCliente().trim().toUpperCase();

                if (cadenaSaldo.contains(cadenaDestino)
                        || cadenaDestino.contains(cadenaSaldo)) {
                    contactosCadena.add(contacto);
                }
            }

            if (contactosCadena.isEmpty()) {
                logger.warning("No existen destinatarios para " + saldo.getCliente());
                return;
            }

            boolean notificaCorreo = enviarCorreo(
                            saldo,
                            hora,
                            contactosCadena);

            boolean notificaSms = enviarSms(
                            saldo,
                            contactosCadena);

            agregarResultadoCEM(
                    saldo,
                    notificaCorreo,
                    notificaSms,
                    true);

            if (saldo.getMonto() <= 20) {
                enviarCorreoSaldoAgotado(saldo, hora, contactosCadena);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    "Error procesando saldo "
                            + saldo.getCliente(),
                    e);
        }

    }

    private void enviarCorreoSaldoAgotado(Saldos saldo,
                                          int hora,
                                          List<Saldos> contactosCadena) {

        logger.info("COMENZZANDO ENVIO DE CORREOS DE SALDO AGOTADO...");

        CorreoSaldoAgotadoService service =
                new CorreoSaldoAgotadoService();

        boolean enviado = service.enviar(
                        saldo,
                        contactosCadena,
                        hora);

        logger.info(
                "Correo saldo agotado enviado para "
                        + saldo.getCliente()
                        + " = "
                        + enviado);

        logger.info("FIN ENVIO DE CORREOS DE SALDO AGOTADO.");

    }

    private boolean enviarSms(Saldos saldo,
                              List<Saldos> contactosCadena) {

        boolean sms = false;

        SmsService smsService = new SmsService();
        sms = smsService.enviar(
                saldo,
                contactosCadena);

        logger.info("SMS enviado = "
                + sms);
        return sms;
    }

    private boolean enviarCorreo(Saldos saldo,
                                 int hora,
                                 List<Saldos> contactosCadena) {

        boolean enviado = false;

        CorreoService correoService = new CorreoService();
        enviado = correoService.enviar(
                saldo,
                contactosCadena,
                hora);

        if (enviado) {
            logger.info("Correo enviado correctamente para "
                    + saldo.getCliente());
        } else {
            logger.warning("No fue posible enviar correo para "
                    + saldo.getCliente());
        }
        return enviado;
    }

    private void agregarResultadoCEM(Saldos saldo,
                                     boolean correoEnviado,
                                     boolean smsEnviado,
                                     boolean correspondeNotificacion) {

        notificaCEM.add(
                new Saldos(
                        saldo.getCliente(),
                        correoEnviado ? "SI" : "NO",
                        smsEnviado ? "SI" : "NO",
                        correspondeNotificacion ? "SI" : "NO"));

    }

    private void notificarCEM(int hora) {

        logger.info("GENERANDO REPORTE PARA CEM...");
        if (notificaCEM.isEmpty()) {
            logger.info("No hay registros para informar.");
            return;
        }

        CorreoCemService correoCemService =
                new CorreoCemService();

        boolean informe = correoCemService.enviar(
                        notificaCEM,
                        hora);

        logger.info(
                "Correo CEM enviado = "
                        + informe);

        logger.info("FIN REPORTE CEM.");

    }

    private void inicializarLogger() throws Exception {

        DateFormat format =
                new SimpleDateFormat("'BitacoraNotificaUSA_'yyyyMMdd'.log'");

        String archivo = format.format(new Date());
        String rutaLogs = ConfigReader.get("ruta.logs");
        File dir = new File(rutaLogs);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        handler = new FileHandler(
                rutaLogs + archivo,
                true);

        handler.setFormatter(formatter);

        logger.addHandler(handler);

        logger.setUseParentHandlers(true);

        logger.setLevel(Level.INFO);

    }


    private void cerrarLogger() {
        logger.info("========================TERMINO PROCESO==========================");
        if (handler != null) {
            handler.flush();
            handler.close();
        }
    }

    private List<Saldos> obtenerDestinatarios(int hora) throws Exception {

        if (hora == ConstanteNum.OCHO) //8
            return GetUtil.getDestinatariosMail(Hora1);

        if (hora == ConstanteNum.NUEVE //9
                || hora == ConstanteNum.TRECE //13
                || hora == ConstanteNum.QUINCE // 15
                || hora == ConstanteNum.DIECINUEVE //19
                || hora == ConstanteNum.VEINTIUNO // 21
            )
            return GetUtil.getDestinatariosMail(Hora2);

        if (hora == ConstanteNum.ONCE //11
                || hora == ConstanteNum.DIECISIETE //17
            )
            return GetUtil.getDestinatariosMail(Hora3);

        if (hora == ConstanteNum.DOCE //12
                || hora == ConstanteNum.DIECISEIS //16
                || hora == ConstanteNum.DIECIOCHO //18
            )
            return GetUtil.getDestinatariosMail(Hora4);

        if (hora == ConstanteNum.DIEZ) // 10
            return GetUtil.getDestinatariosMail(Hora5);

        if (hora == ConstanteNum.CATORCE //14
                || hora == ConstanteNum.VEINTE) //20
            return GetUtil.getDestinatariosMail(Hora6);

        if (hora == ConstanteNum.VEINTIDOS) //22
            return GetUtil.getDestinatariosMail(Hora7);

        if (hora == ConstanteNum.CERO) //0
            return GetUtil.getDestinatariosMail(Hora8);

        return new ArrayList<>();

    }
}