package com.jinwoo.machine_learning_kit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import kotlinx.android.synthetic.main.activity_trans.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class TransActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans)

        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.KO)
            .build()
        val englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        englishGermanTranslator.downloadModelIfNeeded()
            .addOnSuccessListener { toast("모델 다운로드 실패") }
            .addOnFailureListener { exception -> toast("모델 다운로드 실패") }


        trans_trans_btn.onClick {
            englishGermanTranslator.translate(trans_english_edit.text.toString())
                .addOnSuccessListener { translatedText ->
                    toast("번역 성공")
                    trans_trans_korean.text = translatedText
                }
                .addOnFailureListener { exception -> toast("번역 실패") }
        }
    }
}
