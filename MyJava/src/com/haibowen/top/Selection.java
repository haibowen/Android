package com.haibowen.top;

public class Selection implements Type {
    @Override
    public void sort(int [] a) {
        for (int i = 0; i <a.length ; i++) {
            int m=i;
            for (int j = i+1; j <a.length ; j++) {
                if (a[j]<a[i]){
                    m=a[j];
                    a[j]=a[i];
                    a[i]=m;
                }
            }

        }
    }
}
