package com.mmuu.travel.client.ui.other;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.ShareBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.tools.ShareUtils;
import com.mmuu.travel.client.ui.user.TripFeedbackDetailAct;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.IUiListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * webview 页面 带参数 url，tv_title
 *
 * @author Administrator
 */
@SuppressLint("SetJavaScriptEnabled")
public class CreditWebFrg extends MFBaseFragment implements OnClickListener {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.nav_layout)
    LinearLayout navLayout;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.v_padding)
    View vPadding;
    @BindView(R.id.iv_nodata)
    ImageView ivNodata;
    @BindView(R.id.tv_text_hint)
    TextView tvTextHint;
    @BindView(R.id.tv_text_hintbutton)
    TextView tvTextHintbutton;
    @BindView(R.id.rl_nonet)
    RelativeLayout rlNonet;
    private View webFrgView;
    private Intent intent;
    private String url = "";
    private String title = "";
    private long exitTime = 0;
    private ShareBean shareBean = null;
    private Bitmap bitmap;
    private Dialog dialog;
    private IUiListener iUiListener;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    finish();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (webFrgView == null) {
            webFrgView = inflater.inflate(R.layout.frg_creditwebview, null);
            ButterKnife.bind(this, webFrgView);
            initView();
            if (intent != null) {
                url = intent.getStringExtra("url");
                title = intent.getStringExtra("title");
            }
            titleTitle.setText(title);
//            if (NetHelper.checkNetwork(mContext)) {
//                showNoNetDlg();
//                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
//                return webFrgView;
//            }
//            dialogShow();
            if (MFUtil.checkNetwork(mContext)) {
                webView.loadUrl(url);
            } else {
                tvTextHintbutton.setOnClickListener(this);
                webView.setVisibility(View.GONE);
            }
        }
        ButterKnife.bind(this, webFrgView);
        return webFrgView;
    }

    public void setIUiListener(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
    }

    public void finish() {
        getActivity().finish();
    }

    private void initView() {
        navLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleRightText.setVisibility(View.VISIBLE);
        titleRightText.setText("抽奖记录");
        titleRightText.setOnClickListener(this);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                } else {
                finish();
//                }
            }
        });
        webView = (WebView) webFrgView.findViewById(R.id.webview);
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains(MFConstansValue.WEB_COULDNOTSTART)) {
                    Intent intent1 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("pagetype", MFConstansValue.FEELBACK_WEB_COULDNOTSTART);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);
                } else if (url.contains(MFConstansValue.WEB_CREDITRULES)) {
                    Intent intent2 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("pagetype", MFConstansValue.FEELBACK_WEB_NOTENDORDER);
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                } else if (url.contains(MFConstansValue.WEB_FOUNDCAR)) {
                    Intent intent3 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("pagetype", MFConstansValue.FEELBACK_WEB_NOTFINDCAR);
                    intent3.putExtras(bundle3);
                    startActivity(intent3);
                } else if (url.contains(MFConstansValue.WEB_NOTOPENLOCK)) {
                    Intent intent4 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("pagetype", MFConstansValue.FEELBACK_WEB_NOTLOCK);
                    intent4.putExtras(bundle4);
                    startActivity(intent4);
                } else if (url.contains(MFConstansValue.WEB_BREAK)) {
                    Intent intent5 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("pagetype", MFConstansValue.FEELBACK_MAIN_BREAK);
                    intent5.putExtras(bundle5);
                    startActivity(intent5);
                } else if (url.contains(MFConstansValue.WEB_ILLEGAL)) {
                    Intent intent6 = new Intent(mContext, TripFeedbackDetailAct.class);
                    Bundle bundle6 = new Bundle();
                    bundle6.putString("pagetype", MFConstansValue.FEELBACK_MAIN_STOPCAR);
                    intent6.putExtras(bundle6);
                    startActivity(intent6);
                } else if (url.contains(MFConstansValue.WEB_TEL)) {
                    Intent intent7 = new Intent(Intent.ACTION_DIAL, Uri
                            .parse(url));
                    intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent7);
                } else if (url.startsWith("http")) {
                    if (MFUtil.checkNetwork(mContext)) {
                        webView.loadUrl(url);// 在当前的webview中跳转到新的url
                    } else {
                        tvTextHintbutton.setOnClickListener(CreditWebFrg.this);
                        webView.setVisibility(View.GONE);
                    }
                } else {
                    return true;
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (-8 == errorCode) {
                    tvTextHintbutton.setOnClickListener(CreditWebFrg.this);
                    webView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                dismmisDialog();
            }

            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.cancel(); // Android默认的处理方式
//                handler.proceed(); // 接受所有网站的证书
                // handleMessage(Message msg); // 进行其他处理
            }
        };
        webView.setWebViewClient(client);
        WebSettings settings = webView.getSettings();
        settings.setRenderPriority(RenderPriority.HIGH); // 加速webview加载速度
        settings.setBlockNetworkImage(false);// 同上，图片放在最后加载
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//
        settings.setSavePassword(false);
        settings.setSupportZoom(false);//是否可以缩放，默认true
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setDomStorageEnabled(true);//开启DOM存储，部分网页不开启会显示空表页
        webView.removeJavascriptInterface("searchBoxJavaBridge_");//解除漏洞
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.removeJavascriptInterface("accessibility");
        webView.addJavascriptInterface(new JavaScriptinterface(),
                "mf_android");
        //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法
    }

    public class JavaScriptinterface {
        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void getShareMessage(String shareMsg) {
            RequestResultBean<ShareBean> shareBeanRequestResultBean = GsonTransformUtil.getObjectFromJson(shareMsg, new TypeToken<RequestResultBean<ShareBean>>() {
            }.getType());
            if (shareBeanRequestResultBean != null && shareBeanRequestResultBean.getCode() == MFConstansValue.BACK_SUCCESS) {
                shareBean = shareBeanRequestResultBean.getData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = ImageLoader.getInstance().loadImageSync((shareBean.getSharePic()));
                    }
                }).start();
            }
        }

        @JavascriptInterface
        public void shareClick() {
            if (shareBean != null) {
                showBottomDialog();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - exitTime) < 800) {
            return;
        }
        exitTime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.ll_shareqqspace:
                if (shareBean == null || iUiListener == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditwebpoint_shareqqspace");
                String urlQQspace = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToTencent(urlQQspace,
                        shareBean.getShareTitle(), shareBean.getShareDescription(), shareBean.getSharePic(), iUiListener);
                dialog.dismiss();
                break;
            case R.id.ll_shareweibo:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditwebpoint_shareweibo");
                String urlWeibo = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareWeiboMultiMessage("暂无附带内容", shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32),
                        urlWeibo, "InviteFriendFrg");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixin:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditwebpoint_shareweixin");
                String urlWeixin = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXin(urlWeixin, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "a");
                dialog.dismiss();
                break;
            case R.id.ll_shareweixincircle:
                if (shareBean == null || bitmap == null) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_creditwebpoint_shareweixincircle");
                String weixincircle = MFUtil.getValueUrl(shareBean.getShareUrl(),
                        "amount=" + shareBean.getAmount()
                        , "userId=" + MFStaticConstans.getUserBean().getUser().getId()
                );
                ShareUtils.getIntance(mContext).shareToWeiXinCircle(weixincircle, shareBean.getShareTitle(), shareBean.getShareDescription(), MFUtil.imageZoom(bitmap, 32), "a");
                dialog.dismiss();
                break;
            case R.id.title_right_text:
                MobclickAgent.onEvent(mContext, "click_draw_record");
                Bundle bundle = new Bundle();
                bundle.putString("url", MFUrl.DRAWRECORD + "?userId=" + MFStaticConstans.getUserBean().getUser().getId());
                bundle.putString("title", "抽奖记录");
                startActivity(WebAty.class, bundle);
                break;
            case R.id.tv_text_hintbutton:
                dialogShow();
                if (!MFUtil.checkNetwork(mContext)) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 120);
                    return;
                }
                webView.loadUrl(url);
                break;
            default:
                break;
        }
    }

    private void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgSharepoint = inflater.inflate(R.layout.dlg_sharerbottom, null);
            final int cFullFillWidth = 10000;
            mDlgSharepoint.setMinimumWidth(cFullFillWidth);
            TextView tv_titletxt = (TextView) mDlgSharepoint
                    .findViewById(R.id.tv_titletxt);
            tv_titletxt.setText("分享至");
            View v_line = (View) mDlgSharepoint
                    .findViewById(R.id.v_line);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                v_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            LinearLayout ll_shareweixin = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweixin);
            LinearLayout ll_shareweixincircle = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweixincircle);
            LinearLayout ll_shareweibo = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareweibo);
            LinearLayout ll_shareqqspace = (LinearLayout) mDlgSharepoint
                    .findViewById(R.id.ll_shareqqspace);
            ll_shareweixin.setOnClickListener(this);
            ll_shareweixincircle.setOnClickListener(this);
            ll_shareweibo.setOnClickListener(this);
            ll_shareqqspace.setOnClickListener(this);
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgSharepoint);
        }
        dialog.show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public boolean onKeyDown() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }
}
