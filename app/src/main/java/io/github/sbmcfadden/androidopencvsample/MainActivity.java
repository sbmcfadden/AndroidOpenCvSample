package io.github.sbmcfadden.androidopencvsample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Sample application exploring the use of OpenCV in Android
 */
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

    /**
     * Handle button clicks
     * @param v {@link View} representing the {@link Button} pressed
     */
    public void onButtonClick(View v) {
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        if (buttonText.equals( getString(R.string.load_image)) ) {
            loadUnmodifiedImage();
            b.setText( R.string.process_image);
        } else if (buttonText.equals( getString(R.string.process_image)) ) {
            processImage();
            b.setText( R.string.load_image);
        }
    }

    /**
     * Load and display unmodified image
     */
    public void loadUnmodifiedImage() {

        Mat imgMat;
        try {
            imgMat = Utils.loadResource(this, R.drawable.test_image);
        } catch (IOException e) {
            Log.e(TAG, "Failed to load image.", e);
            return;
        }

        Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_RGB2BGRA);

        Bitmap imgBitmap = Bitmap.createBitmap(imgMat.cols(), imgMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imgMat, imgBitmap);
        TextureView textureView = findViewById(R.id.texture_view);
        Surface textureSurface = new Surface(textureView.getSurfaceTexture());
        Canvas canvas = textureSurface.lockHardwareCanvas();
        Rect dst = new Rect(0, 0, imgBitmap.getWidth(), imgBitmap.getHeight());
        canvas.drawBitmap(imgBitmap, null, dst, null);
        textureSurface.unlockCanvasAndPost(canvas);
        textureSurface.release();
    }

    /**
     * Process and display image currently shown in {@link TextureView}
     */
    public void processImage() {
        TextureView textureView = findViewById(R.id.texture_view);
        Bitmap imgBitmap = textureView.getBitmap();
        Mat imgMat = new Mat();
        Utils.bitmapToMat(imgBitmap, imgMat);

        Mat imgEdges = new Mat(imgMat.rows(), imgMat.cols(), CvType.CV_8UC1);
        Imgproc.Canny(imgMat, imgEdges, 32, 64);

        Utils.matToBitmap(imgEdges, imgBitmap);
        Surface textureSurface = new Surface(textureView.getSurfaceTexture());
        Canvas canvas = textureSurface.lockHardwareCanvas();

        Rect dst = new Rect(0, 0, imgBitmap.getWidth(), imgBitmap.getHeight());
        canvas.drawBitmap(imgBitmap, null, dst, null);
        textureSurface.unlockCanvasAndPost(canvas);
        textureSurface.release();
    }
}
