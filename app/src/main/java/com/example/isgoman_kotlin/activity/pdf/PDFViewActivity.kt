package com.example.isgoman_kotlin.activity.pdf

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.print.PrintManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument.Bookmark
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URISyntaxException
import java.net.URL
import java.net.URLEncoder
import java.util.*

class PDFViewActivity : AppCompatActivity(), OnPageChangeListener,
    OnLoadCompleteListener, OnPageErrorListener {
    var relativeHeader: RelativeLayout? = null
    lateinit var headermanager: HeaderManager
    var back: ImageView? = null
    lateinit var download: ImageView
    lateinit var print: ImageView
    lateinit var share: ImageView

    //    PDFView pdf;
    var Pdf: PDFView? = null
    var pageNumber = 0
    var pdfFileName: String? = null
    var dialog: ProgressDialog? = null
    lateinit var webView: WebView
    var url: String? = null
    var title: String? = null
    var name: String? = null
    var extras: Bundle? = null
    lateinit var mContext: Context
    var mProgressDialog: ProgressDialog? = null
    private var printManager: PrintManager? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activty_pdfview)
        mContext = this
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        //  LocaleHelper.setLocale(getApplicationContext(), PrefUtils.getLanguageString(mContext));
        extras = intent.extras
        if (extras != null) {
            url = extras!!.getString("pdf_url")
            title = extras!!.getString("title")
            name = extras!!.getString("filename")
        }
        println("PDF: $url")
        resetDisconnectTimer()
        printManager = this.getSystemService(PRINT_SERVICE) as PrintManager
        relativeHeader = findViewById(R.id.relativeHeader)
        print = findViewById(R.id.print)
        download = findViewById(R.id.download)
        share = findViewById(R.id.shared)
        webView = findViewById(R.id.pdfView)

//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//
//        PdfWebViewClient pdfWebViewClient = new PdfWebViewClient(this, webView);
//        pdfWebViewClient.loadPdfUrl(url);
        dialog = ProgressDialog(this@PDFViewActivity)
        dialog!!.setMessage(resources.getString(R.string.pleasewait)) //Please wait...
        dialog!!.show()
        webView.clearCache(true)
        webView.clearHistory()
        val settings = webView.getSettings()
        settings.javaScriptEnabled = true
        webView.setWebChromeClient(WebChromeClient())
        webView.getSettings().loadWithOverviewMode = true
        webView.getSettings().pluginState = WebSettings.PluginState.ON
        //        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
//        webView.loadUrl("https://view.officeapps.live.com/op/embed.aspx?src="+url);
        val webSettings = webView.getSettings()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = 0
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        Handler().postDelayed({
            //                webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
            webView.loadUrl("http://docs.google.com/viewer?url=$url&embedded=true")
            //  webView.loadUrl(url);
        }, 2000)
        Handler().postDelayed({
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }, 5000)
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                //                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition1");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
//
//                        }
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    println(" url to click$url")
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
                    //                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    if (dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                //                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition2");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
//
//                        }
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    println(" url to click$url")
                    //                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
                    if (dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
            }
        })
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                println("LOADINGURL:$url")
                if (url != null && url.contains("intent://")) {
                    Log.d("URLDATA1:", url)
                    println("url to clickupdated$url")
                    try {
                        val context = webView.getContext()
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
                            val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                            //                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
                            webView.loadUrl(fallbackUrl!!)
                        }
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                } else if (url != null && url.contains("https://isgoman.meitsystems.com")) {
                    Log.d("URLDATA2:", url)
                    webView.loadUrl(url)
                } else {
                    // webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    try {
                        val urltest =
                            "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(
                                url,
                                "ISO-8859-1"
                            )
                        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + urltest);
                        webView.loadUrl(url)
                        Log.d("URLDATA3:", url)
                        println("urltestdata:$urltest")
                        println("urltestdata:$url")
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    if (dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show()
                Log.d(
                    "PDF Error: ",
                    view.url + " error code " + errorCode + " description " + description
                )
                Log.d("LAODING URL DATA:", failingUrl)
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
            }
        })

        /* webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
               Log.d("LOADING URL",url);
//                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition3");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl(url);
//
//                        }
//                        return true;
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                if (url != null && (url.startsWith("https://docs.google.com/gview?embedded=true&url=") || url.startsWith("https://docs.google.com/gview?embedded=true&url="))) {
                    System.out.println(" url to click" + url);
                    try {
                        Context context = webView.getContext();
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            PackageManager packageManager = context.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(fallbackUrl));
                            context.startActivity(browserIntent);
                            webView.loadUrl(url);


                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });*/


//        webView.setWebViewClient(new WebViewClient() {
//
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//
//                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
//                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
//            }
//        });

        // Pdf = findViewById(R.id.pdfView);
        // Pdf.setBackgroundColor(Color.LTGRAY);
        headermanager = HeaderManager(this@PDFViewActivity, title)
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1)
        } else {
            headermanager.getHeader(relativeHeader, 3)
        }
        back = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.backbtn,
            R.drawable.backbtn
        )
        back!!.setOnClickListener {
            stopDisconnectTimer()
            finish()
        }
        print.setOnClickListener(View.OnClickListener {
            stopDisconnectTimer()
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                val jobName = getString(R.string.app_name) + " Document"
                val printJob = printManager!!.print(
                    jobName,
                    PDFPrintDocumentAdapter(
                        this@PDFViewActivity,
                        "document",
                        getFilepath("document.pdf")
                    ),
                    null
                )
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        })
        download.setOnClickListener(View.OnClickListener {
            stopDisconnectTimer()
            // Toast.makeText(mContext, "File Downloaded to download/"+name+".pdf", Toast.LENGTH_SHORT).show();
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                mProgressDialog = ProgressDialog(this@PDFViewActivity)
                mProgressDialog!!.setMessage("Downloading..")
                mProgressDialog!!.isIndeterminate = true
                mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                mProgressDialog!!.setCancelable(true)
               // DownloadPDF().execute()
                mProgressDialog!!.setOnCancelListener {
                   // DownloadPDF().cancel(true) //cancel the task
                }
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        })
        share.setOnClickListener(View.OnClickListener {
            stopDisconnectTimer()
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                val intentShareFile = Intent(Intent.ACTION_SEND)
                val fileWithinMyDir = File(getFilepath("document.pdf"))
                if (fileWithinMyDir.exists()) {
                    intentShareFile.type = "application/pdf"
                    intentShareFile.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("file://" + getFilepath("document.pdf"))
                    )
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman")
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                    startActivity(Intent.createChooser(intentShareFile, "Share File"))
                }
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        })
        /*val permissionListenerGallery: PermissionListener = object : PermissionListener() {
            fun onPermissionGranted() {
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    loadPDF().execute()
                } else {
                    AppUtilityMethods.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Network Error",
                        getString(R.string.no_internet),
                        R.drawable.nonetworkicon,
                        R.drawable.roundred
                    )
                }
            }

            fun onPermissionDenied(deniedPermissions: ArrayList<String?>?) {
                Toast.makeText(mContext, "Permission Denied\n", Toast.LENGTH_SHORT).show()
            }
        }*/
       /* TedPermission.with(mContext)
            .setPermissionListener(permissionListenerGallery)
            .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()*/

        //
    }

    private fun LoadingPdf(uri: File) {
        dialog = ProgressDialog(this@PDFViewActivity)
        dialog!!.setMessage(resources.getString(R.string.pleasewait)) //Please wait...
        dialog!!.show()
        Pdf!!.fromUri(Uri.fromFile(uri))
            .defaultPage(0)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(10) // in dp
            .onPageError(this)
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount))
    }

    override fun loadComplete(nbPages: Int) {
        if (dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        val meta = Pdf!!.documentMeta
        Log.e("PDFVIEW", "title = " + meta.title)
        Log.e("PDFVIEW", "author = " + meta.author)
        Log.e("PDFVIEW", "subject = " + meta.subject)
        Log.e("PDFVIEW", "keywords = " + meta.keywords)
        Log.e("PDFVIEW", "creator = " + meta.creator)
        Log.e("PDFVIEW", "producer = " + meta.producer)
        Log.e("PDFVIEW", "creationDate = " + meta.creationDate)
        Log.e("PDFVIEW", "modDate = " + meta.modDate)
        printBookmarksTree(Pdf!!.tableOfContents, "-")
    }

    private fun printBookmarksTree(tree: List<Bookmark>, sep: String) {
        for (b in tree) {
            Log.e("PDFVIEW", String.format("%s %s, p %d", sep, b.title, b.pageIdx))
            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }

    override fun onPageError(page: Int, t: Throwable) {
        if (dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        Log.e("PDFVIEW", "Cannot load page " + page + "Error: " + t.message)
    }

  /*  class loadPDF : AsyncTask<String?, Void?, Void?>() {
        private val exception: java.lang.Exception? = null
        private val dialog: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            //            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected override fun doInBackground(vararg urls: String): Void? {
            var u: URL? = null
            try {
                val fileName = "document.pdf"
                u = URL(url)
                val c = u.openConnection() as HttpURLConnection
                c.requestMethod = "GET"
                // c.setDoOutput(true);
                val auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r"
                var encodedAuth: String =
                    String(Base64.encodeToString(auth.toByteArray(), Base64.DEFAULT))
                encodedAuth = encodedAuth.replace("\n", "")
                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                c.addRequestProperty("Authorization", "Basic $encodedAuth")
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect()
                val response = c.responseCode
                val PATH = Environment.getExternalStorageDirectory()
                    .toString() + "/download/"
                // Log.d("Abhan", "PATH: " + PATH);
                val file = File(PATH)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val outputFile = File(file, fileName)
                val fos = FileOutputStream(outputFile)
                val `is` = c.inputStream
                val buffer = ByteArray(1024)
                var len1 = 0
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1)
                }
                fos.flush()
                fos.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
            val file =
                File(Environment.getExternalStorageDirectory().absolutePath + "/download/" + "document.pdf")
            val uri =
                Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/download/" + "document.pdf")
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

//            LoadingPdf(file);

            //pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();


            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }
    }*/

    /*inner class DownloadPDF : AsyncTask<String?, Void?, Void?>() {
        private val exception: Exception? = null
        private var dialog: ProgressDialog? = null
        var filename = name!!.replace(" ", "_")
        var fileName = "$filename.pdf"
        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog(this@PDFViewActivity)
            dialog!!.setMessage(resources.getString(R.string.pleasewait)) //Please wait...
            dialog!!.show()
        }

        protected override fun doInBackground(vararg params: String?): Void? {
            var u: URL? = null
            try {
                u = URL(url)
                val c = u.openConnection() as HttpURLConnection
                c.requestMethod = "GET"
                // c.setDoOutput(true);
                val auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r"
                var encodedAuth: String =
                    String(Base64.encodeToString(auth.toByteArray(), Base64.DEFAULT))
                encodedAuth = encodedAuth.replace("\n", "")
                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                c.addRequestProperty("Authorization", "Basic $encodedAuth")
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect()
                val response = c.responseCode
                val PATH = Environment.getExternalStorageDirectory()
                    .toString() + "/download/"
                // Log.d("Abhan", "PATH: " + PATH);
                val file = File(PATH)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val outputFile = File(file, fileName)
                val fos = FileOutputStream(outputFile)
                val `is` = c.inputStream
                val buffer = ByteArray(1024)
                var len1 = 0
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1)
                }
                fos.flush()
                fos.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
            val file =
                File(Environment.getExternalStorageDirectory().absolutePath + "/download/" + fileName)
            val uri =
                Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/download/" + fileName)
            println("file.exists() = " + file.exists())
            if (file.exists()) {
                Toast.makeText(
                    mContext,
                    "File Downloaded to download/$fileName",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    mContext,
                    "Something Went Wrong. Download failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    fun resetDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
        HomeActivity().disconnectHandler.postDelayed(
            HomeActivity().disconnectCallback,
            HomeActivity().DISCONNECT_TIMEOUT
        )
    }

    fun stopDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
    }

    override fun onUserInteraction() {
        resetDisconnectTimer()
    }

    public override fun onResume() {
        super.onResume()
        resetDisconnectTimer()
    }

    public override fun onStop() {
        super.onStop()
        //        stopDisconnectTimer();
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopDisconnectTimer()
    }

    private fun getFilepath(filename: String): String {
        return File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/Download/$filename"
        ).path
    }

    /**
     * PDF CLASS
     */

}
