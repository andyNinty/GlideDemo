package andy.lee.glidedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButton;
    private ImageView mCacheImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mCacheImg = (ImageView) findViewById(R.id.image_view_from_disk);
        mButton = (Button) findViewById(R.id.read_cache);

        //网络读取数据,1可以是你的数据库中的主键
        GetImageCacheTask task = new GetImageCacheTask(this, mImageView);
        task.execute(new String[]{"http://upload-images.jianshu.io/upload_images/490111-90b1fbbadea76d6d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240", "1"});


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //本地读取数据,1可以是你数据库的主键
                final String CACHE_URL = "mnt/sdcard/Android/data/" + getPackageName() + "/cache/";
                Glide.with(MainActivity.this)
                        .load(CACHE_URL + "1.jpg")
                        .into(mCacheImg);
            }
        });


    }

}
