package xml.analysis;

import com.alibaba.fastjson.JSONArray;
import xml.analysis.jdbc.Transaction;
import xml.analysis.util.DomJaxpXML;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author: Mr.Zhang
 * @Description: 入口程序
 * @Date: 10:12 2018/8/1
 * @Modified By:
 */
public class Entrance {

    public static void xmlAnalysis(String filePath) {
        String[] filePaths = filePath.split("\\.");
        if ("xml".equals(filePaths[1])) {
            String fileName = filePaths[0];
            String[] file = fileName.split("_");
            String times = file[file.length - 2];
            String[] types = file[file.length - 1].split("-");
            String ty = types[types.length - 1];
            int type = 1;//默认 12小时预警
            if ("24012".equals(ty)) {
                type = 2;
            }
            Transaction trans = new Transaction();
            JSONArray jsar = DomJaxpXML.getXMLFromDom(filePath);
            trans.saveData(jsar, times, type);
        }
    }

    public static void main(String[] args) {
        String filePath = "E:/cs/xml/";
        String filee = "E:/cs/xml/MSP2_BJ-MO_WF_ME_LNO_BJ_201807231100_00000-01201.xml";
       // xmlAnalysis(filee);
        ArrayList<String> files = getFiles(filePath);
        for (String file : files) {
            System.out.println("文件名：" + file);
            xmlAnalysis(file);
        }
    }

    /**
     * 获取文件夹下的文件
     *
     * @param path
     * @return
     */
    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
//              System.out.println("文     件：" + tempList[i]);
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
//              System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
    }

    /**
     * 获取文件夹下所有xml文件解析
     *
     * @param filePath
     */
    public static void xmlAnalysisForFiles(String filePath) {
        ArrayList<String> files = getFiles(filePath);
        for (String file : files) {
//            System.out.println("文件名：" + file);
            xmlAnalysis(file);
        }
    }
}
