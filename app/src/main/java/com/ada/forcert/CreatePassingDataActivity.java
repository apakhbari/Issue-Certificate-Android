package com.ada.forcert;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import com.ada.forcert.utilities.Converts;
import com.ada.forcert.utilities.KeySize;
import com.ada.forcert.utilities.UserCredentials;
import com.android.volley.toolbox.Volley;
import com.shuhart.stepview.StepView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.x500.X500Principal;
import javax.security.cert.CertificateException;

import io.github.muddz.styleabletoast.StyleableToast;

import android.util.Base64;

public class CreatePassingDataActivity extends AppCompatActivity {

    private String customerCode = "";
    private String profileName = "گواهی موبایل برنز شخص حقیقی مستقل";
    private String trackingCode;
    private String url = "";
    private String resolvedURL = "/RegisterInfo";

    private final static String PRIVATE_KEY =
            "-----BEGIN PRIVATE KEY-----\n" +
                    "MIICWwIBAAKBgQCmhxt7yU8TRLIqpftKikqonlgb4VdLgHkwSNyxQ6SONGL3l2XW\n" +
                    "MijyoGMssaSQjiZrDqqMtVYdlbwJIg44NNefGjtoi97IgMnMP8JM/ZyrvCi2IGUN\n" +
                    "jNAH8PQ7XfxCyY63AsT0ADfUeMO7/eqgm73JIBC1l6OWfw6VvfQdMsLCywIDAQAB\n" +
                    "AoGAHF4PlHqqecHOB+3WbNA5/5dZswYj9Lyh6FPBJIE9WuwZn8uukVpiF1KzEj4A\n" +
                    "po9QhBQvgWCylr+XFIc0nOaDhFR7rKeCiciEJw7sZtsEQ5Er7FHVPS0WPhPVxEtB\n" +
                    "mU2k/LTmoq7f2LwqJXz86MimjGwIVw7wWgCD+wzHTova0EECQQDSqrdplAoVr8D2\n" +
                    "F19h50VQWziRPbuulru2/m+iQqc0KLjWvQtBc6/YPiaa4tnzzbI5QRZjugYdsJWO\n" +
                    "IyCZIqAjAkEAylzXlbLR+iVV+MewQjTtlqYy2ygTiNe5JWrsdIEX4tonCY7YBFOE\n" +
                    "BOSSMkUwpwkyc39LBGnFj13rQaX74rKpOQJATEQ9rl1H4V4Fb5I6l8kQUO0VZ49/\n" +
                    "2M79Gly2sXmL6tgrQqKh5oopSRIHC9/ApAD92rhzkJsSB8GcAx84d/gPHQJAdmZ1\n" +
                    "+O+krdB0idgEh+hB7vgVH2dGbrWRbZQu/0ec+y0a1BvQxk87GkXGSEV1XvYhn7Ql\n" +
                    "51Iftm+EO2frhct0+QJAKUcxc2cAz06L1h94InTaHbSK5oYnsT0q4qsjwHi3tOGb\n" +
                    "kn/itPGS/972SBDhnBLHfo5jYfx/1N1om+V5sszt5A==\n" +
                    "-----END PRIVATE KEY-----";
    private final static String PUBLIC_KEY =
            "00:a6:87:1b:7b:c9:4f:13:44:b2:2a:a5:fb:4a:8a:\n" +
                    "4a:a8:9e:58:1b:e1:57:4b:80:79:30:48:dc:b1:43:\n" +
                    "a4:8e:34:62:f7:97:65:d6:32:28:f2:a0:63:2c:b1:\n" +
                    "a4:90:8e:26:6b:0e:aa:8c:b5:56:1d:95:bc:09:22:\n" +
                    "0e:38:34:d7:9f:1a:3b:68:8b:de:c8:80:c9:cc:3f:\n" +
                    "c2:4c:fd:9c:ab:bc:28:b6:20:65:0d:8c:d0:07:f0:\n" +
                    "f4:3b:5d:fc:42:c9:8e:b7:02:c4:f4:00:37:d4:78:\n" +
                    "c3:bb:fd:ea:a0:9b:bd:c9:20:10:b5:97:a3:96:7f:\n" +
                    "0e:95:bd:f4:1d:32:c2:c2:cb";

    private final static String SIGNATURE = "l+WSE0xJsjSajrsvgwbgfv1wWlELav4GAMq5kJSuEQZ+RgB9jIrfp++qOx1ahuW9z/VsdBCv5RgcPklXTyj9iDLjdFLsM7Ifhn1nvKN5ZMPDt/7xnwALU8KM2mWf5y2jiy+UjfoB7il/b5yNqZczzyjax3Y8IftG/hUbvWaFzYc=";
    private final static String SIGNATURE2 = "97e592134c49b2349a8ebb2f8306e07efd705a510b6afe0600cab99094ae1106 7e46007d8c8adfa7efaa3b1d5a86e5bdcff56c7410afe5181c3e49574f28fd88 32e37452ec33b21f867d67bca37964c3c3b7fef19f000b53c28cda659fe72da3 8b2f948dfa01ee297f6f9c8da99733cf28dac7763c21fb46fe151bbd6685cd87";

    static String MY_ALIAS = "ADA_TEST";
    boolean demandedKeyPairExists = false ;

    UserCredentials userCredentials;

    String jsonRequest = "";
    String signature = "" ;
    String toBeSigned = "";

    String responseBody = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_passing_data);

        AppCompatButton btnCopyPublicKey= (AppCompatButton) findViewById(R.id.btn_CopyPublicKey);
        btnCopyPublicKey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (demandedKeyPairExists){
                    //converting public key to byte
                    byte[] byte_pubkey = new byte[0];
                    try {
                        byte_pubkey = loadPublicKey().getEncoded();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnrecoverableEntryException e) {
                        e.printStackTrace();
                    } catch (java.security.cert.CertificateException e) {
                        e.printStackTrace();
                    }

                    //converting byte to String
                    String str_key = Converts.ByteArrayToBase64String(byte_pubkey);
                    str_key = str_key.replace("\n", "").replace("\r", "");

                    copyToClipBoard(("Label: PublicKey"), "-----BEGIN RSA PUBLIC KEY-----\n" + str_key + "\n" + "-----END RSA PUBLIC KEY-----");
                    Log.wtf("ADA_LOG",  "public key: \n" + "-----BEGIN RSA PUBLIC KEY-----\n" +str_key + "\n" + "-----END RSA PUBLIC KEY-----");
                    StyleableToast.makeText(CreatePassingDataActivity.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();

            } else { StyleableToast.makeText(CreatePassingDataActivity.this, "لطفاً ابتدا جفت کلید بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show(); }
            }
        });

        AppCompatButton btnGenerateAsymmetricKey= (AppCompatButton) findViewById(R.id.btn_generateAsymmetricKey);
        btnGenerateAsymmetricKey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (demandedKeyPairExists){
                    StyleableToast.makeText(CreatePassingDataActivity.this, "جفت کلید را قبلاً ساخته‌اید!", Toast.LENGTH_LONG, R.style.error_toast).show();

                }else {
                    try {
                        generateAsymmetricKey();

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        AppCompatButton btnCopyPassingJson= (AppCompatButton) findViewById(R.id.btn_CopyPassingJson);
        btnCopyPassingJson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (demandedKeyPairExists){
                    try {
                        copyToClipBoard(("Label: PassingJson"), createPassingJson());

                    } catch (UnrecoverableEntryException e) {
                        e.printStackTrace();
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SignatureException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (java.security.cert.CertificateException | InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                    StyleableToast.makeText(CreatePassingDataActivity.this, "کپی روی کلیپ بورد انجام شد.", Toast.LENGTH_LONG, R.style.just_toast).show();

            }else { StyleableToast.makeText(CreatePassingDataActivity.this, "لطفاً ابتدا جفت کلید بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show(); }
            }
        });

        AppCompatButton btnNextActivity= (AppCompatButton) findViewById(R.id.btn_NextActivity);
        btnNextActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (jsonRequest.isEmpty()){
                    StyleableToast.makeText(CreatePassingDataActivity.this, "لطفاً جیسون ارسالی به سرور را بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show();
                }else {
                    postDataUsingVolley(url+resolvedURL, jsonRequest);
                }

            }
        });


        StepView stepView= (StepView) findViewById(R.id.step_view);
        stepView.getState()
                .animationType(StepView.ANIMATION_CIRCLE)
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(CreatePassingDataActivity.this, R.font.vazir))
                .commit();
        stepView.go(1, true);


        try {
            if (checkKeyStoreForDemandedAlias())
            {
                demandedKeyPairExists = true;
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

    private void toNextActivity() throws IOException, JSONException {
            Intent intent = new Intent(CreatePassingDataActivity.this, ServerResponseActivity.class);
            intent.putExtra("ReceivedFromServer", responseBody);
            intent.putExtra("UserCredentials", userCredentials);
            startActivity(intent);
    }

    private void postDataUsingVolley(String url, String passingData) {

        ProgressDialog progress = new ProgressDialog(CreatePassingDataActivity.this);
        progress.setMessage("Please Wait ...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(CreatePassingDataActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url,  new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                StyleableToast.makeText(CreatePassingDataActivity.this, "ارتباط موفقیت آمیز انجام شد." , Toast.LENGTH_LONG, R.style.just_toast).show();
                responseBody = response;
                Log.wtf("ADA_LOG", "String Response From Server Is: " + responseBody);

                progress.dismiss();

                try {
                    toNextActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                StyleableToast.makeText(CreatePassingDataActivity.this, "Fail to get response : " + error, Toast.LENGTH_LONG, R.style.error_toast).show();
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
                StyleableToast.makeText(CreatePassingDataActivity.this, "Fail to Post Body : " + exception, Toast.LENGTH_LONG, R.style.error_toast).show();

                return null;
            }
            return httpPostBody.getBytes();
        }};
        queue.add(request);
    }

    private String createPassingJson() throws UnrecoverableEntryException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, java.security.cert.CertificateException, InvalidKeySpecException {
        Intent createPassingDataActivityIntent = getIntent();
        userCredentials = (UserCredentials)createPassingDataActivityIntent.getSerializableExtra("UserCredentials");
        toBeSigned = (customerCode + "-" + userCredentials.getNationalCode());
        signature = sign(stringToPrivateKey(), toBeSigned );
        signature = signature.replace("\n", "").replace("\r", "");
        signature = SIGNATURE2;

        jsonRequest = "{\n" +
                "caName:\""+ userCredentials.getIsForTest() +"\",\n" +
                "customercode:\""+ customerCode+"\",\n" +
                "profileName:\""+ profileName+"\",\n" +
                "signature:\""+ signature.trim() +"\",\n" +
                "personData:{\n" +
                "\tName:\""+ (userCredentials.getFaFirstName()) +"\",\n" +
                "\tLastName:\""+ (userCredentials.getFaLastName()) +"\",\n" +
                "\tEnName:\""+ (userCredentials.getEnFirstName()) +"\",\n" +
                "\tEnLastName:\""+ (userCredentials.getEnLastName()) +"\",\n" +
                "\tNationalCode:\""+ (userCredentials.getNationalCode()) +"\",\n" +
                "\tIsMan:"+ (userCredentials.getIsMan() ) +",\n" +
                "\tPostalCode:\""+ (userCredentials.getPostalCode()) +"\",\n" +
                "\tTelephone:\""+ (userCredentials.getTelephone()) +"\",\n" +
                "\tIsForeign:"+ (userCredentials.getIsForeign()) + ",\n" +
                "\tBirthCertificateID:\""+ (userCredentials.getBirthCertificateCode()) +"\",\n" +
                "\tBirthDate:\""+ (userCredentials.getBirthDate()) +"\",\n" +
                "\tCity:\""+ (userCredentials.getCity()) +"\",\n" +
                "\tProvinceName:\""+ (userCredentials.getProvinceName()) +"\"\n" +
                "\t}\n" +
                "}";

        url = userCredentials.getServerURL();
        Log.wtf("ADA_LOG", "toBeSigned is:\n" + toBeSigned);
        Log.wtf("ADA_LOG", "jsonRequest is:\n" + jsonRequest + "\nurl is: " + url);
        Log.wtf("ADA_LOG", "verify is: " + verify(toBeSigned,signature,loadPublicKey()));

        return jsonRequest;
    }

    private PrivateKey loadPrivateKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, java.security.cert.CertificateException {

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            boolean isExist = false;

            PrivateKey privateKey = null;
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Log.wtf("ADA_LOG", "Aliases: " + alias);
                if (alias.equals(MY_ALIAS)) {
                    Log.wtf("ADA_LOG", "Match!");
                    isExist = true;
                    KeyStore.Entry entry = ks.getEntry(MY_ALIAS, null);
                    if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                        Log.w("ADA_LOG", "Not an instance of a PrivateKeyEntry");
                        continue;
                    }
                    privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
                    return privateKey;
                }
            }
            return null;
    }

    private PublicKey loadPublicKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, java.security.cert.CertificateException {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();

            boolean isExist = false;

            PublicKey publicKey = null;
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Log.wtf("ADA_LOG", "Aliases: " + alias);
                if (alias.equals(MY_ALIAS)) {
                    Log.wtf("ADA_LOG", "Match!");
                    isExist = true;
                    KeyStore.Entry entry = ks.getEntry(MY_ALIAS, null);
                    publicKey = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
                    return publicKey;
                }
            }
            return null;
        }

    private boolean checkKeyStoreForDemandedAlias() throws java.security.cert.CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException  {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            Enumeration<String> aliases = keyStore.aliases();

            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Log.wtf("ADA_LOG", "Aliases: " + alias);
                if (alias.equals(MY_ALIAS)) {
                    StyleableToast.makeText(CreatePassingDataActivity.this, "جفت کلید را قبلاً ساخته‌اید.", Toast.LENGTH_LONG, R.style.just_toast).show();
                    Log.wtf("ADA_LOG", "Match!");
                    return true;
                }
            }
        StyleableToast.makeText(CreatePassingDataActivity.this, "جفت کلیدی وجود ندارد! لطفاً ابتدا جفت کلید بسازید.", Toast.LENGTH_LONG, R.style.error_toast).show();
        return false;
    }

    private String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        //signature.update(Converts.base64ToByteArray(message));
        signature.update(Converts.stringUTF32ToBytes(message));
        byte[] signatureBytes = signature.sign();
        return Converts.ByteArrayToBase64String(signatureBytes);
    }

/*    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }*/

    private boolean verify(String b64Message, String b64Signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] streamMessage = Converts.base64ToByteArray(b64Message);
        byte[] sigBytes = Converts.base64ToByteArray(b64Signature);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(streamMessage);
        boolean result = signature.verify(sigBytes);
        return result;
    }

    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) CreatePassingDataActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }



    private PrivateKey stringToPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read in the key into a String
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(PRIVATE_KEY));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }

        // Remove the "BEGIN" and "END" lines, as well as any whitespace

        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");

        // Base64 decode the result

        byte [] pkcs8EncodedBytes = Base64.decode(pkcs8Pem, Base64.DEFAULT);

        // extract the private key

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);

        //Log.wtf("ADA_LOG", "Fail to get response : ", privKey);
        System.out.println(privKey);

        return privKey;
    }

    private PublicKey byteToPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Converts.base64ToByteArray(PUBLIC_KEY)));
        return publicKey;
    }

    private void generateAsymmetricKey() throws NoSuchAlgorithmException,NoSuchProviderException, InvalidAlgorithmParameterException  {
        KeyPairGenerator kpGenerator = null;
            kpGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

            kpGenerator.initialize(
                    new KeyGenParameterSpec.Builder(MY_ALIAS, KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                            .setRandomizedEncryptionRequired(false)
                            .setDigests(      //Set of digests algorithms with which the key can be used
                                    KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5,
                                    KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224,
                                    KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384,
                                    KeyProperties.DIGEST_SHA512)
                            .setKeySize(KeySize.BIT_2048.value())
                            .setEncryptionPaddings(
                                    KeyProperties.ENCRYPTION_PADDING_NONE,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .setSignaturePaddings(      //Set of padding schemes with which the key can be used when signing/verifying
                                    KeyProperties.SIGNATURE_PADDING_RSA_PKCS1,
                                    KeyProperties.SIGNATURE_PADDING_RSA_PSS)
                            .setCertificateSubject(new X500Principal(       //Subject used for the self-signed certificate of the generated key pair, default is CN=fake
                                    "CN=Android, O=Android Authority"))
                            .setCertificateSerialNumber(new BigInteger(256, new Random()))      //Serial number used for the self-signed certificate of the generated key pair, default is 1
                            .setUserAuthenticationRequired(false)       //Sets whether this key is authorized to be used only if the user has been authenticated, default false
                            //.setUserAuthenticationValidityDurationSeconds(500)      //Duration(seconds) for which this key is authorized to be used after the user is successfully authenticated
                            // .setCertificateNotBefore()       //Start of the validity period for the self-signed certificate of the generated, default Jan 1 1970
                            //.setCertificateNotAfter()     //End of the validity period for the self-signed certificate of the generated key, default Jan 1 2048
                            .build());
            kpGenerator.generateKeyPair();

        demandedKeyPairExists = true;

            StyleableToast.makeText(CreatePassingDataActivity.this, "جفت کلید ساخته شد.", Toast.LENGTH_LONG, R.style.just_toast).show();
        }
    }