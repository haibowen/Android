package com.mmuu.travel.client.ui.other;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFApp;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.ui.user.LoginAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JGW 设置frg
 */
public class AdviseFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface, TextWatcher {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.ed_advisecontent)
    EditText edAdvisecontent;
    @BindView(R.id.tv_submitcontent)
    TextView tvSubmitcontent;
    @BindView(R.id.tv_textnumber)
    TextView tvTextnumber;
    private View adviseView;
    private static final int TAG_SUBMMIT = 1695701;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (adviseView == null) {
            adviseView = inflater.inflate(R.layout.frg_advise, null);
            ButterKnife.bind(this, adviseView);
            initView();
        }
        return adviseView;
    }

    private void submitData() {
        if (!MFUtil.checkNetwork(mContext)) {
            showNoNetworkDialog(true);
            return;
        }
        dialogShow();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        params.addBodyParameter("content", edAdvisecontent.getText() + "");
        if (MFApp.getInstance().getmLocation() != null) {
            params.addBodyParameter("userGps", MFApp.getInstance().getmLocation().getLongitude() + "," + MFApp.getInstance().getmLocation().getLatitude());
        }
        MFRunner.requestPost(TAG_SUBMMIT, MFUrl.SUGGESTION, params, this);

    }

    private void initView() {
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText("意见建议");
        tvSubmitcontent.setOnClickListener(this);
        edAdvisecontent.addTextChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submitcontent:
                if (TextUtils.isEmpty(edAdvisecontent.getText().toString().trim())) {
                    MFUtil.showToast(mContext, "请添加您的建议或意见");
                    return;
                }
                if (edAdvisecontent.getText().toString().trim().length() < 5) {
                    MFUtil.showToast(mContext, "客官再多给点建议呗，我们会改哒");
                    return;
                }
                submitData();
                break;
            case R.id.title_left_image:
                mContext.finish();
                break;
        }
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {

    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        switch (i) {
            case TAG_SUBMMIT:
                if (!isAdded()) {
                    return;
                }
                dismmisDialog();
                String stringjson = responseInfo.result.toString();
                RequestResultBean requestResultBean = GsonTransformUtil.getObjectFromJson(stringjson, new TypeToken<RequestResultBean>() {
                }.getType());
                if (requestResultBean != null && MFConstansValue.BACK_SUCCESS == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    mContext.finish();
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();
                } else if (requestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == requestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == requestResultBean.getCode())) {
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // 输入表情前EditText中的文本
    private String inputAfterText;
    // 是否重置了EditText的内容
    private boolean resetText;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!resetText) {
// 这里用s.toString()而不直接用s是因为如果用s，
// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
// inputAfterText也就改变了，那么表情过滤就失败了
            inputAfterText = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!resetText) {
            if (count >= 2) {// 表情符号的字符长度最小为2
//                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                CharSequence input = s.subSequence(start, start + count <= s.length() ? start + count : s.length());
                if (containsEmoji(input.toString())) {
                    resetText = true;
                    Toast.makeText(mContext, "不支持输入表情符号！", Toast.LENGTH_SHORT).show();
                    // 是表情符号就将文本还原为输入表情符号之前的内容
                    edAdvisecontent.setText(inputAfterText);
                    CharSequence text = edAdvisecontent.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                }
            }
        } else {
            resetText = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        tvTextnumber.setText(s.toString().length() + "/500字");
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}