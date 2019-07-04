package com.jinwoo.machine_learning_kit

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_text.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.firebase.ml.vision.text.FirebaseVisionText
import org.jetbrains.anko.imageBitmap


class TextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        text_ocr_btn.onClick {
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

        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image)
            .addOnSuccessListener {
                text_ocr_tv.text = ""
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                findText(it, mutableBitmap)
            }
            .addOnFailureListener { toast("실패") }
    }

    fun findText(result: FirebaseVisionText, image: Bitmap) {
        val canvas = Canvas(image)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F

        var index = 0
        for (block in result.textBlocks) {
            for (line in block.lines) {
                canvas.drawRect(line.boundingBox, rectPaint)
                canvas.drawText(index.toString(), line.cornerPoints!![2].x.toFloat(), line.cornerPoints!![2].y.toFloat(), textPaint)
                text_ocr_tv.text = "${text_ocr_tv.text} \n ${line.text}"
                index++
            }
        }

        text_ocr_img.imageBitmap = image
    }
}
