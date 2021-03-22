package com.intersiot.ohmybank.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.intersiot.ohmybank.R;


public class NewsWebViewFragment extends Fragment {
    // firebase 인증
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // 웹뷰
    private WebView webView;
    private WebSettings webSettings;
    // url
    String url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news_web_view, container, false);
        // 웹뷰 셋팅
        webView = rootView.findViewById(R.id.news_webView);
        // 클릭시 새창 안뜨게
        webView.setWebViewClient(new WebViewClient());
        // 세부 세팅 등록
        webSettings = webView.getSettings();

        return rootView;
    } // end onCreateView()

}