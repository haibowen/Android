package activity;

import adapter.FlowerAdapterListView;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.administrator.myapplication.R;
import util.Flower;

import java.util.ArrayList;
import java.util.List;


public class Main4Activity extends AppCompatActivity {
    private ListView listView;
    private Toolbar toolbar;
    private FlowerAdapterListView flowerAdapterListView;
    // private String[] data={"haibowen.top","shangxuetang.com","heuxnhulian.com","renjuhuitong.com"};
    // private List<String> data1=new ArrayList<String>();
    private List<Flower> flowers = new ArrayList<>();
    private Flower[] flowerssource = {new Flower("梅花", R.drawable.one), new Flower("兰花", R.drawable.two), new Flower("火龙果", R.drawable.three)};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ininFlowers();
        toolbar = findViewById(R.id.toobar_four);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.lv);
        flowerAdapterListView = new FlowerAdapterListView(Main4Activity.this, R.layout.layout_item_1, flowers);
        listView.setAdapter(flowerAdapterListView);

        /**
         for (int i=0;i<50;i++){
         Random random=new Random();
         int index=random.nextInt(data.length);
         data1.add(data[index]);
         }

         ArrayAdapter <String>adapter=new ArrayAdapter<String>(Main4Activity.this,android.R.layout.simple_list_item_1,data1);
         listView.setAdapter(adapter);
         **/

        //listview的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Flower flower=flowers.get(position);
                Snackbar.make(listView,"我被点击了",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void ininFlowers() {

        /**  for (int i=0;i<500000;i++){

         Random random=new Random();
         int index=random.nextInt(flowerssource.length);
         flowers.add(flowerssource[index]);

         }
         **/


        for (int i = 0; i < 2000; i++) {
          Flower fl=  new Flower("梅花", R.drawable.one);
           flowers.add(fl);
           Flower f2= new Flower("兰花", R.drawable.two);
           flowers.add(f2);
           Flower f3= new Flower("火龙果", R.drawable.three);
           flowers.add(f3);

        }
    }
}
