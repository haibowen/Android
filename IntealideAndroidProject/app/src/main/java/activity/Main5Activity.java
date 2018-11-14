package activity;

import adapter.FlowerAdapterRecyclerView1;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.*;
import com.example.administrator.myapplication.R;
import util.BaseActivity;
import util.Flower;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main5Activity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Flower> flowers=new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main5);
        toolbar=findViewById(R.id.tool_five);
        setSupportActionBar(toolbar);

        initFlowers();
        recyclerView=findViewById(R.id.recylerview_five);



        //LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        FlowerAdapterRecyclerView1 flowerAdapterRecyclerView1=new FlowerAdapterRecyclerView1(flowers);
        recyclerView.setAdapter(flowerAdapterRecyclerView1);





    }

 public void   initFlowers(){

     for (int i = 0; i < 2000; i++) {

         Flower fl=  new Flower( getRandomLengthName("梅花"), R.drawable.one);
         flowers.add(fl);
         Flower f2= new Flower(getRandomLengthName("兰花"), R.drawable.two);
         flowers.add(f2);
         Flower f3= new Flower(getRandomLengthName("火龙果"), R.drawable.three);
         flowers.add(f3);

     }


    }

    public String getRandomLengthName(String randomLengthName) {

        Random random=new Random();
        int length=random.nextInt(20)+1;
        StringBuilder builder=new StringBuilder();
        for (int i=0;i<length;i++){
            builder.append(randomLengthName);
        }
        return builder.toString();
    }
}
