package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import app.hn.com.ficohsaseguros.R;
import interfaces.PanAndZoomListener;

/**
 * Created by mac on 2/4/16.
 */
public class NotificationImageActivity extends Activity {

    ImageView imageView;
    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;

    float lastFocusX;
    float lastFocusY;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_image_notificacion);
        imageView = (ImageView)findViewById(R.id.imageViewNotification);
        SGD = new ScaleGestureDetector(this,new ScaleListener());

        activity = this;

        //getActionBar().setTitle("Ficohsa | Seguros");
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.actionbar_title);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_tittle_back);

        ImageButton b = (ImageButton) findViewById(R.id.imageViewBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String imagen = bundle.getString("image64");
        String imagenB64 = imagen.replace("data:image/jpeg;base64,","");

        if(imagenB64 != ""){
            byte[] decodedString = Base64.decode(imagenB64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //imageView.setImageBitmap(decodedByte);
           FrameLayout view = new FrameLayout(this);
            FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(decodedByte);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            view.addView(imageView, fp);
            view.setOnTouchListener(new PanAndZoomListener(view, imageView, PanAndZoomListener.Anchor.TOPLEFT));
            setContentView(view);
        }


    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastFocusX = detector.getFocusX();
            lastFocusY = detector.getFocusY();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f,Math.min(scale,5f));
            matrix.setScale(scale,scale);
            imageView.setImageMatrix(matrix);


            Matrix transformationMatrix = new Matrix();
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            //Zoom focus is where the fingers are centered,
            transformationMatrix.postTranslate(-focusX, -focusY);

            transformationMatrix.postScale(detector.getScaleFactor(), detector.getScaleFactor());

            /* Adding focus shift to allow for scrolling with two pointers down. Remove it to skip this functionality. This could be done in fewer lines, but for clarity I do it this way here */
            //Edited after comment by chochim
            float focusShiftX = focusX - lastFocusX;
            float focusShiftY = focusY - lastFocusY;
            transformationMatrix.postTranslate((focusX + focusShiftX), focusY + focusShiftY);
            matrix.postConcat(transformationMatrix);
            lastFocusX = focusX;
            lastFocusY = focusY;
            //invalidate();

            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //SGD.onTouchEvent(event);
        return true;
    }


}
