package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.R;


public class FlowerActivity extends AppCompatActivity {
    public  static  final  String FLOWER_NAME="flower_name";
    public  static  final  String FLOWER_IMAGE_ID="flower_image_id";
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);
        Intent intent=getIntent();
        String FlowerName=intent.getStringExtra(FLOWER_NAME);
        int FlowerImagerId=intent.getIntExtra(FLOWER_IMAGE_ID,0);

        toolbar=findViewById(R.id.toobal_head);
        collapsingToolbarLayout=findViewById(R.id.collaps);
        imageView=findViewById(R.id.image_toobar);
        textView=findViewById(R.id.text_header);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout.setTitle(FlowerName);

        Glide.with(this).load(FlowerImagerId).into(imageView);
        String FlowerContent=generateFlowerContent(FlowerName);
        textView.setText(FlowerContent);











    }

    private String generateFlowerContent(String flowername) {
        StringBuilder FlowerContent=new StringBuilder();
        for (int i=0;i<500;i++){
            FlowerContent.append(flowername);

        }
        return FlowerContent.toString();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
