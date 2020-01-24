package io.github.sbmcfadden.androidopencvsample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // load and initialize OpenCV library
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    private native String stringFromJNI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "stringFromJNI() returned " + stringFromJNI());
    }

    public void processImage(View v) {
        Mat img = null;

        try {
            img = Utils.loadResource(this, R.drawable.test_image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (img != null);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);

        Mat img_result = img.clone();
        Imgproc.Canny(img, img_result, 80, 90);
        Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_result, img_bitmap);
        ImageView imageView = findViewById(R.id.image);
        imageView.setImageBitmap(img_bitmap);
    }
}
