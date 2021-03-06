package com.third.dwgame.utils;

import android.util.Log;
import android.util.Xml;

import com.third.dwgame.bean.ADPlayBean;
import com.third.dwgame.bean.PlayDataBean;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 作者：Sky on 2018/3/6.
 * 用途：解析文件工具
 */

public class ParseFileUtil {

    /**
     * 直接解析
     * @param is
     * @return
     */
    public static  List<PlayDataBean> parsePlayData(InputStream is) {
        Log.v("rss", "开始解析XML.");
        List<PlayDataBean> list = new ArrayList<PlayDataBean>();
        String itemElement = "configRoot";
        String adElement = "PLAY_DATA";
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            int event = xmlPullParser.getEventType();
            PlayDataBean playDataBean = null;
            ADPlayBean adPlayBean = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            playDataBean = new PlayDataBean();
                        }
                        if (adElement.equals(xmlPullParser.getName())) {
                            adPlayBean = new ADPlayBean();
                        }
                        if (playDataBean != null){
                            if(xmlPullParser.getName().equals("DEFAULT_SERIAL_PORT")){
                                playDataBean.setDefaultSerialPort(xmlPullParser.nextText());
                            }else if(xmlPullParser.getName().equals("DEFAULT_SERIAL_RATE")){
                                playDataBean.setDefaultSerialRate(xmlPullParser.nextText());
                            }else if(xmlPullParser.getName().equals("DEFAULT_PLAY")){
                                playDataBean.setDefaultPlayPath(xmlPullParser.nextText());
                            }

                            if(adPlayBean != null){
                                if(xmlPullParser.getName().equals("AD_PLAY_TIME")){
                                    adPlayBean.setAdPlayStartTime(xmlPullParser.nextText());
                                }else if(xmlPullParser.getName().equals("AD_PLAY_PATH")){
                                    adPlayBean.setAdPlayPath(xmlPullParser.nextText());
                                }else if(xmlPullParser.getName().equals("AD_TEXT_CONTENT")){
                                    adPlayBean.setAdContent(xmlPullParser.nextText());
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            list.add(playDataBean);
                            playDataBean = null;
                        }else if(adElement.equals(xmlPullParser.getName())){
                            if(playDataBean != null){
                                playDataBean.getAdPlayBeanList().add(adPlayBean);
                                adPlayBean = null;
                            }
                        }
                        break;
                }
                event = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.e("rss", "解析XML异常：" + e.getMessage());
            throw new RuntimeException("解析XML异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 解析XML转换成对象
     *
     * @param is
     *            输入流
     * @param clazz
     *            对象Class
     * @param fields
     *            字段集合一一对应节点集合
     * @param elements    //d（这两行标红，是因为这两个是java中字段和xml文件中的字段对应）队医
     *            节点集合一一对应字段集合
     * @param itemElement
     *            每一项的节点标签
     * @return
     */

    //静态方法中加入泛型，需要申明<T>，如果不是对泛型不是很熟悉，可以先用Object代替，然后再换回来

    public static <T> List<T> parse(InputStream is, Class<T> clazz,
                                    List<String> fields,
                                    List<String> elements,
                                    String itemElement) {
        Log.v("rss", "开始解析XML.");
        List<T> list = new ArrayList<T>();
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            int event = xmlPullParser.getEventType();
            T obj = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            obj = clazz.newInstance();
                        }
                        if (obj != null
                                && elements.contains(xmlPullParser.getName())) {
                            setFieldValue(obj, fields.get(elements
                                            .indexOf(xmlPullParser.getName())),
                                    xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            list.add(obj);
                            obj = null;
                        }
                        break;
                }
                event = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.e("rss", "解析XML异常：" + e.getMessage());
            throw new RuntimeException("解析XML异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 设置字段值
     *
     * @param propertyName
     *            字段名
     * @param obj
     *            实例对象
     * @param value
     *            新的字段值
     * @return
     */
    public static void setFieldValue(Object obj, String propertyName,
                                     Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    /**
     * 修改tag内容
     * @param tagNameAndValues
     */
    public static void updateTagContents(File xmlFile,HashMap<String,String> tagNameAndValues,String itemElement) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList rootList = doc.getElementsByTagName(itemElement);
            Element emp = null;
            for(int i=0; i < rootList.getLength();i++){
                for (Map.Entry<String, String> entry : tagNameAndValues
                        .entrySet()) {
                    emp = (Element) rootList.item(i);
                    NodeList list = emp.getElementsByTagName(entry.getKey());
                    Node name = list.item(0).getFirstChild();
                    name.setNodeValue(entry.getValue());
                }
            }
            doc2XmlFile(doc, xmlFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将修改后的数据保存
     * @param document
     * @param filename
     * @return
     */
    private static boolean doc2XmlFile(Document document, String filename) {
        boolean flag = true;
        try {
            /** 将document中的内容写入文件中 */
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            /** 编码 */
            // transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        return flag;
    }

}
