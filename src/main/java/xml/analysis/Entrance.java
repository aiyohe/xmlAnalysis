package xml.analysis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xml.analysis.jdbc.Transaction;
import xml.analysis.util.DomJaxpXML;

/**
 * @Author: Mr.Zhang
 * @Description: 入口程序
 * @Date: 10:12 2018/8/1
 * @Modified By:
 */
public class Entrance {

    public static void xmlAnalysis(String filePath){
        JSONArray jsar = DomJaxpXML.getXMLFromDom(filePath);
        String date="";
        Transaction trans=new Transaction();
        trans.saveData(jsar,date,2);
    }

    public static void main(String[] args) {
        String filePath="E:/cs/xml/MSP2_BJ-MO_WF_ME_LNO_BJ_201805301700_00000-24012.xml";
        xmlAnalysis(filePath);
    }
}
