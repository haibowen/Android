package com.example.administrator.mycontext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.xml.sax.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

public class MyDataBase extends SQLiteOpenHelper {
    private Context mcontext;
    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mcontext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL();
        //建表的语句

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void parseXMLWithPull(String xmldata){
        try {

            XmlPullParserFactory factory =XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType=xmlPullParser.getEventType();
            String id="";
            String name="";
            String version="";
            while (eventType!=XmlPullParser.END_DOCUMENT){
                String nodename=xmlPullParser.getName();
                switch (eventType){
                    //开始解析某个节点

                    case  XmlPullParser.START_TAG:{
                        if ("id".equals(nodename)){
                            id=xmlPullParser.nextText();
                        }else if ("name".equals(nodename)){
                            name=xmlPullParser.nextText();
                        }else  if ("version".equals(nodename)){
                            version=xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodename)){
                            //打印
                        }
                        break;
                        default:
                            break;
                }
                eventType=xmlPullParser.next();

            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    private void  parseXMLWithSAX(String xmlData) {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xmlData)));

        }catch (Exception e){
            e.printStackTrace();

        }



    }
}
