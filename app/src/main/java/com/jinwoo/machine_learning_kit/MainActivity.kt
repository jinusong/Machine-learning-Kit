package com.jinwoo.machine_learning_kit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_trans_btn.onClick { startActivity<TransActivity>() }
        main_text_btn.onClick { startActivity<TextActivity>() }
        main_face_btn.onClick { startActivity<FaceActivity>() }
    }
}