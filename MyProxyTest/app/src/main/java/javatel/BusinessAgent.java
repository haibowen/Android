package javatel;

import interfacedef.Sell;

public class BusinessAgent implements Sell {

    private Sell vendor;

    public BusinessAgent(Sell vendor) {

        this.vendor=vendor;



    }

    @Override
    public void sell() {

       vendor.sell();

    }

    @Override
    public void ad() {


       vendor.ad();
    }

}
