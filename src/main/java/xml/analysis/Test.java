package xml.analysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Mr.Zhang
 * @Description:
 * @Date: 15:34 2018/8/2
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
        String dd="201704031500";
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date date=format.parse(dd);
            SimpleDateFormat form=new SimpleDateFormat("yyyyMMdd");
            String format1 = form.format(date);
            System.out.println(format1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
