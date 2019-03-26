package javatel;

import interfacedef.Sell;

public class Vender implements Sell {


    @Override
    public void sell() {

        System.out.println("the method is sell");

    }

    @Override
    public void ad() {

        System.out.println("the method is ad");



    }
}
