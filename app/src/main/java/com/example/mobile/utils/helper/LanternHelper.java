package com.example.mobile.utils.helper;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;

public class LanternHelper {

    private static final String SENSOR = "SENSOR";
    private final CameraManager cameraManager;
    private String cameraId;

    public LanternHelper(final Context context) {
        this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (final String id : this.cameraManager.getCameraIdList()) {
                if (Boolean.TRUE.equals(this.cameraManager.getCameraCharacteristics(id)
                        .get(CameraCharacteristics.FLASH_INFO_AVAILABLE))) {
                    this.cameraId = id;
                    break;
                }
            }
        } catch (final CameraAccessException e) {
            Log.e(SENSOR, "Não foi possível acessar a câmera.");
        }
    }

    public void turnOn() {
        try {
            this.cameraManager.setTorchMode(this.cameraId, true);
        } catch (final CameraAccessException e) {
            Log.e(SENSOR, "Não foi possível ligar a câmera.");
        }
    }

    public void turnOff() {
        try {
            this.cameraManager.setTorchMode(this.cameraId, false);
        } catch (final CameraAccessException e) {
            Log.e(SENSOR, "Não foi possível desligar a câmera.");
        }
    }
}
