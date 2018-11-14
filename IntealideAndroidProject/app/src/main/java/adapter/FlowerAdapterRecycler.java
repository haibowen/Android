package adapter;

import activity.FlowerActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.myapplication.R;
import com.squareup.picasso.Picasso;
import util.Flower;

import java.util.List;

public class FlowerAdapterRecycler extends RecyclerView.Adapter<FlowerAdapterRecycler.ViewHolder> {

    private Context context;
    private List<Flower> mlist;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.tvname);

        }
    }

    public FlowerAdapterRecycler(List<Flower> list) {
        mlist = list;


    }

    @Override
    public FlowerAdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        final   ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion =holder.getAdapterPosition();
                Flower flower=mlist.get(postion);
                Intent intent=new Intent(context, FlowerActivity.class);
                intent.putExtra(FlowerActivity.FLOWER_NAME,flower.getName());
                intent.putExtra(FlowerActivity.FLOWER_IMAGE_ID,flower.getImageid());
                 context.startActivity(intent);
            }
        });
       // return new ViewHolder(view);
         return holder;
    }

    @Override
    public void onBindViewHolder(FlowerAdapterRecycler.ViewHolder holder, int position) {

        Flower flower = mlist.get(position);
        holder.textView.setText(flower.getName());
        Picasso.with(context).load(flower.getImageid()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


}
