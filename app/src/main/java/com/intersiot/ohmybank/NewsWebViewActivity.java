package com.intersiot.ohmybank;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class NewsWebViewActivity extends AppCompatActivity {
    // web view, web settings
    private WebView webView;
    private WebSettings webSettings;
    // firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);
        // 웹뷰 레이아웃 등록
        webView = findViewById(R.id.news_webView);

        url = getIntent().getStringExtra("url");
        // 클릭시 새창 안뜨게
        webView.setWebViewClient(new WebViewClient());
        //세부 세팅 등록
        webSettings = webView.getSettings();
        // detail settings
        // 웹페이지 자바스크립트 허용 여부
        webSettings.setJavaScriptEnabled(true);
        // 새창 띄우기 허용 여부
        webSettings.setSupportMultipleWindows(false);
        // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        // 메타태그 허용 여부
        webSettings.setLoadWithOverviewMode(true);
        // 화면 사이즈 맞추기 허용 여부
        webSettings.setUseWideViewPort(true);
        // 화면 줌 허용 여부
        webSettings.setSupportZoom(false);
        // 화면 확대 축소 허용 여부
        webSettings.setBuiltInZoomControls(true);
        // 컨텐츠 사이즈 맞추기
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 브라우저 캐시 허용 여부
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 로컬저장소 허용 여부
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl(url);

    } // end onCreate()

    public void logout(View view) {
        mAuth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}