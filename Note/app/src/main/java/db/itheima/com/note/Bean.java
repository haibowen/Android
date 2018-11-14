package db.itheima.com.note;

/**
 * Created by wenhaibo on 2017/3/9.
 */
public class Bean {

    private int id;
    private String name;
    private String content;
    //构造方法
    public Bean(String content, String name){
        this.content=content;
        this.name=name;
    }
    public Bean(){

    }

    public Bean(int id, String name, String content) {
        this.name = name;
        this.id = id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{ " +
                "id=" + id +"}";
    }
}
