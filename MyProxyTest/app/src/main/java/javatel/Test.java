package javatel;

import interfacedef.Sell;

public class Test {




    public static void main(String[] args) {
        //委托类
        Vender vender=new Vender();
        //代理类

        BusinessAgent businessAgent=new BusinessAgent(vender);

        BusinessAgent businessAgent1=new BusinessAgent(new Sell() {
            @Override
            public void sell() {



            }

            @Override
            public void ad() {

            }
        });

        businessAgent.sell();
        businessAgent.ad();






    }
}
