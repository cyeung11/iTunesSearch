package com.ych.itunessearch


import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.ych.itunessearch.databinding.DialogLangBinding
import java.util.Locale

class LanguageDialog(
    private val act: Activity,
) : Dialog(act, androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog), View.OnClickListener {

    private val viewBinding: DialogLangBinding =
        DialogLangBinding.inflate(layoutInflater, null, false)

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(viewBinding.root)

        viewBinding.btnCn.setOnClickListener(this)
        viewBinding.btnEn.setOnClickListener(this)
        viewBinding.btnZh.setOnClickListener(this)
    }

    override fun onStart() {
        if (window != null) {

            val d = ColorDrawable(0x00000000)
            window?.setBackgroundDrawable(d)
        }

        super.onStart()
    }

    override fun onClick(v: View?) {
        val locale: Locale
        when (v?.id) {
            R.id.btnCn -> {
                locale = Locale("zh", "CN")
            }
            R.id.btnEn -> {
                locale = Locale("en", "US")
            }
            R.id.btnZh -> {
                locale = Locale("zh", "HK")
            }
            else -> {
                return
            }
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        dismiss()
    }
}