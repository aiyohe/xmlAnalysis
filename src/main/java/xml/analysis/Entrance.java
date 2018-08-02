package xml.analysis;

import com.alibaba.fastjson.JSONArray;
import xml.analysis.jdbc.Transaction;
import xml.analysis.util.DomJaxpXML;

/**
 * @Author: Mr.Zhang
 * @Description: 入口程序
 * @Date: 10:12 2018/8/1
 * @Modified By:
 */
public class Entrance {

    public static void xmlAnalysis(String filePath) {
        JSONArray jsar = DomJaxpXML.getXMLFromDom(filePath);
        String[] filePaths = filePath.split("\\.");
        String fileName = filePaths[0];
        String[] file = fileName.split("_");
        String date = file[file.length - 2];
        String[] types = file[file.length-1].split("-");
        String ty = types[types.length-1];
        int type = 1;//默认 12小时预警
        if ("24012".equals(ty)) {
            type = 2;
        }
        Transaction trans = new Transaction();
        trans.saveData(jsar, date, type);
    }

    public static void main(String[] args) {
        String filePath = "E:/cs/xml/MSP2_BJ-MO_WF_ME_LNO_BJ_201806021100_00000-24012.xml";
        xmlAnalysis(filePath);
    }
}
