package xml.analysis.util;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DomJaxpXML {

    private static Logger logger = Logger.getLogger(DomJaxpXML.class);

    public static void main(String[] args) throws Exception {
        //DomJaxpXML JavapXML = new DomJaxpXML();
        String filePath = "E:/cs/xml/MSP2_BJ-MO_WF_ME_LNO_BJ_201805301700_00000-24012.xml";
        // 通过Dom获取XML
        DomJaxpXML.getXMLFromDom(filePath);

        // JavapXML.domXML(filePath);
    }

    /**
     * 通过DOM获取xml文档
     */
    public static JSONArray getXMLFromDom(String filePath) {
        JSONArray jsar = new JSONArray();
        try {

            // 1-: 获得dom解析器工厂（工作的作用是用于创建具体的解析器）
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // 2-:获得具体的dom解析器
            DocumentBuilder db = dbf.newDocumentBuilder();

            // 3-: 解析一个xml文档，获得Document对象
            // 利用classloader加载xml文件，文件的位置很重要
            Document document = db.parse(filePath);
            // 4-通过元素名，得到某个元素
            //// 获取属性值--循环获取 子节点数据 一级 节点
            NodeList stationList = document.getElementsByTagName("station");
            for (int i = 0; i < stationList.getLength(); i++) {
                JSONObject json = new JSONObject();
                Element insuredElement = (Element) document.getElementsByTagName("station").item(i);
                String stationcode = insuredElement.getAttribute("stationcode");
                String stationname = insuredElement.getAttribute("stationname");
                NodeList dataList = insuredElement.getElementsByTagName("data");
                JSONArray datajs = new JSONArray();
                for (int j = 0; j < dataList.getLength(); j++) {
                    Node node = dataList.item(j);
                    NodeList childNodes = node.getChildNodes();
                    JSONObject dataJs = new JSONObject();
                    for (int k = 0; k < childNodes.getLength(); k++) {
                        JSONObject js = new JSONObject();
                        Node item = childNodes.item(k);
                        String nodeName = item.getNodeName();
                        if (!"#text".equals(nodeName)) {
                            js.put(nodeName, item.getTextContent());
                            dataJs.put(nodeName, item.getTextContent());
                        }
                    }
                    datajs.add(dataJs);
                }
                json.put("stationcode", stationcode);
                json.put("stationname", stationname);
                json.put("data", datajs);
                jsar.add(json);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" xml 数据 :" + jsar.toString());
        return jsar;
    }

    /**
     * Dom解析整个XML文档
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void domXML(String filePath) throws ParserConfigurationException, SAXException, IOException {
        // 1-: 获得dom解析器工厂（工作的作用是用于创建具体的解析器）
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // 2-:获得具体的dom解析器
        DocumentBuilder db = dbf.newDocumentBuilder();

        // 3-: 解析一个xml文档，获得Document对象InputStream
        Document document = db.parse(new File(filePath));

        // 4-得到根节点
        Element requestElement = document.getDocumentElement();
        logger.debug("得到根节点：  " + requestElement.getNodeName());

        // 需要使用递归
        listChildrenNodes(requestElement);
    }

    /**
     * 递归方法 打印的时候会有每个Text对象,会处理掉
     *
     * @param root
     */
    private void listChildrenNodes(Node root) {

        // 先判断一步， 把Text对象过滤掉
        if (root instanceof Element) {
            logger.debug("节点的名字为：    " + root.getNodeName());
        }

        NodeList childrenLists = root.getChildNodes();
        for (int i = 0; i < childrenLists.getLength(); i++) {
            Node child = childrenLists.item(i);
            // 递归调用
            listChildrenNodes(child);
        }

    }

}
