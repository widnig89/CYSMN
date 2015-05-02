package at.sum.android.cysmn.sensing.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.RotateAnimation;

import at.sum.android.cysmn.controllers.ServiceFacadeSubject;
import at.sum.android.cysmn.sensing.ISensorHandler;
import at.sum.android.cysmn.utils.AppLogger;
import at.sum.android.cysmn.utils.ServiceEnum;

/**
 * Created by widnig89 on 02.05.15.
 */
public class CompassSensorListener extends ServiceFacadeSubject implements SensorEventListener, ISensorHandler {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] rotMatrix = new float[9];
    private float[] orientation = new float[3];
    private float currentDegree = 0.0f;
    private float azimuthInDegrees = 0.0f;
    private long oldTimeSec = 0;


    private Context ctx;

    public CompassSensorListener(Context ctx)
    {
        this.ctx = ctx;
        this.sensorManager = (SensorManager)ctx.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }



    //http://www.techrepublic.com/article/pro-tip-create-your-own-magnetic-compass-using-androids-internal-sensors/
    @Override
    public void onSensorChanged(SensorEvent event) {
        //AppLogger.logDebug(getClass().getSimpleName(), "Compass: onSensorChanged");
        Long currentTimeSec = event.timestamp / 1000000000; //get seconds
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(rotMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotMatrix, orientation);
            float azimuthInRadians = orientation[0];
            azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            currentDegree = -azimuthInDegrees;

            //AppLogger.logDebug(getClass().getSimpleName(), "Compass: onSensorChanged (both true)");
            lastAccelerometerSet = false;
            lastMagnetometerSet = false;

            if(currentTimeSec - oldTimeSec > 2) //bigger than 2 seconds (from nanoseconds)
            {
                oldTimeSec = currentTimeSec;
                notifyObservers(ServiceEnum.COMPASS);
            }

        }
    }

    public float getAzimuthInDegrees()
    {
        return azimuthInDegrees;
    }

    public float getCurrentDegree()
    {
        return currentDegree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void startSensing() {
        sensorManager.registerListener(this,accelerometer,2000000);
        sensorManager.registerListener(this,magnetometer, 2000000);
    }

    @Override
    public void stopSensing() {
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
    }
}
