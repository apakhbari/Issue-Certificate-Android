package com.ada.forcert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.ada.forcert.databinding.ActivityMainBinding;
import com.ada.forcert.utilities.CertificateCredentials;
import com.ada.forcert.utilities.UserCredentials;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ada.forcert.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Objects;

import javax.security.cert.CertificateException;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    static String MY_ALIAS = "ADA_TEST";
    boolean demandedCertExists = false;

    String jsonRequest;

    String responseBody;

    String customerCode= "";
    String revokeCertReason= "2";
    private String resolvedURL = "http://api.raahbartrust.ir/api/RevokeCertificate";

    TextView tvSubjectDN, tvIssuer, tvExpirationDate, tvShowError;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (demandedCertExists)
        {
            Intent createPassingDataActivityIntent = getIntent();
            CertificateCredentials certificateCredentials = (CertificateCredentials) createPassingDataActivityIntent.getSerializableExtra("CertificateCredentials");

            Log.wtf("ADA_LOG", "certificateCredentials is: \n" +
                    "getCertificate is: " + certificateCredentials.getCertificate() + "\n" +
                    "getCN is: " + certificateCredentials.getCN() + "\n" +
                    "getDescription is: " + certificateCredentials.getDescription() + "\n" +
                    "getIsSuccess is: " + certificateCredentials.getIsSuccess() + "\n" +
                    "getIssuerName is: " + certificateCredentials.getIssuerName() + "\n" +
                    "getPaymentStatus is: " + certificateCredentials.getPaymentStatus() + "\n" +
                    "getValidFrom is: " + certificateCredentials.getValidFrom() + "\n" +
                    "getValidTo is: " + certificateCredentials.getValidTo());
        }
        else
        {
            StyleableToast.makeText(MainActivity.this, "خوش آمدید", Toast.LENGTH_LONG, R.style.just_toast).show();
        }


        AppCompatButton button= (AppCompatButton)findViewById(R.id.issueCertButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(demandedCertExists) {
                    StyleableToast.makeText(MainActivity.this, "صدور بیش از یک گواهی، در حال توسعه است.", Toast.LENGTH_LONG, R.style.just_toast).show();
                }
                else
                {
                    Intent UserInputActivityIntent = new Intent(MainActivity.this, UserInputActivity.class);
                    startActivity(UserInputActivityIntent);
                    //startActivity(new Intent(MainActivity.this, UserInputActivity.class));
                }
            }
        });


        AppCompatButton button3= (AppCompatButton)findViewById(R.id.revokeCertButton);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(demandedCertExists){
                    revokeCert();
                }
                else {
                    StyleableToast.makeText(MainActivity.this, "هنوز گواهی‌ای صادر نکرده‌اید!", Toast.LENGTH_LONG, R.style.just_toast).show();
                }
            }
        });

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutForCert);

        tvSubjectDN = (TextView) findViewById(R.id.textviewSubjectDN);
        tvIssuer = (TextView) findViewById(R.id.textviewIssuer);
        tvExpirationDate = (TextView) findViewById(R.id.textviewExpirationDate);
        tvShowError = (TextView) findViewById(R.id.textviewShowError);

        try {
            if (checkKeyStoreForDemandedCert())
            {
                demandedCertExists = true;
                loadCertificate();
                tvShowError.setVisibility(View.GONE);
                tvSubjectDN.setVisibility(View.VISIBLE);
                tvIssuer.setVisibility(View.VISIBLE);
                tvExpirationDate.setVisibility(View.VISIBLE);
            }
            else {
                tvShowError.setVisibility(View.VISIBLE);
                tvSubjectDN.setVisibility(View.GONE);
                tvIssuer.setVisibility(View.GONE);
                tvExpirationDate.setVisibility(View.GONE);
            }

        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private void loadCertificate() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            X509Certificate cert = (X509Certificate) ks.getCertificate(MY_ALIAS+"_signed");

            Log.wtf("ADA_LOG", "X509Certificate is:" + "\n" +
                    "cert.getBasicConstraints(): " + cert.getBasicConstraints() +  "\n" +
                    "getSigAlgName: " + cert.getSigAlgName().toString() + "\n" +
                    "getSigAlgOID: " + cert.getSigAlgOID().toString() + "\n" +
                    "getType: " + cert.getType().toString() + "\n" +
                    "getPublicKey: " + cert.getPublicKey().toString()  + "\n" +
                    "getExtendedKeyUsage: " + cert.getExtendedKeyUsage().toString()  + "\n" +
                    "getIssuerAlternativeNames: " + cert.getIssuerAlternativeNames().toString()  + "\n" +
                    "getIssuerDN: " + cert.getIssuerDN().toString()  + "\n" +
                    "getIssuerUniqueID: " + cert.getIssuerUniqueID().toString() + "\n" +
                    "getIssuerX500Principal: " + cert.getIssuerX500Principal().toString()  + "\n" +
                    "getSerialNumber: " + cert.getSerialNumber().toString()  + "\n" +
                    "getSubjectDN: " + cert.getSubjectDN().toString()  + "\n" +
                    "getNotBefore: " + cert.getNotBefore().toString()  + "\n" +
                    "getNotAfter: " + cert.getNotAfter().toString());

            tvSubjectDN.setText("SubjectDN: ");
            tvIssuer.setText("Issuer: ");
            tvExpirationDate.setText("ExpirationDate: ");


        } catch (Exception e){
            Log.d("ADA_LOG", String.valueOf(e));
        }
    }

    private void deleteCertificate() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            ks.deleteEntry(MY_ALIAS+"_signed");

        } catch (Exception e){
            Log.d("ADA_LOG", String.valueOf(e));
        }
    }

    private boolean checkKeyStoreForDemandedCert() throws java.security.cert.CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException  {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        Enumeration<String> aliases = keyStore.aliases();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Log.wtf("ADA_LOG", "Aliases: " + alias);
            if (alias.equals(MY_ALIAS+"_signed")) {
                Log.wtf("ADA_LOG", "Match!");
                return true;
            }
        }
        return false;
    }

    private void revokeCert() {
        createPassingJson();
        postDataUsingVolley(resolvedURL, jsonRequest);
    }

    private void createPassingJson() {
        jsonRequest = "{\n" +
                "certificate:\""+ "(base64(certificate))" +"\",\n" +
                "signature:\""+ "(Base64(Sign_RSAWithSHA1(certificate)))" +"\",\n" +
                "customercode:\""+ customerCode +"\",\n" +
                "revokeCertReason:\""+ revokeCertReason +"\",\n" +
                "}";

        Log.wtf("ADA_LOG", "jsonRequest: " + jsonRequest);
    }

    private void postDataUsingVolley(String url, String passingData) {

        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Please Wait ...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url,  new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                StyleableToast.makeText(MainActivity.this, "ارتباط موفقیت آمیز انجام شد." , Toast.LENGTH_LONG, R.style.just_toast).show();
                responseBody = response;
                Log.wtf("ADA_LOG", "String Response From Server Is: " + responseBody);

                progress.dismiss();

                deleteCertificate();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                StyleableToast.makeText(MainActivity.this, "Fail to get response : " + error, Toast.LENGTH_LONG, R.style.error_toast).show();
                Log.wtf("ADA_LOG", "Fail to get response : ", error);
            }
        }) {        // this is the relevant method
            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = "";

                try {
                    httpPostBody = URLEncoder.encode(passingData, "UTF-8");

                } catch (UnsupportedEncodingException exception) {

                    Log.wtf("ADA_LOG", "Fail to Post Body : ", exception);
                    StyleableToast.makeText(MainActivity.this, "Fail to Post Body : " + exception, Toast.LENGTH_LONG, R.style.error_toast).show();

                    return null;
                }
                return httpPostBody.getBytes();
            }};
        queue.add(request);
    }

}

//The KeyStore is available from Android 4.3 (API level 18). There are slight differences between versions : https://stackoverflow.com/questions/38159990/generate-csr-from-private-key-or-key-store
//Where to store certificate: https://nelenkov.blogspot.com/2011/11/ics-credential-storage-implementation.html

