package itservicesam.android.hcg_kur_manager;

import org.greenrobot.greendao.annotation.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

    static String getDbDateString(@NotNull Date date) {
        return sdf.format(date);
    }

    /** Uses {@link SimpleDateFormat} (ddMMyyyy) to parse the String to Date
     * **/
    static Date getDateFromDbDateString(@NotNull String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
