package com.brainup.readbyapp.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.brainup.readbyapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

//=========== this activity shows the privacy policy of connect osh===========
public class DocumentViewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageView zoomImageView;
    private static final String URL = "http://docs.google.com/gview?embedded=true&url=";

    public static String EXTRA_DOC_URL = "doc_url";
    public static String EXTRA_DOC_TITLE = "doc_title";
    String mDocUrl = "";
    String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
            );
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            mDocUrl = getIntent().getExtras().getString(EXTRA_DOC_URL);
            // mDocUrl = URL + mDocUrl;
            mTitle = getIntent().getExtras().getString(EXTRA_DOC_TITLE);
            if (mTitle != null)
                setTitle(mTitle);
        }

        init();
        loadAttachment(mDocUrl);
    }

    //==== on back arrow pressed=========
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //=========== initialization===========
    private void init() {
        webView = findViewById(R.id.privacy_policy_webview);
        progressBar = findViewById(R.id.privacy_policy_loader);
        zoomImageView = findViewById(R.id.ziView);

    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;

        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // progressBar.setVisibility(View.GONE);
        }
    }

    //============ memory leak precaution========
    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    private void freeMemory() {
        webView = null;
    }

    public void playVideoInWebView(String url) {

        progressBar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                progressBar.setVisibility(View.VISIBLE);
                return true;
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String extension = MimeTypeMap.getFileExtensionFromUrl(url);
                if (extension != null) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String mimeType = mime.getMimeTypeFromExtension(extension);
                    if (mimeType != null) {
                        if (mimeType.toLowerCase().contains("video")
                                || extension.toLowerCase().contains("mov")
                                || extension.toLowerCase().contains("mp3")) {
                            DownloadManager mdDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(url));
                            File destinationFile = new File(
                                    Environment.getExternalStorageDirectory(),
                                    getFileName(url));
                            // request.setDescription(getString(R.string.downloading_via_osh));
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationUri(Uri.fromFile(destinationFile));
                            mdDownloadManager.enqueue(request);
                            // value = false;
                        }
                    }
                }
            }
        });

        webView.loadUrl(url);
    }

    public String getFileName(String url) {
        if (url.contains("Alternatate.mp4"))
            return "Alternatate.mp4";
        else
            return (url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf(".")) + ".mp4");
    }

    private void loadAttachment(String url) {
        if (url != null) {
            showWebView();

        } else {
            showWebView(mDocUrl);
            //noDataFound();
        }
    }

    private void showImage(String url) {
        zoomImageView.setVisibility(View.VISIBLE);
        if (url != null) {
            if (!url.equals("") || !url.isEmpty()) {
                Picasso.get()
                        .load(url)
                        .resize(300, 300)
                        .into(zoomImageView);
            }
        }
    }

    private void showWebView() {
        mDocUrl = URL + mDocUrl;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl(mDocUrl);
        webView.setWebViewClient(new AppWebViewClients(progressBar));
    }

    private void showWebView(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl(URL + url);
        webView.setWebViewClient(new AppWebViewClients(progressBar));
    }
}

