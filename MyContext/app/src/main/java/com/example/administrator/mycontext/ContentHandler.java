package com.example.administrator.mycontext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandler extends DefaultHandler {
    private String nodename;
    private StringBuilder id;
    private StringBuilder name;
    private StringBuilder version;



    @Override
    public void startDocument() throws SAXException {
        id=new StringBuilder();
        name=new StringBuilder();
        version=new StringBuilder();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodename=localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if ("id".equals(nodename)){
            id.append(ch,start,length);
        }else if ("name".equals(nodename)){
            name.append(ch,start,length);
        }else if ("version".equals(nodename)){
            version.append(ch,start,length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("app".equals(localName)){
            //打印
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
