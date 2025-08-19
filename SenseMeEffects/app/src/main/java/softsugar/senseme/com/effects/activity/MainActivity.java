package softsugar.senseme.com.effects.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import softsugar.senseme.com.effects.R;
import softsugar.senseme.com.effects.STLicenseUtils;
import softsugar.senseme.com.effects.databinding.ActivityMainBinding;
import softsugar.senseme.com.effects.state.AtyStateContext;
import softsugar.senseme.com.effects.state.CameraAtyState;
import softsugar.senseme.com.effects.state.ImgAtyState;
import softsugar.senseme.com.effects.state.VideoAtyState;
import softsugar.senseme.com.effects.utils.Constants;
import softsugar.senseme.com.effects.utils.ContextHolder;
import softsugar.senseme.com.effects.utils.DeviceUtils;
import softsugar.senseme.com.effects.utils.FileUtils;
import softsugar.senseme.com.effects.utils.GlideEngine;
import softsugar.senseme.com.effects.utils.MultiLanguageUtils;
import softsugar.senseme.com.effects.utils.NetworkUtils;
import softsugar.senseme.com.effects.utils.ToastUtils;
import softsugar.senseme.com.effects.view.widget.LoadingDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 100;

    private ActivityMainBinding mBinding;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));

        setContentView(mBinding.getRoot());
        mBinding.tvVersion.setText("V" + DeviceUtils.INSTANCE.getAppVersionName());
        MultiLanguageUtils.initLanguageConfig();

        requestNecessaryPermissions();

        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShort(MultiLanguageUtils.getStr(R.string.toast_net_error));
        }

        bindEvent();
    }

    /**
     * 请求必要的权限
     */
    private void requestNecessaryPermissions() {
        String[] permissions = getRequiredPermissions();

        PermissionUtils.permission(permissions)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> granted) {
                        LogUtils.iTag(TAG, "Permissions granted: " + granted.toString());
                        applySuccess();
                    }

                    @Override
                    public void onDenied(List<String> deniedForever, List<String> denied) {
                        handlePermissionDenied(deniedForever, denied);
                    }
                })
                .request();
    }

    /**
     * 获取需要的权限列表，根据Android版本适配
     */
    private String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8+ (API 26+)
            return new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        } else {
            // Android 8以下版本
            return new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            };
        }
    }

    /**
     * 处理权限被拒绝的情况
     */
    private void handlePermissionDenied(List<String> deniedForever, List<String> denied) {
        LogUtils.wTag(TAG, "Permissions denied forever: " + deniedForever.toString());
        LogUtils.wTag(TAG, "Permissions denied: " + denied.toString());
        // 权限被拒绝，仅记录日志，不做额外处理
    }

    private void bindEvent() {
        // 预览模式
        mBinding.ivTakePic.setOnClickListener(v -> {
            AtyStateContext.getInstance().setState(new CameraAtyState());
            BaseActivity.actionStart(this, null);
        });

        // 视频模式
        mBinding.ivVideo.setOnClickListener(v -> {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofVideo())
                    .isDisplayCamera(false)
                    .setImageEngine(GlideEngine.createGlideEngine()).setSelectionMode(SelectModeConfig.SINGLE)
                    .setCameraVideoFormat(PictureMimeType.MP4)
                    .setFilterVideoMaxSecond(60)
                    .setFilterVideoMinSecond(3)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        });
    }

    // 鉴权
    private Observable<Boolean> observableCheckLicense() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                boolean checkLicense = STLicenseUtils.checkLicense(ContextHolder.getContext());
                ContextHolder.setCheckLicenseSuccess(checkLicense);
                emitter.onNext(checkLicense);
                emitter.onComplete();
            }
        });
    }

    // 拷贝素材
    private Observable<Boolean> observableCopyAssets() {
        return Observable.create(emitter -> {
            FileUtils.copyFilesFromAssets(ContextHolder.getContext(), "local_stickers", Objects.requireNonNull(MainActivity.this.getExternalFilesDir(null)).getAbsoluteFile() + File.separator + "local_stickers");
            FileUtils.copyModelsFiles(ContextHolder.getContext(), "models");

            FileUtils.copyStickerFiles(ContextHolder.getContext(), Constants.ASSET_FILTER_PORTRAIT);
            FileUtils.copyStickerFiles(ContextHolder.getContext(), Constants.ASSET_FILTER_TEXTURE);
            FileUtils.copyStickerFiles(ContextHolder.getContext(), Constants.ASSET_FILTER_FILM);
            FileUtils.copyStickerFiles(ContextHolder.getContext(), Constants.ASSET_FILTER_VINTAGE);

            FileUtils.copyStickerFiles(ContextHolder.getContext(), "whiten_assets");
            emitter.onNext(true);
            emitter.onComplete();
        });
    }

    public void applySuccess() {
        LoadingDialog dialog = new LoadingDialog(this);
        dialog.show();

        compositeDisposable.add(observableCopyAssets()
                .flatMap(ret -> observableCheckLicense())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Toast.makeText(ContextHolder.getContext(), MultiLanguageUtils.getStr(R.string.toast_error_license), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                    if (!result.isEmpty()) {
                        LocalMedia localMedia = result.get(0);
                        if (PictureMimeType.isHasImage(localMedia.getMimeType())) {
                            LogUtils.iTag(TAG, "onActivityResult: " + localMedia);

                            AtyStateContext.getInstance().setState(new ImgAtyState());
                            LogUtils.iTag(TAG, "onActivityResult:" + AtyStateContext.getInstance().getState() + " ");
                            ImageActivity.actionStart(this, localMedia.getRealPath());
                        }
                        if ("video/mp4".equals(localMedia.getMimeType())) {
                            AtyStateContext.getInstance().setState(new VideoAtyState());
                            LogUtils.iTag(TAG, "onActivityResult video path: " + localMedia.getRealPath());
                            VideoActivity.actionStart(this, localMedia.getRealPath());
                        }
                    }
                    LogUtils.iTag(TAG, "onActivityResult: " + result);
                    break;

                case REQUEST_CODE:
                    break;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/storage/emulated/0/Movies/SenseMeEffects"))));
        MediaScannerConnection.scanFile(this, new String[]{"/storage/emulated/0/DCIM/Camera"}, null, (path, uri) -> {
            LogUtils.iTag(TAG, "insert onScanCompleted path  uri " + uri);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}