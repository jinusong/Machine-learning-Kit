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
import com.google.firebase.ml.vision.objects.FirebaseVisionObject
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_track_object.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class TrackObjectActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_object)
        object_ocr_btn.onClick {
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

        val options = FirebaseVisionObjectDetectorOptions.Builder()
            .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val detector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options)

        detector.processImage(image)
            .addOnSuccessListener {
                object_ocr_tv.text = ""
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                findText(it, mutableBitmap)
            }
            .addOnFailureListener { toast("실패") }
    }

    @SuppressLint("SetTextI18n")
    fun findText(result: List<FirebaseVisionObject>, image: Bitmap) {
        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F

        for (obj in result) {
            canvas.drawRect(obj.boundingBox, rectPaint)
            object_ocr_tv.text = object_ocr_tv.text.toString() +
                    "id: ${obj.trackingId}\n " +
                    "category: ${obj.classificationCategory}\n" +
                    "confidence: ${obj.classificationConfidence}\n\n\n"
        }

        object_ocr_img.imageBitmap = image
    }
}