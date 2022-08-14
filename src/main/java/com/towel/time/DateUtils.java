package com.towel.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static final boolean isValidDate(String psDt) {
        String a = psDt.trim();
        if (a.length() != 10) {
            return false;
        }
        try {
            Integer dia = new Integer(a.substring(0, 2));
            Integer mes = new Integer(a.substring(3, 5));
            Integer ano = new Integer(a.substring(6));
            if (mes.intValue() > 12) {
                return false;
            }
            if (mes.intValue() == 2) {
                if (ano.intValue() % 4 == 0) {
                    if (dia.intValue() > 29) {
                        return false;
                    }
                } else if (dia.intValue() > 28) {
                    return false;
                }
            } else if (dia.intValue() > 31) {
                return false;
            } else {
                if (dia.intValue() > 30 && (mes.intValue() == 4 || mes.intValue() == 6 || mes.intValue() == 9 || mes.intValue() == 11)) {
                    return false;
                }
            }
            if (dia.intValue() > 31) {
                return false;
            }
            try {
                Date dataConv = formatter.parse(a);
                Date dataLimiteInf = formatter.parse("01/01/1900");
                if (dataConv.after(formatter.parse("06/06/2079")) || dataConv.before(dataLimiteInf)) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static final Date parseDate(String dateString, String psFormato) {
        return parseDate(dateString, new SimpleDateFormat(psFormato));
    }

    public static final Date parseDate(String dateString, SimpleDateFormat formatter2) {
        try {
            return formatter2.parse(dateString);
        } catch (Exception e) {
            return new Date(0);
        }
    }

    public static final Date parseDate(String psDt) {
        return parseDate(psDt, formatter);
    }

    public static final String format(Date pDt) {
        return formatter.format(pDt);
    }

    public static final String format(Date pDt, String psFormato) {
        return new SimpleDateFormat(psFormato).format(pDt);
    }

    public static final String textToMMDDYYYY(String pdata) {
        if (pdata.equals("__/__/____") || pdata.equals("")) {
            return "";
        }
        return String.valueOf(pdata.substring(3, 5)) + "/" + pdata.substring(0, 2) + "/" + pdata.substring(6);
    }

    public static long msDate(Date pData) {
        return ((((pData.getTime() + 2209154400000L) / 1000) / 60) / 60) / 24;
    }

    public static long msDate(String pData) {
        return ((((parseDate(pData).getTime() + 2209154400000L) / 1000) / 60) / 60) / 24;
    }

    public static String getDateTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(new Date());
    }

    public static String getDateTimeSegundos() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
