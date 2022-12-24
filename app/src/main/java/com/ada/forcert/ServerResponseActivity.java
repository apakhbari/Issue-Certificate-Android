package com.ada.forcert;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.ada.forcert.utilities.Converts;
import com.ada.forcert.utilities.UserCredentials;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shuhart.stepview.StepView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x509.BasicConstraints;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.asn1.x509.ExtensionsGenerator;
import org.spongycastle.openssl.PEMWriter;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.spongycastle.util.io.pem.PemObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.Enumeration;

import javax.security.cert.CertificateException;

import io.github.muddz.styleabletoast.StyleableToast;

public class ServerResponseActivity extends AppCompatActivity {

    private String customerCode = "";
    private String profileName = "گواهی موبایل برنز شخص حقیقی مستقل";
    private String url = "";
    private String resolvedURL = "/IssueCertificateByTrackingCode";
    private String paymentId = "10320408934-4500";

    static String MY_ALIAS = "ADA_TEST";

    String isSuccess = "";
    String trackingCode = "";
    String Description = "";
    String ErrorMessage = "";
    String ErrorCode = "";


    String responseBody = "";
    String signature = "";
    String jsonRequest = "";

    String csr = "";

    UserCredentials userCredentials;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_response);

        Intent ServerResponseActivity = getIntent();
        userCredentials = (UserCredentials) ServerResponseActivity.getSerializableExtra("UserCredentials");

        url = userCredentials.getServerURL();

        String data = getIntent().getStringExtra("ReceivedFromServer");

        Log.wtf("ADA_LOG", "data is: " +data);


        trackingCode = StringUtils.substringBetween(data, "TrackingCode:", ",");
        TextView tv2 = (TextView)findViewById(R.id.textView2);
        tv2.setText("TrackingCode: " + trackingCode);
        userCredentials.setTrackingCode(trackingCode);


        Description = StringUtils.substringBetween(data, "Description:", ",");
        TextView tv3 = (TextView)findViewById(R.id.textView3);
        tv3.setText(data);


        AppCompatButton CopyReceivingJson= (AppCompatButton) findViewById(R.id.btn_CopyReceivingJson);
        CopyReceivingJson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                copyToClipBoard("ReceivedFromServer", data);
                StyleableToast.makeText(ServerResponseActivity.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();
                Log.wtf("ADA_LOG", "ReceivedFromServer is: \n" + data);

            }
        });

        AppCompatButton btnCopyCSR= (AppCompatButton) findViewById(R.id.btn_CopyCSR);
        btnCopyCSR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    PKCS10CertificationRequest pkcs10CertificationRequest = generateCSR(MY_ALIAS);

                    PemObject pemObject = new PemObject("CERTIFICATE REQUEST", pkcs10CertificationRequest.getEncoded());
                    StringWriter str = new StringWriter();
                    PEMWriter pemWriter = new PEMWriter(str);
                    pemWriter.writeObject(pemObject);
                    pemWriter.close();
                    str.close();

                    csr = String.valueOf(str);

                    copyToClipBoard("pkcs10CertificationRequest", String.valueOf(str) + "\nSignature: " + "\n" + signature );
                    Log.wtf("ADA_LOG", "pkcs10CertificationRequest is: \n" + String.valueOf(str) + "Signature is : " + "\n" + signature);

                    StyleableToast.makeText(ServerResponseActivity.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OperatorCreationException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (java.security.cert.CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnrecoverableEntryException e) {
                    e.printStackTrace();
                }
            }
        });

        AppCompatButton btnCopyPassingJson= (AppCompatButton) findViewById(R.id.btn_CopyPassingJson);
        btnCopyPassingJson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (signature.isEmpty() || csr.isEmpty() ) {
                    StyleableToast.makeText(ServerResponseActivity.this, "لطفاً ابتدا CSR بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show();
                } else {
                    createPassingJson();
                    copyToClipBoard("jsonRequest", jsonRequest);
                    StyleableToast.makeText(ServerResponseActivity.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();
                    Log.wtf("ADA_LOG", "jsonRequest is: \n" + jsonRequest);
                }

            }
        });

        AppCompatButton btnNextActivity= (AppCompatButton) findViewById(R.id.btn_NextActivity);
        btnNextActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (jsonRequest.isEmpty()){
                    StyleableToast.makeText(ServerResponseActivity.this, "لطفاً ابتدا  JSON را بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show();
                }
                else {
                    postDataUsingVolley(url+resolvedURL, jsonRequest);
                }
            }
        });

        StepView stepView= (StepView) findViewById(R.id.step_view);
        stepView.getState()
                .animationType(StepView.ANIMATION_CIRCLE)
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(ServerResponseActivity.this, R.font.vazir))
                .commit();
        stepView.go(2, true);

    }

    private void toNextActivity() {
        Intent intent = new Intent(ServerResponseActivity.this, Issued.class);
        intent.putExtra("ReceivedFromServer", responseBody);
        startActivity(intent);
    }

    private void postDataUsingVolley(String url, String passingData) {

        ProgressDialog progress = new ProgressDialog(ServerResponseActivity.this);
        progress.setMessage("Please Wait ...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(ServerResponseActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url,  new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                StyleableToast.makeText(ServerResponseActivity.this, "ارتباط موفقیت آمیز انجام شد." , Toast.LENGTH_LONG, R.style.just_toast).show();
                responseBody = response;
                Log.wtf("ADA_LOG", "String Response From Server Is: " + responseBody);

                progress.dismiss();

                toNextActivity();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                StyleableToast.makeText(ServerResponseActivity.this, "Fail to get response : " + error, Toast.LENGTH_LONG, R.style.error_toast).show();
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
                    StyleableToast.makeText(ServerResponseActivity.this, "Fail to Post Body : " + exception, Toast.LENGTH_LONG, R.style.error_toast).show();

                    return null;
                }
                return httpPostBody.getBytes();
            }};
        queue.add(request);
    }

    private void createPassingJson() {

        signature = signature.replace("\n", "").replace("\r", "");
        csr = csr.replace("\n", "").replace("\r", "");

        jsonRequest = "{\n" +
                "caName:\""+ userCredentials.getIsForTest() +"\",\n" +
                "profileName:\""+ profileName+"\",\n" +
                "trackingCode:\""+ trackingCode +"\",\n" +
                "csr:\""+ StringUtils.substringBetween(csr, "-----BEGIN CERTIFICATE REQUEST-----\n", "\n-----END CERTIFICATE REQUEST-----\n") +"\",\n" +
                "paymentId:\""+ paymentId +"',\n" +
                "signature:\""+ StringUtils.substringBetween(signature, "<Signature>\n", "\n</Signature>") +"\",\n" +
                "customercode:\""+ customerCode+"\",\n" +
                "}";

        Log.wtf("ADA_LOG", "jsonRequest: " + jsonRequest);
        Log.wtf("ADA_LOG ", "url is: " + url);
    }

    //Create the certificate signing request (CSR) from private and public keys
    public PKCS10CertificationRequest generateCSR(String MY_ALIAS) throws IOException, OperatorCreationException, KeyStoreException, java.security.cert.CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {

        String CN_PATTERN =
                "CN=" + ("bronzeCert_Person_APK: "  +  userCredentials.getEnLastName() + " " + userCredentials.getEnLastName() + " "  + userCredentials.getNationalCode() )
                        +", O=ADA"
                        + ", L=" + userCredentials.getCity()
                        + ", ST=" + userCredentials.getCity()
                        +", C=IR";

        Log.wtf("ADA_LOG", "CN_PATTERN is: " + CN_PATTERN);
        String principal = String.format(CN_PATTERN);
        Log.wtf("ADA_LOG", "principal is: " + principal);


        //loading KeyPair
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        KeyStore.Entry entry = keyStore.getEntry(MY_ALIAS, null);
        PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
        PublicKey publicKey = keyStore.getCertificate(MY_ALIAS).getPublicKey();

        //Continue to Generate CSR
        ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").build(privateKey);

        PKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(
                new X500Name(principal), publicKey);

        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        extensionsGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(
                true));
        csrBuilder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                extensionsGenerator.generate());
        PKCS10CertificationRequest csr = csrBuilder.build(signer);

        signature = "<Signature>\n" + Converts.ByteArrayToBase64String(csr.getSignature()) + "</Signature>";

        return csr;
    }


    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) ServerResponseActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
