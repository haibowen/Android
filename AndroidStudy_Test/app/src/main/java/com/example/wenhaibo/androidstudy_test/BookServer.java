package com.example.wenhaibo.androidstudy_test;


import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class BookServer {
    //解析xml文件返回天气信息的集合
    public static List<Bookinfo> getInfosFromXML (InputStream is)
            throws Exception {
        //得到pull解析器
        XmlPullParser parser = Xml.newPullParser();
        // 初始化解析器,第一个参数代表包含xml的数据
        parser.setInput(is, "utf-8");
        List<Bookinfo> bookinfos = null;
       Bookinfo bookinfo = null;
        //得到当前事件的类型
        int type = parser.getEventType();
        // END_DOCUMENT文档结束标签
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //一个节点的开始标签
                case XmlPullParser.START_TAG:
                    //解析到全局开始的标签 infos 根节点
                    if("infos".equals(parser.getName())){
                        bookinfos = new ArrayList<Bookinfo>();
                    }else if("book".equals(parser.getName())){
                        bookinfo= new Bookinfo();
                        String idStr = parser.getAttributeValue(0);
                        bookinfo.setId(idStr);
                    }else if("title".equals(parser.getName())){
                        //parset.nextText()得到该tag节点中的内容
                        String title = parser.nextText();
                        bookinfo.setTitle(title);
                    }else if("price".equals(parser.getName())){
                        String price = parser.nextText();
                        bookinfo.setPrice(price);
                    }else if("time".equals(parser.getName())) {
                        String time = parser.nextText();
                        bookinfo.setTime( time );
                    }
                    break;
                //一个节点结束的标签
                case XmlPullParser.END_TAG:
                    //一个城市的信息处理完毕，city的结束标签
                    if("book".equals(parser.getName())){
                        bookinfos.add(bookinfo);
                        bookinfo = null;
                    }
                    break;
            }
            type = parser.next();
        }
        return bookinfos;
    }

    

}
