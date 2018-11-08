package com.haibowen.top;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {

        int [] b={ 2,3,1,5,3,78,84,34,56};
        int []c={23,45,67,89,21,13,24,678,111};
        Bubersort bubersort=new Bubersort();
        bubersort.sort(b);
        System.out.println(Arrays.toString(b));
        Selection selection=new Selection();
        selection.sort(c);
        System.out.println(Arrays.toString(c));



    }
}
