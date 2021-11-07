package com.mosi.update.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mosi.update.interfaces.AppDownloadListener;
import com.mosi.update.model.DownloadInfo;
import com.mosi.update.utils.ResUtils;
import com.mosi.update.utils.RootActivity;
import com.mosi.update.R;


/**
 * 类似虎牙直播的更新样式
 */
public class UpdateType2Activity extends RootActivity {

    private TextView tvMsg;
    private TextView tvBtn2;
    private ImageView ivClose;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_type2);

        findView();

        setDataAndListener();
    }

    private void setDataAndListener() {
        tvMsg.setText(downloadInfo.getUpdateLog());
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvVersion.setText("v" + downloadInfo.getProdVersionName());

        if (downloadInfo.isForceUpdateFlag()) {
            ivClose.setVisibility(View.GONE);
        } else {
            ivClose.setVisibility(View.VISIBLE);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //右边的按钮
                download();
            }
        });
    }

    private void findView() {
        tvMsg = findViewById(R.id.tv_content);
        tvBtn2 = findViewById(R.id.tv_update);
        ivClose = findViewById(R.id.iv_close);
        tvVersion = findViewById(R.id.tv_version);
    }

    @Override
    public AppDownloadListener obtainDownloadListener() {
        return new AppDownloadListener() {
            @Override
            public void downloading(int progress) {
                tvBtn2.setText(ResUtils.getString(R.string.downloading) + progress + "%");
            }

            @Override
            public void downloadFail(String msg) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
                Toast.makeText(UpdateType2Activity.this, ResUtils.getString(R.string.apk_file_download_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void downloadComplete(String path) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
            }

            @Override
            public void downloadStart() {
                tvBtn2.setText(ResUtils.getString(R.string.downloading));
            }

            @Override
            public void reDownload() {
                Log.e("gjt", "下载失败后点击重试");
            }

            @Override
            public void pause() {

            }
        };
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param info
     */
    public static void launch(Context context, DownloadInfo info) {
        launchActivity(context, info, UpdateType2Activity.class);
    }

}
