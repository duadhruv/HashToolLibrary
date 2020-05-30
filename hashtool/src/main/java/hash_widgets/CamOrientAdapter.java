package hash_widgets;

import android.hardware.Camera;
import android.view.Surface;

import androidx.appcompat.app.AppCompatActivity;

public class CamOrientAdapter {
    int result;
    public  void setCameraDisplayOrientation(AppCompatActivity activity,
                                             int cameraId, Camera camera) {

        Camera.CameraInfo info =
                new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }


        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        try {
            camera.setDisplayOrientation(result);
        }catch (RuntimeException e)
        {
            e.printStackTrace();
        }


    }
    public int setBitmapOrientation()
    {
        return result;
    }
}
