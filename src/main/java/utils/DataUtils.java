package utils;




import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {

    public static String getDataDiferenteDias(Integer quantidadeDeDias){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, quantidadeDeDias);
        return getDataFormatada(calendar.getTime());
    }

    public static String getDataFormatada(Date data){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(data);
    }


}
