package com.fitlife.atfsd.data.remote

import android.content.SharedPreferences
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.fitlife.atfsd.domain.KOTIK_UPDATED

class KotikConsumer(private val fitLifePrefs:SharedPreferences):WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.visibility = View.VISIBLE
        CookieManager.getInstance().flush()
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url?.contains("app_link_save") == true) checkPath(url)
        return false
    }

    private fun checkPath(kotikHouse: String) {
        val regex = Regex("""(app_link_save=https://[a-z\d./-]*)""")
        val newKotik = regex.find(kotikHouse)?.value
        if (newKotik != null) {
            val replacedHouse = newKotik.replace("app_link_save=", "")
            fitLifePrefs.edit().putString(KOTIK_UPDATED, replacedHouse).apply()
        }
    }

}