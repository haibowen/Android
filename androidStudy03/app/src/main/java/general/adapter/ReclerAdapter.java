package general.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenhaibo.androidstudy03.R;

import java.bean.Fruit;
import java.util.List;

public class ReclerAdapter extends RecyclerView.Adapter<ReclerAdapter.ViewHolder> {
    private List<Fruit> fruitList;



    static class ViewHolder extends RecyclerView.ViewHolder {

        View fruitview;
        ImageView FruitImage;
        TextView FruitName;

        public ViewHolder(View itemView) {
            super( itemView );

            fruitview=itemView;

            //绑定id

            FruitImage=itemView.findViewById( R.id.Image_list_item );
            FruitName=itemView.findViewById( R.id.Tv_list_item );


        }
    }

    public ReclerAdapter(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_item_staggerdgrid,parent,false );
      final   ViewHolder holder=new ViewHolder( view );

        //点击事件在这里注册   大布局view的点击事件

        holder.fruitview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion=holder.getAdapterPosition();
                Fruit fruit=fruitList.get( postion );
                Toast.makeText( view.getContext(),"hello",Toast.LENGTH_SHORT ).show();

            }
        } );

        //图片的点击事件

        holder.FruitImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion =holder.getAdapterPosition();
                Fruit fruit=fruitList.get( postion );
                Toast.makeText( view.getContext(),"我是图片",Toast.LENGTH_SHORT ).show();

            }
        } );

        //文字的点击事件注册

        holder.FruitName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion=holder.getAdapterPosition();
                Fruit fruit= fruitList.get( postion );
                Toast.makeText( view.getContext(),"我是文字",Toast.LENGTH_SHORT ).show();
                
            }
        } );



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fruit fruit=fruitList.get( position );
        holder.FruitImage.setImageResource( fruit.getImageId() );
        holder.FruitName.setText( fruit.getName() );


    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }


}
