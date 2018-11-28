package com.example.administrator.myviewmove;

public class TestBigCircle {

    public static void main(String[] args) {

        //长半径
        double a=14.25;
        //短半径
        double b=9.5;

        //周长
        double l=0;
        l=2*Math.PI*b+4*(a-b);
        System.out.println(l);
    }
}
