package com.mmuu.travel.client.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.http.RequestParams;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.bean.mfinterface.DialogClickListener;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.PermissionHelper;
import com.mmuu.travel.client.tools.SharedPreferenceTool;
import com.mmuu.travel.client.tools.TimeDateUtil;
import com.mmuu.travel.client.ui.other.StartAct;
import com.mmuu.travel.client.widget.dialog.MFProgresssDialog;
import com.mmuu.travel.client.widget.dialog.NoNetworkDialog;
import com.mmuu.travel.client.widget.dialog.OneDialog;

import java.util.ArrayList;
import java.util.List;


public class MFBaseFragment extends Fragment {
    public Activity mContext;
    private Handler baseHandler;
    protected View fragContentView;
    private MFProgresssDialog mfProgresssDialog;
    public NoNetworkDialog noNetworkDialog;
    private PermissionHelper permissionHelper;
    private OneDialog outServiceTimeDialog;


    @Override
    public void onAttach(Activity activity) {
        initDialog();
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        initHandler();
        super.onCreate(savedInstanceState);
    }

    
}