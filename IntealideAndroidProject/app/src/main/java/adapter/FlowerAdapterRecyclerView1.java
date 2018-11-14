package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.myapplication.R;
import util.Flower;

import java.util.List;

public class FlowerAdapterRecyclerView1 extends RecyclerView.Adapter<FlowerAdapterRecyclerView1.ViewHolder> {

  private List<Flower> flowerList;



    static  class ViewHolder extends  RecyclerView.ViewHolder{

        View FLowerView;
        ImageView imageView;
        TextView textView;
         ViewHolder(View view){
            super(view);
            FLowerView=view;
            imageView=view.findViewById(R.id.image_listvie);
            textView=view.findViewById(R.id.tx_listvie);

        }
    }

    public FlowerAdapterRecyclerView1(List<Flower> flowers) {

       flowerList=flowers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_1,parent,false);
       final ViewHolder holder=new ViewHolder(view);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int postion =holder.getAdapterPosition();
               Flower flower=flowerList.get(postion);
               Toast.makeText(v.getContext(),"ok"+flower.getName(),Toast.LENGTH_SHORT).show();
           }
       });
       holder.textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int postion=holder.getAdapterPosition();
               Flower flower=flowerList.get(postion);
               Toast.makeText(v.getContext(),"no"+flower.getName(),Toast.LENGTH_SHORT).show();

           }
       });
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Flower flower=flowerList.get(position);
        holder.imageView.setImageResource(flower.getImageid());
        holder.textView.setText(flower.getName());

    }



    @Override
    public int getItemCount() {
        return flowerList.size();
    }
}
