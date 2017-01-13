package andy.lee.glidedemo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static android.content.ContentValues.TAG;

/**
 * andy.lee.glidedemo
 * Created by andy on 17-1-12.
 */

public class GetImageCacheTask extends AsyncTask<String, Void, File> {
    private final Context mContext;

    private String fileNum;

    private ImageView mImageView;
    private String mImgUrl;

    public GetImageCacheTask(Context context, ImageView imageView) {
        mContext = context;
        mImageView = imageView;
    }

    @Override
    protected File doInBackground(String... params) {
        mImgUrl = params[0];
        fileNum = params[1];
        try {
            File file = Glide.with(mContext)
                    .load(mImgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            publishProgress();
            return file;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File file) {
        if (file == null) {
            return;
        }
        String path = file.getPath();
        copyFile(path, fileNum);
    }

    public void copyFile(String oldPath, String fileNum) {
        //有网 优先加载网络图片，当然这样做是不合适的，因为只要第一次从网络上加载图片之后，若用户不清理缓存，则从本地读取图片
        //这一块还没想好
        Glide.with(mContext)
                .load(mImgUrl)
                .into(mImageView);
        //将图片复制到cache文件夹下，并重命名，便于缓存显示
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                File file = new File(mContext.getExternalCacheDir().getPath(), fileNum + ".jpg");
                Log.d(TAG, "copyFile: " + file.getPath());
                InputStream is = new FileInputStream(oldPath);
                byte[] buffer = new byte[1024];

                RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
                while ((byteRead = is.read(buffer)) != -1) {
                    accessFile.write(buffer, 0, byteRead);
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
