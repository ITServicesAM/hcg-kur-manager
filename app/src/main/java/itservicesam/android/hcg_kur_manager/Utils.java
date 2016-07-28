package itservicesam.android.hcg_kur_manager;

import org.greenrobot.greendao.annotation.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {

    public static final int VALUE_TYPE_BAUCH = 1;
    public static final int VALUE_TYPE_LI_OBERSCH = 2;
    public static final int VALUE_TYPE_RE_OBERSCH = 3;
    public static final int VALUE_TYPE_BRUST = 4;
    public static final int VALUE_TYPE_PO = 5;

    static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

    static String getDbDateString(@NotNull Date date) {
        return sdf.format(date);
    }

    /**
     * Uses {@link SimpleDateFormat} (ddMMyyyy) to parse the String to Date
     **/
    static Date getDateFromDbDateString(@NotNull String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int lastValue(int valueType, List<BodyData> data, Date curDate) {
        int lastValue = -1;
        switch (valueType) {
            case VALUE_TYPE_BAUCH:
                for (BodyData bodyData : data) {
                    if (bodyData.getBauchUmfang() > -1 && isBefore(curDate, bodyData.getDate()))
                        lastValue = bodyData.getBauchUmfang();
                }
                break;
            case VALUE_TYPE_LI_OBERSCH:
                for (BodyData bodyData : data) {
                    if (bodyData.getOberschenkelUmfangLinks() > -1 && isBefore(curDate, bodyData.getDate()))
                        lastValue = bodyData.getOberschenkelUmfangLinks();
                }
                break;
            case VALUE_TYPE_RE_OBERSCH:
                for (BodyData bodyData : data) {
                    if (bodyData.getOberschenkelUmfangRechts() > -1 && isBefore(curDate, bodyData.getDate()))
                        lastValue = bodyData.getOberschenkelUmfangRechts();
                }
                break;
            case VALUE_TYPE_BRUST:
                for (BodyData bodyData : data) {
                    if (bodyData.getBrustUmfang() > -1 && isBefore(curDate, bodyData.getDate()))
                        lastValue = bodyData.getBrustUmfang();
                }
                break;
            case VALUE_TYPE_PO:
                for (BodyData bodyData : data) {
                    if (bodyData.getPoUmfang() > -1 && isBefore(curDate, bodyData.getDate()))
                        lastValue = bodyData.getPoUmfang();
                }
                break;
        }
        return lastValue;
    }

    private static boolean isBefore(Date dateStartDay, Date dateZuVergleichen) {
        return dateZuVergleichen.before(dateStartDay);
    }
}
