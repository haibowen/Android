package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySqlDatabase  extends SQLiteOpenHelper {

    private Context mcontext;

    public  static final String CREATE_BOOK="create table book("
            +"id integer primary key autoincrement,"
            +"author text,"
            +"price real,"
            +"pages integer,"
            +"name text)";



    public MySqlDatabase( Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mcontext=context;



    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BOOK);
        Toast.makeText(mcontext,"Succed",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
