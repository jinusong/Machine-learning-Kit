package com.jinwoo.machine_learning_kit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_face.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import android.graphics.PointF
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import org.jetbrains.anko.imageBitmap


class FaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)

        face_ocr_btn.onClick {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, 200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val uri = data.data
            ocrText(MediaStore.Images.Media.getBitmap(contentResolver, uri))
        }
    }

    fun ocrText(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .build()

        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)


        detector.detectInImage(image)
            .addOnSuccessListener {
                face_ocr_tv.text = ""
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                findText(it, mutableBitmap)
            }
            .addOnFailureListener { toast("실패") }
    }

    @SuppressLint("SetTextI18n")
    fun findText(result: List<FirebaseVisionFace>, image: Bitmap) {
        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F

        for (face in result) {
            canvas.drawRect(face.boundingBox, rectPaint)
            face_ocr_tv.text =
                "rotY: ${face.headEulerAngleY}\n " +
                        "rotZ: ${face.headEulerAngleZ}\n" +
                        "smileProb: ${face.smilingProbability}\n" +
                        "leftEyeOpen: ${face.leftEyeOpenProbability}\n" +
                        "rightEyeOpen: ${face.rightEyeOpenProbability}\n" +
                        "id: ${face.trackingId}\n\n\n"
        }

        face_ocr_img.imageBitmap = image
    }
}
