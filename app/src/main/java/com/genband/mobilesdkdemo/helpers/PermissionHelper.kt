package com.genband.mobilesdkdemo.helpers

//Bu kod, Android uygulamalarında kullanıcı izinlerini yönetmek için oluşturulmuş bir yardımcı sınıftır. Amacı, uygulamanın çalışması için gerekli olan bazı izinleri kontrol etmek ve bu izinleri almak için gereken işlemleri gerçekleştirmektir.
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    const val REQUEST_WRITE_STORAGE_REQUEST_CODE = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    fun requestAppPermissions(context: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        if (hasPermissions(context)) {
            return
        }
        if (hasAudioCallPermission(context)){
            return
        }
        if (hasVideoCallPermissions(context)){
            return
        }

        ActivityCompat.requestPermissions(
            context,
            requiredPermissions,
            REQUEST_WRITE_STORAGE_REQUEST_CODE
        )
    }

    fun hasPermissions(context: Context): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun hasAudioCallPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasVideoCallPermissions(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }
}
