package com.ada.forcert;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.security.KeyChain;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.ada.forcert.utilities.CertificateCredentials;
import com.ada.forcert.utilities.Converts;
import com.shuhart.stepview.StepView;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.security.cert.CertificateException;

import io.github.muddz.styleabletoast.StyleableToast;

public class Issued extends AppCompatActivity {

    static String MY_ALIAS = "ADA_TEST";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_issued);

        String data = getIntent().getStringExtra("ReceivedFromServer");
        CertificateCredentials certificateCredentials = new CertificateCredentials();


        TextView tv1 = (TextView)findViewById(R.id.textView1);
        tv1.setText(data);

        certificateCredentials.setIsSuccess(StringUtils.substringBetween(data, "IsSuccess = ", ","));
        certificateCredentials.setCertificate(StringUtils.substringBetween(data, "Certificate = ", ","));
        certificateCredentials.setPaymentStatus(StringUtils.substringBetween(data, "PaymentStatus = ", ","));
        certificateCredentials.setDescription(StringUtils.substringBetween(data, "Description = ", ","));
        certificateCredentials.setCN(StringUtils.substringBetween(data, "CN = ", ","));
        certificateCredentials.setIssuerName(StringUtils.substringBetween(data, "IssuerName = ", ","));
        certificateCredentials.setValidFrom(StringUtils.substringBetween(data, "ValidFrom = ", ","));
        certificateCredentials.setValidTo(StringUtils.substringBetween(data, "ValidTo = ", "\n"));


        AppCompatButton CopyReceivingJson= (AppCompatButton) findViewById(R.id.btn_CopyReceivingJson);
        CopyReceivingJson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                copyToClipBoard("ReceivedFromServer", data);
                StyleableToast.makeText(Issued.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();
                Log.wtf("ADA_LOG", "ReceivedFromServer is: \n" + data);
            }
        });

        AppCompatButton NextActivity= (AppCompatButton) findViewById(R.id.btn_NextActivity);
        NextActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(Issued.this, MainActivity.class);
                MainIntent.putExtra("CertificateCredentials", certificateCredentials);
                startActivity(MainIntent);
            }
        });

        StepView stepView= (StepView) findViewById(R.id.step_view);
        stepView.getState()
                .animationType(StepView.ANIMATION_CIRCLE)
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(Issued.this, R.font.vazir))
                .commit();
        stepView.go(3, true);
    }


/*    void importCertificateIntoAndroid(String certStr) throws CertificateException, KeyStoreException {
        try {
            KeyStore pk12KeyStore = KeyStore.getInstance("PKCS12");
            pk12KeyStore.load(null, null);
            ByteArrayInputStream is = new ByteArrayInputStream(certStr.getBytes());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(is);
            pk12KeyStore.setKeyEntry("server.name", "privateKey", "".toCharArray(), new Certificate[]{cert});
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            pk12KeyStore.store(os, "".toCharArray());
            Intent certInstallIntent = KeyChain.createInstallIntent();
            certInstallIntent.putExtra(KeyChain.EXTRA_PKCS12, String.valueOf(os.toByteArray()));    //certInstallIntent.putExtra(KeyChain.EXTRA_PKCS12, String.valueOf(os));
            certInstallIntent.putExtra(KeyChain.EXTRA_KEY_ALIAS, "server.name");
            certInstallIntent.putExtra(KeyChain.EXTRA_NAME,  "server.name");
            startActivity(certInstallIntent);
        } catch (Exception e) {
            Log.d("TAG", "help");
        }
    }

        public static byte[] createPKCS12InMemory(byte[] x509AsPEM, PrivateKey privKey, String pkcs12Password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, java.security.cert.CertificateException {
        InputStream stream = new ByteArrayInputStream(x509AsPEM);
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) fact.generateCertificate(stream);
        KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
        pkcs12.load(null, null);
        pkcs12.setKeyEntry("device_certificate", privKey, pkcs12Password.toCharArray(), new Certificate[] {cert});
        ByteArrayOutputStream p12 = new ByteArrayOutputStream();
        pkcs12.store(p12, pkcs12Password.toCharArray());
        return p12.toByteArray();
    }
    */



    private void getCertsFromP12(String pathToFile, String passphrase){
        try {
            KeyStore p12 = KeyStore.getInstance("pkcs12");
            p12.load(new FileInputStream(pathToFile), passphrase.toCharArray());
            Enumeration e = p12.aliases();
            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();
                X509Certificate c = (X509Certificate) p12.getCertificate(alias);
                addCertificateToKeyStore(c);
            }
        } catch (Exception e) {}
    }

    private void addCertificateToKeyStore(String input) {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());

            //is = Converts.base64ToByteArray(input);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(is);

            //X509Certificate c = (X509Certificate) p12.getCertificate(alias);
            //addCertificateToKeyStore(c);

            ks.setCertificateEntry(MY_ALIAS+"_signed", cert);

            X509Certificate c = (X509Certificate) ks.getCertificate(MY_ALIAS+"_signed");
        } catch (Exception e){}
    }

    private void addCertificateToKeyStore(X509Certificate c) {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            ks.setCertificateEntry("myCertAlias", c);
            ks.getCertificate("Aliias");
        } catch (Exception e){}
    }

    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) Issued.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
