package com.softsugar.stmobile.engine.glutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Environment;

import com.softsugar.stmobile.STMobileAnimalNative;
import com.softsugar.stmobile.model.STMobileAnimalResult;
import com.softsugar.stmobile.params.STRotateType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class STUtils {
    public static void savePicture(Context context, int textureId, int width, int height) {
        if (textureId == OpenGLUtils.NO_TEXTURE) return;

        ByteBuffer tmpBuffer = ByteBuffer.allocate(width * height * 4);
        int[] frameBuffers = new int[1];

        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId, 0);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, tmpBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glDeleteFramebuffers(1, frameBuffers, 0);

        onPictureTaken(context, tmpBuffer, getOutputMediaFile(), width, height);
    }

    public static void onPictureTaken(Context context, ByteBuffer data, File file, int mImageWidth, int mImageHeight) {
        if (mImageWidth <= 0 || mImageHeight <= 0)
            return;
        Bitmap srcBitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        data.position(0);
        srcBitmap.copyPixelsFromBuffer(data);
        saveToSDCard(context, file, srcBitmap);
        srcBitmap.recycle();
    }

    public static void saveToSDCard(Context context, File file, Bitmap bmp) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
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
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

//    public static String getFilePath(Context context, String fileName) {
//        String path = null;
//        File dataDir = context.getApplicationContext().getExternalFilesDir(null);
//        if (dataDir != null) {
//            path = dataDir.getAbsolutePath() + File.separator + fileName;
//        }
//        return path;
//    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".png");

        return mediaFile;
    }

    public static int getRenderOrientation(int deviceOrientation) {
        int dir = deviceOrientation;
        int orientation = dir - 1;
        if (orientation < 0) {
            orientation = dir ^ 3;
        }

        return orientation;
    }

    public static int getHumanActionOrientation(boolean frontCamera, int accOrientation, int cameraOrientation) {
        int orientation = accOrientation;
        //在使用后置摄像头，且传感器方向为0或2时，后置摄像头与前置orentation相反
        if (!frontCamera && accOrientation == STRotateType.ST_CLOCKWISE_ROTATE_0) {
            orientation = STRotateType.ST_CLOCKWISE_ROTATE_180;
        } else if (!frontCamera && accOrientation == STRotateType.ST_CLOCKWISE_ROTATE_180) {
            orientation = STRotateType.ST_CLOCKWISE_ROTATE_0;
        }
        // 请注意前置摄像头与后置摄像头旋转定义不同 && 不同手机摄像头旋转定义不同
        if (((cameraOrientation == 270 && (accOrientation & STRotateType.ST_CLOCKWISE_ROTATE_90) == STRotateType.ST_CLOCKWISE_ROTATE_90) ||
                (cameraOrientation == 90 && (accOrientation & STRotateType.ST_CLOCKWISE_ROTATE_90) == STRotateType.ST_CLOCKWISE_ROTATE_0)))
            orientation = (orientation ^ STRotateType.ST_CLOCKWISE_ROTATE_180);
        return orientation;
    }

    public static STMobileAnimalResult processAnimalFaceResult(STMobileAnimalResult result, int width, int height, boolean isFrontCamera, int cameraOrientation) {
        if (result == null) {
            return null;
        }
        if (isFrontCamera && cameraOrientation == 90) {
            result = STMobileAnimalNative.animalRotate(width, height, STRotateType.ST_CLOCKWISE_ROTATE_90, result);
            result = STMobileAnimalNative.animalMirror(height, result);
        } else if (isFrontCamera && cameraOrientation == 270) {
            result = STMobileAnimalNative.animalRotate(width, height, STRotateType.ST_CLOCKWISE_ROTATE_270, result);
            result = STMobileAnimalNative.animalMirror(height, result);
        } else if (!isFrontCamera && cameraOrientation == 270) {
            result = STMobileAnimalNative.animalRotate(width, height, STRotateType.ST_CLOCKWISE_ROTATE_270, result);
        } else if (!isFrontCamera && cameraOrientation == 90) {
            result = STMobileAnimalNative.animalRotate(width, height, STRotateType.ST_CLOCKWISE_ROTATE_90, result);
        }
        return result;
    }
}
