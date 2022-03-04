package uk.ac.abertay.intentsandactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Locale;

public class BrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        String uri = this.getIntent().getStringExtra("webAddressToLoad");
        if(uri == null) uri = this.getIntent().getDataString();
        if(uri == null){
            Toast.makeText(this, "There is an issue with the provided link and the browser cannot open!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            return;
        }
        loadWebView(uri);
    }

    private void loadWebView(String uri) {
        WebView webView = findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(uri);
    }
}