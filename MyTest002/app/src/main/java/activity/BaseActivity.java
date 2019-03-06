package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import util.ActivityControler;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getSimpleName() );

        ActivityControler.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityControler.removeActivity(this);
    }
}
