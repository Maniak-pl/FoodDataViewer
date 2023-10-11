package pl.maniak.fooddataviewer.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.fotoapparat.Fotoapparat
import io.fotoapparat.view.CameraView
import pl.maniak.fooddataviewer.R

class ScanFragment : Fragment(R.layout.scan_fragment) {
    private lateinit var fotoapparat: Fotoapparat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraView = view.findViewById<CameraView>(R.id.camera_view)

        fotoapparat = Fotoapparat(
            context = requireContext(),
            view = cameraView
        )
    }

    override fun onStart() {
        super.onStart()
        handleCameraPermission(false)
    }

    override fun onStop() {
        fotoapparat.stop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handleCameraPermission(permissionResult = true)
    }

    private fun handleCameraPermission(permissionResult: Boolean) {
        if (hasCameraPermission()) {
            fotoapparat.start()
        } else if (!permissionResult || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

