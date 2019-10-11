package id.booble.absenmember.util;

import java.text.DecimalFormat;

public class Util {

    public static String currencyFormatterString(String num){
        if (num=="0"  || num==""){
            return "";
        }

        Double m = Double.parseDouble(num);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###");
        return decimalFormat.format(m).replace(",",".");
    }

}
