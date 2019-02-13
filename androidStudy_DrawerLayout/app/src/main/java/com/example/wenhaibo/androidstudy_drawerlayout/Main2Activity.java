package com.example.wenhaibo.androidstudy_drawerlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private Banner banner;
    private RecyclerView recyclerView;
    private List<Integer> images=new ArrayList<>(  );
    private  List<String>titles=new ArrayList<>(  );
    private List<News>mnews1=new ArrayList<>(  );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        banner=findViewById( R.id.banner );
        recyclerView=findViewById( R.id.recycler_2 );

        //组装数据
        images.add( R.drawable.first );
        images.add( R.drawable.second );
        images.add( R.drawable.third);

        titles.add( "这是事件一" );
        titles.add( "这是事件二" );
        titles.add( "这是事件三" );

        banner  .setImages( images)
                .setBannerStyle( BannerConfig.CIRCLE_INDICATOR_TITLE )
                .setBannerTitles( titles )
                .setDelayTime( 2000 )
                .isAutoPlay( true )
                .setBannerAnimation( Transformer.Tablet )
                .setImageLoader( new GlideImageLoader() ).start();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );
        for (int i=0;i<3;i++){
            News []news={new News( titles.get( i),titles.get( i) ),
                    new News( titles.get( i),titles.get( i ) ),
                    new News( titles.get( i ),titles.get( i ) )};
            mnews1.add( news[i] );

        }
        NewsAdapter newsAdapter=new NewsAdapter( mnews1 );
        recyclerView.setAdapter( newsAdapter );

    }
}
