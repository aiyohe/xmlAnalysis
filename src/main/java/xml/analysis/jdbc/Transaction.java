package xml.analysis.jdbc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: Mr.Zhang
 * @Description: 持久化操作
 * @Date: 10:27 2018/8/1
 * @Modified By:
 */
public class Transaction {
    private Connection conn;
    private PreparedStatement prs;
    // private ResultSet res;
    private static String url = "jdbc:jtds:sqlserver://210.74.194.121:1433/zxk;tds=8.0;lastupdatecount=true";
    private static String name = "sa";
    private static String psd = "!qaz2wsx";

    private void init() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(url, name, psd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param jsar 数据
     * @param time 时间 yyyyMMdd
     * @param type 1 12时数据表 2 10天数据表
     */
    public void saveData(JSONArray jsar, String time, int type) {
        init();
        String sql;
        if (type == 2) {
            sql = " insert  into  t_weather_forecast_day ( id,stationcode,stationname,hour,wspeed,wdir,t,wp,humi,time,date)  values (?,?,?,?,?,?,?,?,?,?,?)";
        } else {
            sql = " insert  into  t_weather_forecast_hour ( id,stationcode,stationname,hour,wspeed,wdir,t,wp,humi,time,date)  values (?,?,?,?,?,?,?,?,?,?,?)";
        }
        String date=getDate(time);
        try {
            for (int i = 0; i < jsar.size(); i++) {
                JSONObject json = (JSONObject) jsar.get(i);
                String stationcode = (String) json.get("stationcode");
                String stationname = (String) json.get("stationname");
                JSONArray dataJs = (JSONArray) json.get("data");
                for (int j = 0; j < dataJs.size(); j++) {
                    JSONObject data = (JSONObject) dataJs.get(j);
                    prs = conn.prepareStatement(sql);
                    prs.setString(1, UUID.randomUUID().toString());
                    prs.setString(2, stationcode);
                    prs.setString(3, stationname);
                    prs.setInt(4, Integer.valueOf((String) data.get("hour")));
                    prs.setString(5, (String) data.get("wspeed"));
                    prs.setString(6, (String) data.get("wdir"));
                    prs.setString(7, (String) data.get("t"));
                    prs.setString(8, (String) data.get("wp"));
                    prs.setString(9, (String) data.get("humi"));
                    prs.setString(10,time);
                    prs.setString(11,date);
                    prs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * 获取日期
     * @param time
     * @return
     */
    public String getDate( String time){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
        String da=null;
        try {
            Date date=format.parse(time);
            SimpleDateFormat form=new SimpleDateFormat("yyyyMMdd");
             da = form.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }
    private void close() {
        try {
//            if (res != null) {
//                res.close();
//            }
            if (prs != null) {
                prs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
