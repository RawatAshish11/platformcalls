package com.example.platformcalls

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.example.platformcalls.databinding.ActivityWebViewBinding
import java.net.CookieManager

import android.view.View


import android.webkit.WebView


class WebView : AppCompatActivity() {
    private var chatWebSettings : WebSettings? = null
    private var chatCookieManager: CookieManager? = null

    val urlToLoad = "https://shubhamvermarg822e.blob.core.windows.net/e-learning-app-container/ebook_document/f7c4a615-dbac-4387-9096-226007fe117eThe%20Secret%20Garden%27s%20Song/wetransfer_snake-rabbit/Snake%20&%20Rabbit_New%20File%20-%20Presenter%20output/presentation_html5.html"
    //      val urlToLoad = "https://www.geeksforgeeks.org/singleton-class-java/"
    private lateinit var _binding:ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setToolBar()
        loadWebView()
    }
    private fun setToolBar() {
        // Set the toolbar as the action bar
        setSupportActionBar(_binding.myToolbar)

// Enable the Up button and set its click listener
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)       // adds the back arrow functionality.
            actionBar.setDisplayShowHomeEnabled(true)       // ensures the arrow is visible.
        }

// Handle navigation up click using lambda
        _binding.myToolbar.setNavigationOnClickListener {
            onBackPressed() // Use onBackPressed() for default back navigation
        }
    }

    private fun loadWebView() {
//        chatCookieManager = CookieManager.getInstance()
//        chatCookieManager?.setAcceptCookie(true)            //  enables the WebView to accept cookies.
//        chatCookieManager?.setAcceptThirdPartyCookies(
//            _binding?.joinMeetingWebView,
//            true
//        )   // Setting it to false means that the WebView will only accept cookies from the same domain as the one currently loaded, and not from third-party domains.

        _binding?.joinMeetingWebView?.webChromeClient = WebChromeClient()

        var initialProgressBar: ProgressDialog? = ProgressDialog(this)
        initialProgressBar?.setTitle("Loading...")
        initialProgressBar?.setMessage("Please Wait...")
        initialProgressBar?.setCancelable(false)
        initialProgressBar?.show()

        // swipe refresh to reLoad the website
//        _binding?.swipeRefreshLayout?.setOnRefreshListener {
//            _binding?.joinMeetingWebView?.reload()
//        }

        _binding?.joinMeetingWebView?.webChromeClient = object : WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        _binding?.joinMeetingWebView?.webViewClient  = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
//                _binding?.swipeRefreshLayout?.isRefreshing = false

            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                initialProgressBar?.let {
                    it.dismiss()
                    initialProgressBar = null

                }
            }
        }

        chatWebSettings?.setRenderPriority(WebSettings.RenderPriority.HIGH)
        _binding?.joinMeetingWebView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)

//        chatWebSettings?.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537"

        chatWebSettings = _binding?.joinMeetingWebView?.settings
        chatWebSettings?.javaScriptEnabled = true       // Enables or disables JavaScript support in the WebView.
        chatWebSettings?.cacheMode  = WebSettings.LOAD_DEFAULT      // Sets the caching mode for the WebView content.
        chatWebSettings?.setGeolocationEnabled(false)       // : Enables or disables geolocation in the WebView.

        chatWebSettings?.allowContentAccess = false     //  Sets whether the WebView allows access to content from other origins.
        chatWebSettings?.allowFileAccess = false        //  Sets whether the WebView allows access to the file system.
        chatWebSettings?.builtInZoomControls = false    // Enables or disables built-in zoom controls in the WebView.
        chatWebSettings?.databaseEnabled = false        //  Enables or disables database storage in the WebView
        chatWebSettings?.displayZoomControls = false    // Enables or disables the display of zoom controls in the WebView.
        chatWebSettings?.domStorageEnabled = true       // Enables or disables DOM storage (Web Storage) in the WebView.
        chatWebSettings?.saveFormData = false       // Enables or disables saving form data in the WebView.


        _binding?.joinMeetingWebView?.loadUrl(urlToLoad!!)

    }
}