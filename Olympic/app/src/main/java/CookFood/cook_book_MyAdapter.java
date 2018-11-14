package CookFood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import com.example.wenhaibo.olympic.R;


public class cook_book_MyAdapter extends BaseAdapter {
    private int m = 15;
    private ArrayList<String> url;
    private ArrayList<String> step;
    private Context context;

    public cook_book_MyAdapter(Context context) {
        this.context = context;
    }

    public cook_book_MyAdapter(Context context, cook_book_Person list_s_i) {
        this.context = context;
        url = list_s_i.getList_image_url();
        step = list_s_i.getList_step();

    }


    @Override
    public int getCount() {
        return m;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ResourceAsColor", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.cookbook_item, null);
        ImageView iv_image_show = (ImageView) view.findViewById(R.id.iv_image);
        TextView tv_image_show = (TextView) view.findViewById(R.id.tv_image);
        //正好步骤和图片数相等
        if (url != null && step != null) {
            m = url.size();
            String getimage = url.get(i);
            String getstep = step.get(i) + "\n";
            Log.e("gaoyu", "步骤" + getstep + "\n" + "图片链接" + getimage);
            //显示图片
            Picasso.with(context).load(getimage).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).resize(200, 200).into(iv_image_show);
            //显示步骤
            tv_image_show.setText(getstep);
            tv_image_show.setTextColor(R.color.black);
        }
        return view;
    }
}
