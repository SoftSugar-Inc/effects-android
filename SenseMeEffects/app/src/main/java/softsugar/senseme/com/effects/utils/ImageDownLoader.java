package softsugar.senseme.com.effects.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageDownLoader {
    private static final String TAG = "ImageDownloader";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Context context;
    private OkHttpClient client;
    private ImageDownloadListener listener;

    public interface ImageDownloadListener {
        void onDownloadSuccess(String savedImagePath);

        void onDownloadFailure(String errorMessage);
    }

    public ImageDownLoader(Context context) {
        this.context = context;
        client = new OkHttpClient();
    }

    public void downloadImage(String imageUrl, ImageDownloadListener listener) {
        this.listener = listener;
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to download image: " + e.getMessage());
                handler.post(() -> listener.onDownloadFailure("error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download image: " + response);
                }

                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    inputStream = response.body().byteStream();

                    // 将图片保存到相册
                    saveImageToGallery(BitmapFactory.decodeStream(inputStream));
                } catch (Exception e) {
                    Log.e(TAG, "Exception occurred while downloading image: " + e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void saveImageToGallery(Bitmap bitmap) {
        //File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG);
        File file = FileUtils.getOutputMediaFile();
        saveToSDCard(FileUtils.getOutputMediaFile(), bitmap);

        //String savedImageURL = file.getAbsolutePath();
        //Log.i(TAG, "savedImageURL=" + file.getAbsolutePath());

        // 发送广播通知相册更新
        /*
        context.sendBroadcast(new android.content.Intent(
                android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                android.net.Uri.parse(savedImageURL)));
        handler.post(() -> listener.onDownloadSuccess(savedImageURL));

         */
        handler.post(() -> listener.onDownloadSuccess(file.getAbsolutePath()));
    }

    protected void saveToSDCard(File file, Bitmap bmp) {

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        String path = file.getAbsolutePath();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);

        if (Build.VERSION.SDK_INT >= 19) {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
        }
    }
}
