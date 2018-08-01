package xml.analysis.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @Author: Mr.Zhang
 * @Description: 用于备份
 * @Date: 13:08 2018/7/31
 * @Modified By:
 */
public class XmlUtil extends DefaultHandler {


    private static Logger logger = Logger.getLogger(DomJaxpXML.class);

    public static void main(String[] args) throws Exception {

        // ************************调整JVM 内存大小
        // java.lang.OutOfMemoryError: Java heap space 堆内存不足
        // 固定为64M
        // -Xmx80m 修改VM Arguments
        // byte[] b = new byte[1024 * 1024 * 70];

        XmlUtil JavapXML = new XmlUtil();

        // 通过Dom获取XML
        JavapXML.getXMLFromDom();

        JavapXML.domXML();
    }

    /**
     * 通过DOM获取xml文档
     */
    public void getXMLFromDom() {

        try {

            // 1-: 获得dom解析器工厂（工作的作用是用于创建具体的解析器）
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // 2-:获得具体的dom解析器
            DocumentBuilder db = dbf.newDocumentBuilder();

            // 3-: 解析一个xml文档，获得Document对象
            // 利用classloader加载xml文件，文件的位置很重要
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("Request.xml");
            Document document = db.parse(input);


            // 4-通过元素名，得到某个元素

            // 4-1 得到<appnt>元素
            NodeList appntElement = document.getElementsByTagName("appnt");
            Node appnt = appntElement.item(0);// 得到第一个
            String name = appnt.getNodeName();// 得到元素的名称
            logger.debug("<appnt>元素的名称 :  " + name);// appnt

            // 4-2 得到<appnt>元素所有子元素
            NodeList appntChildren = appnt.getChildNodes();

            // 4-3 得到<appnt>元素下<name>元素
            Node appnt_name = appntChildren.item(1);
            logger.debug("<appnt>元素下<name>子元素的名称 ：   " + appnt_name.getNodeName());
            logger.debug("<appnt>元素下<name>子元素的value ：   " + appnt_name.getTextContent());

            // 4-4 得到<appnt>元素下<age>元素
            // 中间有text类的内容也会被解析，所以这边1跳转到了3
            // item(2)的话，会输出#Text,是指在<appnt>#Text<age>中间的文本信息
            Node appnt_age = appntChildren.item(3);
            logger.debug("<appnt>元素下<age>子元素的名称 ：   " + appnt_age.getNodeName());
            logger.debug("<appnt>元素下<age>子元素的value ：   " + appnt_age.getTextContent());


            // 5-获取属性值
            Element insuredElement = (Element) document.getElementsByTagName("insured").item(0);
            String insuredName = insuredElement.getAttribute("name");
            String insuredAge = insuredElement.getAttribute("age");

            logger.debug("<insured>元素的name属性为: " + insuredName + " |age属性为" + insuredAge);


            // 6-添加节点元素

            // 6-1 新建元素
            Element element = document.createElement("新建标签");
            element.setAttribute("属性", "10块");
            element.setTextContent("我是动态新建的元素");

            // 6-2 放入根节点元素
            Element root = document.getDocumentElement();//获取根节点
            root.appendChild(element);//新建的元素放入根节点元素

            // 6-3 写入文件，需要使用 TransformerFactory 固定写法
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer transformer = tff.newTransformer();

            // 写出去
            DOMSource domSource = new DOMSource(document);
            Result result = new StreamResult(new FileOutputStream("resources/Request.xml"));
            transformer.transform(domSource, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Dom解析整个XML文档
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void domXML() throws ParserConfigurationException, SAXException, IOException {
        // 1-: 获得dom解析器工厂（工作的作用是用于创建具体的解析器）
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // 2-:获得具体的dom解析器
        DocumentBuilder db = dbf.newDocumentBuilder();

        // 3-: 解析一个xml文档，获得Document对象InputStream
        Document document = db.parse(new File("resources/Request.xml"));

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
