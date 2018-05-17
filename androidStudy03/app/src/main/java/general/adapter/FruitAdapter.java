package general.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.androidstudy03.R;

import java.bean.Fruit;
import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private  int resouseid;

    public FruitAdapter(@NonNull Context context, int resource, @NonNull List<Fruit> objects) {
        super( context, resource,  objects );
        resouseid=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //获取当前项的fruit实例

        Fruit fruit=getItem( position );

        //绑定布局

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
             view= LayoutInflater.from( getContext() ).inflate( resouseid,parent,false );
             viewHolder=new ViewHolder();

             //绑定id

             viewHolder.fruitImage=view.findViewById( R.id.Image_list_item );
             viewHolder.fruitText=view.findViewById( R.id.Tv_list_item );

             //将viewholder存储在view中

            view.setTag( viewHolder );

        }else{

            view=convertView;

            //重新获取viewholder
            viewHolder= (ViewHolder) view.getTag();
        }


        //设置显示的内容

       viewHolder.fruitImage.setImageResource( fruit.getImageId() );
        viewHolder.fruitText.setText( fruit.getName() );

        return  view;
    }


    private class ViewHolder {
        ImageView fruitImage;
        TextView fruitText;
    }
}
