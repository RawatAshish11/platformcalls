package com.example.platformcalls

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.example.platformcalls.databinding.ActivityWebViewBinding
import java.net.CookieManager

import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse


import android.webkit.WebView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate


class WebView : AppCompatActivity() {
    private var chatWebSettings : WebSettings? = null
    private var chatCookieManager: CookieManager? = null


    var urlToLoad : String? = null
    var appBarName : String? = null
    //      val urlToLoad = "https://www.geeksforgeeks.org/singleton-class-java/"
    companion object {
        val TAG = "WebViewActivity_d"
    }

    private lateinit var _binding:ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        appBarName = intent.getStringExtra("appBarTitle").toString()
        setToolBar()
        urlToLoad = intent.getStringExtra("appBarUrl").toString()

        loadWebView()
    }
    private fun setToolBar() {
        // Set the toolbar as the action bar
        setSupportActionBar(_binding.myToolbar)

// Enable the Up button and set its click listener
        val actionBar = supportActionBar
        _binding.appBartitle.text = appBarName
        if (actionBar != null) {
            actionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)       // adds the back arrow functionality.
            actionBar.setDisplayShowHomeEnabled(true)       // ensures the arrow is visible.
        }
//        val layoutParams = _binding.myToolbar.layoutParams as Toolbar.LayoutParams
//        layoutParams.gravity = Gravity.CENTER
        _binding.myToolbar.setTitleTextColor(resources.getColor(R.color.cardview_dark_background))     // set title color

        // Set back navigation arrow color to white (using AppCompatDelegate for compatibility)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        _binding.myToolbar.navigationIcon = resources.getDrawable(R.drawable.abc_ic_ab_back_material, theme)   // set custom back btn
        _binding.myToolbar.navigationIcon?.setTint(resources.getColor(R.color.cardview_dark_background))   // set Navigation color

        // Handle navigation up click using lambda
        _binding.myToolbar.setNavigationOnClickListener {
            onBackPressed() // Use onBackPressed() for default back navigation
        }
        _binding.myToolbar.setBackgroundColor(resources.getColor(R.color.cardview_light_background))
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

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            /*
                        * Intercepts navigation within a WebView before it happens.
                        * Gives you an opportunity to decide whether to allow the WebView to handle the navigation or handle it yourself.
                        * */
            override fun shouldOverrideUrlLoading(      // todo: try this
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                Log.d(TAG + 1, "url2::::::::${request?.url}")
                Log.d(TAG + 1, "view2:${view?.url.toString()}")
                return false
            }

            /*
                        * Intercepts requests for web resources (HTML, CSS, JavaScript, images, etc.) before they're sent to the network.
                        * Allows you to take control over these requests, enabling customization, filtering, or blocking based on your app's needs.
                        * */
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                Log.d(TAG, "[shouldInterceptRequest] url::::::::: ${request?.url}")
                var quizzUrl = request?.url
                onQuizLastPageSuccess(quizzUrl)    // to fininsh the screen & return result

                if (!request?.url.toString().startsWith("https://")) {
                    Log.d(
                        TAG,
                        "[shouldInterceptRequest][NON-HTTPS] Blocked access to ${request?.url}"
                    )
                    return WebResourceResponse(
                        "text/javascript",
                        "UTF-8",
                        null
                    ) // Deny non-HTTPS URLs
                }
                return null
            }


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
    // https://shubhamvermarg822e.blob.core.windows.net/e-learning-app-container/Success.html
    private fun onQuizLastPageSuccess(quizzUrl: Uri?) {
        if (quizzUrl.toString().contains("Success.html")) {
            val resultIntent = Intent()
            resultIntent.putExtra("INTENT_WEBVIEW_KEY", "Success.html")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}