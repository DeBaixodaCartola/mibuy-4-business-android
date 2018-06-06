package com.example.felipe.mibuy4business;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

import com.example.felipe.mibuy4business.Notificacao.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressBar spinner;
    FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.activity_main_webview);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        // Enable Javascript
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add JavaScript interface
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Enable DOM storage to firebase auth persist
        webview.getSettings().setDomStorageEnabled(true);

        webview.loadUrl("https://projetocartolada.firebaseapp.com/");

    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView webview, String url) {

            spinner.setVisibility(View.INVISIBLE);

            webview.setVisibility(View.VISIBLE);
            super.onPageFinished(webview, url);

        }
    }

    public class WebAppInterface {
        Context mContext;
        String email, pass;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showEmailAndPass(String email, String pass) {

            // save email and pass
            SharedPrefManager.getInstance(getApplicationContext()).saveEmail(email);
            SharedPrefManager.getInstance(getApplicationContext()).savePass(pass);

            final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

            autenticacao = FirebaseAuth.getInstance();
            autenticacao.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        // pegar uid user
                        FirebaseUser userMibuy = task.getResult().getUser();
                        String id = userMibuy.getUid();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Empresas").child(id).child("token").setValue(token);

                    }
                }
            });


        }

    }

}
