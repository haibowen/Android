package db.itheima.com.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAdd ,btSearch;
    private EditText etSearch;
    private List<Bean> beans;
    private Dao d;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d=new Dao(this);
        beans=d.findAll();
        btSearch=(Button)findViewById(R.id.btSearch);
        etSearch=(EditText)findViewById(R.id.etSearch);
        btAdd=(Button)findViewById(R.id.btAdd);
        lv=(ListView)findViewById(R.id.lv);
        Log.d(TAG, "onCreate: hahhhahahhahah");
        btAdd.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        lv.setAdapter(new MyAdapter());
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> Arg0, View view, final int position, long arg3) {
                int id_1=beans.get(position).getId();
                d.delete(id_1);
                beans=d.findAll();
                lv.setAdapter(new MyAdapter());
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long position) {int id =beans.get(item).getId();
                Intent intent=new Intent(MainActivity.this,ItemActivity.class);
                intent.putExtra("item",String.valueOf(id));
                startActivity(intent);
                finish();

            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,ContentActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String str =etSearch.getText().toString();
        Log.e("wen","只1"+str);

        switch (v.getId()){
            case R.id.btSearch:
                if (str !=null) {
                    Intent i=new Intent(MainActivity.this,ItemActivity.class);
                    i.putExtra("Search",str);
                    startActivity(i);

                }
                else {
                    Toast.makeText(MainActivity.this,"Nothing",Toast.LENGTH_SHORT).show();
                }
        }
    }
    /*
    适配器
     */

    private  class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {return beans.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Bean bean=beans.get(position);
            View view= View.inflate(MainActivity.this,R.layout.list_item,null);
            TextView tvContent= (TextView)view.findViewById(R.id.tvContent);
            tvContent.setText("内容:"+bean.getContent());
            TextView tvTittle=(TextView) view.findViewById(R.id.tvTittle);
            tvTittle.setText("标题:"+bean.getName());
            return view;

        }
    }

    @Override
    protected void onResume() {
        beans=d.findAll();
        lv.setAdapter(new MyAdapter());

        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        switch (id){
            case  R.id.action_add:
                Intent i=new Intent(MainActivity.this,ContentActivity.class);
                startActivity(i);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
