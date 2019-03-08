package com.example.myretrofit;

import android.widget.TextView;



public class Traslation {



//    @BindView(R.id.text_status) TextView textViewstatus;
//    @BindView(R.id.text_content) TextView textViewcontent;
//    @BindView(R.id.text_for) TextView textViewfor;
//    @BindView(R.id.text_to) TextView textViewto;
//    @BindView(R.id.text_vender) TextView textViewvender;

    private String status;

    private content content;
    private static class content{

        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;

    }

    public String show(){

        String a=status;


        //System.out.println(a);
//       textViewstatus.setText(status);
//       textViewcontent.setText(content.out);
//       textViewfor.setText(content.from);
//       textViewvender.setText(content.vendor);
//       textViewto.setText(content.errNo);


        return a;
    }

    public String show1(){

        String a=content.from;

        return a;
    }

}
