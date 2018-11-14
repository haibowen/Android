package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.myapplication.R;
import util.Flower;

import java.util.List;

public class FlowerAdapterListView extends ArrayAdapter<Flower> {

private int resourceId;

    public FlowerAdapterListView(@NonNull Context context, int resource, @NonNull List<Flower> objects) {
        super(context, resource, objects);

        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Flower flower=getItem(position);
        /**
        View view =LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView=view.findViewById(R.id.image_listvie);
        TextView textView=view.findViewById(R.id.tx_listvie);
        imageView.setImageResource(flower.getImageid());
        textView.setText(flower.getName());
        return view;
         **/




          View view;
          ViewHolder viewHolder;
          if (convertView==null){
              view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
              viewHolder=new ViewHolder();
              viewHolder.imageView=view.findViewById(R.id.image_listvie);
              viewHolder.textView=view.findViewById(R.id.tx_listvie);
              view.setTag(viewHolder);
          }else {
              view=convertView;
              viewHolder= (ViewHolder) view.getTag();
          }
      viewHolder.imageView.setImageResource(flower.getImageid());
      viewHolder.textView.setText(flower.getName());
         return  view;

    }
    class ViewHolder{

         ImageView imageView;
         TextView textView;
    }
}
