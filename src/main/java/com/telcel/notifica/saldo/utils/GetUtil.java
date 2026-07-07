package com.telcel.notifica.saldo.utils;

import com.telcel.notifica.saldo.dao.Saldos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class GetUtil {
    /**
     *
     */
    public static String RUTACONF = ConfigReader.get("ruta.destinatarios");

    public static List<Saldos> getDestinatariosMail(String[] tipoHorario) throws Exception {
        System.out.println("entra a get destinatarios con tipoHorario");

        List<Saldos> dest = new ArrayList<Saldos>();
        try {
            if (tipoHorario.length > 1) {
                for (int i = 0; i <= tipoHorario.length - 1; i++) {
                    File destFile = new File(RUTACONF);
                    if (destFile.exists()) {
                        BufferedReader bf = new BufferedReader(new FileReader(destFile));
                        String linea = "";
                        while ((linea = bf.readLine().trim()) != null && !linea.equals("") &&
                                !linea.contains("final")) {
                            String[] linDest = linea.split(",");
                            //System.out.println("CSV=" + linDest[4] + "  BUSCANDO=" + tipoHorario[i]);
                            if (linDest[4].equals(tipoHorario[i])) {
                                System.out.println(linDest[0] + "-" + linDest[4] + "-" + tipoHorario[i]);
                                if ((!linDest[2].equals("") || !linDest[3].equals("")) && (
                                        !dest.contains(linDest[2]) || !dest.contains(linDest[3]))) {
                                    dest.add(new Saldos(linDest[0], linDest[2], linDest[3]));
                                    //System.out.println("Se agrego a lista");
                                }
                            }
                        }
                    } else {
                        System.out.println("No se encontro el archivo de Destinaratios");
                        return dest;
                    }
                }
            } else {
                File destFile = new File(RUTACONF);
                if (destFile.exists()) {
                    BufferedReader bf = new BufferedReader(new FileReader(destFile));
                    String linea = "";
                    while ((linea = bf.readLine().trim()) != null && !linea.equals("") &&
                            !linea.contains("final")) {
                        String[] linDest = linea.split(",");
                        if (linDest[4].equals(tipoHorario[0])) {
                            System.out.println(linDest[0] + "-" + linDest[4] + "-" + tipoHorario[0]);
                            if ((!linDest[2].equals("") || !linDest[3].equals("")) && (
                                    !dest.contains(linDest[2]) || !dest.contains(linDest[3]))) {
                                dest.add(new Saldos(linDest[0], linDest[2], linDest[3]));
                                System.out.println("Se agrego a lista");
                            }
                        }
                    }
                } else {
                    System.out.println("No se encontro el archivo de Destinaratios");
                    return dest;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }

/*  public static StringBuffer creaTablaEnvios(Set<Saldos> datNotifica) {
    StringBuffer tablaenvios = new StringBuffer();
    Boolean cambioColor = Boolean.valueOf(true);
    try {
      tablaenvios.append("<table width=\"95%\" align=\"center\" ><tr>");
      tablaenvios.append("<TD noWrap align=middle width=\"30%\" colspan=\"3\"><FONT size='-1' color=\"#FFFFFF\"></FONT></TD>");
      tablaenvios.append("<TD noWrap align=middle width=\"40%\" colspan=\"3\"><font size=3 color=navy face=Verdana align=\"center\"><strong>Bitacora de Envio de Saldos</strong></font></TD>");
      tablaenvios.append("<TD noWrap align=middle width=\"30%\" colspan=\"3\"><FONT size='-1' color=\"#FFFFFF\"></FONT></TD>");
      tablaenvios.append("</tr></table><br>");
      tablaenvios.append("<table width=\"95%\" align=\"center\" ><tr bgcolor=\"#333399\" size='-1' color=\"#FFFFFF\">");
      tablaenvios.append("<TD noWrap align=middle width=\"10%\" colspan=\"4\"><FONT size='-1' color=\"#FFFFFF\">Detalle de Envios</FONT></TD>");
      tablaenvios.append("</tr><tr bgcolor=\"#333399\" size='-1' color=\"#FFFFFF\">");
      tablaenvios.append("<TD noWrap align=middle width=\"8.33%\" ><FONT size='-1' color=\"#FFFFFF\">Cadena</FONT></TD>");
      tablaenvios.append("<TD noWrap align=middle width=\"8.33%\" ><FONT size='-1' color=\"#FFFFFF\">Corresponde Notificacion</FONT></TD>");
      tablaenvios.append("<TD noWrap align=middle width=\"8.33%\" ><FONT size='-1' color=\"#FFFFFF\">Correo</font></TD>");
      tablaenvios.append("<TD noWrap align=middle width=\"8.33%\" ><FONT size='-1' color=\"#FFFFFF\">Mensaje</FONT></TD></tr>");
      for (Saldos confEnvio : datNotifica) {
        if (cambioColor.booleanValue()) {
          tablaenvios.append("<tr bgcolor=\"#f7f8e0\" >");
          cambioColor = Boolean.valueOf(false);
        } else {
          tablaenvios.append("<tr bgcolor=\"#e0ecf8\" >");
          cambioColor = Boolean.valueOf(true);
        }
        tablaenvios.append("<TD noWrap align=middle width=\"10%\" ><FONT size='-1' >" + confEnvio.getCliente() + "</FONT></TD>");
        tablaenvios.append("<TD noWrap align=middle width=\"10%\" ><FONT size='-1' >" + confEnvio.getTocaNot() + "</FONT></TD>");
        tablaenvios.append("<TD noWrap align=middle width=\"10%\" ><FONT size='-1' >" + confEnvio.getCorreo() + "</FONT></TD>");
        tablaenvios.append("<TD noWrap align=middle width=\"10%\" ><FONT size='-1' >" + confEnvio.getTelefono() + "</FONT></TD>");
      }
      tablaenvios.append("</tr></table><br>");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tablaenvios;
  }*/

    public static StringBuffer creaTablaEnvios(Set<Saldos> datNotifica) {
        StringBuilder tablaenvios = new StringBuilder(); // StringBuilder es más rápido
        boolean cambioColor = true; // boolean primitivo, no Boolean

        tablaenvios.append("<table class='tabla-envios'><tr>")
                .append("<th colspan='3'>Bitácora de Envio de Saldos</th>")
                .append("</tr><tr class='cabecera'>")
                .append("<th>Cadena</th><th>Notificación</th><th>Correo</th><th>Mensaje</th></tr>");

        for (Saldos confEnvio : datNotifica) {
            tablaenvios.append("<tr class='fila-" + (cambioColor ? "amarilla" : "azul") + "'>")
                    .append("<td>").append(confEnvio.getCliente()).append("</td>")
                    .append("<td>").append(confEnvio.getTocaNot()).append("</td>")
                    .append("<td>").append(confEnvio.getCorreo()).append("</td>")
                    .append("<td>").append(confEnvio.getTelefono()).append("</td>")
                    .append("</tr>");
            cambioColor = !cambioColor;
        }
        tablaenvios.append("</table>");
        return new StringBuffer(tablaenvios.toString()); // Compatibilidad si se necesita StringBuffer
    }

    public static String getFirmaComercioElectronico(String telefono1, String ext, String telefono2) {
        StringBuilder html = new StringBuilder("<i><FONT SIZE=3 COLOR=navy Face='Arial'>Cualquier duda u observaci&oacute;n, favor de enviarla a la cuenta &nbsp;&nbsp;<b><a href='mailto:comercio.electronico@mail.telcel.com'>comercio.electronico@mail.telcel.com</a></b></font></i>");
        html.append("<br><br><b><font size=2 color=navy face=Arial>Comercio Electr&oacute;nico M&oacute;vil</font></b>");
        html.append("<br><br><font size=2 color=blue face=Wingdings>( &nbsp;</font><font size=2 color=navy face=Arial style=italic>" + telefono1 + "&nbsp;&nbsp; Ext: " + ext + "</font>");
        html.append("<br><br><font size=2 color=blue face=Wingdings>( &nbsp;</font><font size=2 color=navy face=Arial style=italic>Directo: " + telefono2 + "</font>");
        html.append("<br><br><font size=2 color=blue face=Wingdings>* &nbsp;</font><font size=2 color=navy face=Arial>comercio.electronico@mail.telcel.com</font>");
        return html.toString();
    }
}