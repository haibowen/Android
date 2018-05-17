package java.bean;

public class Fruit {

    //声明属性

    private String name;
    private  int imageId;

    //构造方法

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }


    //get set 方法


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
