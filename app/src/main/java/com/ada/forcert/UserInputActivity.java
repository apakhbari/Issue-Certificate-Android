package com.ada.forcert;

import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ada.forcert.utilities.UserCredentials;
import com.google.android.material.textfield.TextInputEditText;
import com.shuhart.stepview.StepView;

import java.io.Serializable;

import io.github.muddz.styleabletoast.StyleableToast;

public class UserInputActivity extends AppCompatActivity {
    private TextInputEditText et_faFirstName;
    private TextInputEditText et_faLastName;
    private TextInputEditText et_enFirstName;
    private TextInputEditText et_enLastName;
    private TextInputEditText et_nationalCode;
    private TextInputEditText et_birthCertificateCode;
    private TextInputEditText et_telephone;
    private TextInputEditText et_postalCode;
    private TextInputEditText et_provinceName;
    private TextInputEditText et_city;
    private TextInputEditText et_birthDate;

    private ToggleButton tb_toggleButtonGender;
    private ToggleButton tb_toggleButtonForeign;
    private ToggleButton tb_toggleButtonTest;
    private ToggleButton tb_toggleButtonServer;

    private AppCompatButton btnCopyPublicKey;
    private AppCompatButton btnCopyPrivateKey;
    private AppCompatButton btnCopyPassingJson;
    private AppCompatButton btnNextFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_input);

        et_faFirstName = (TextInputEditText) findViewById(R.id.TIET_faFirstName);
        et_faLastName = (TextInputEditText) findViewById(R.id.TIET_faLastName);
        et_enFirstName = (TextInputEditText) findViewById(R.id.TIET_enFirstName);
        et_enLastName = (TextInputEditText) findViewById(R.id.TIET_enLastName);
        et_nationalCode = (TextInputEditText) findViewById(R.id.TIET_nationalCode);
        et_birthCertificateCode = (TextInputEditText) findViewById(R.id.TIET_birthCertificateCode);
        et_telephone = (TextInputEditText) findViewById(R.id.TIET_telephone);
        et_postalCode = (TextInputEditText) findViewById(R.id.TIET_postalCode);
        et_provinceName = (TextInputEditText) findViewById(R.id.TIET_provinceName);
        et_city = (TextInputEditText) findViewById(R.id.TIET_city);
        et_birthDate = (TextInputEditText) findViewById(R.id.TIET_birthDate);

        tb_toggleButtonGender = (ToggleButton) findViewById(R.id.toggleButtonGender);
        tb_toggleButtonForeign = (ToggleButton) findViewById(R.id.toggleButtonForeign);
        tb_toggleButtonTest = (ToggleButton) findViewById(R.id.toggleButtonTest);
        tb_toggleButtonServer = (ToggleButton) findViewById(R.id.toggleButtonServer);



        AppCompatButton btnNextActivity= (AppCompatButton) findViewById(R.id.btn_NextActivity);
        btnNextActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toNextActivity();
            }
        });

        StepView stepView= (StepView) findViewById(R.id.step_view);
        stepView.getState()
                .animationType(StepView.ANIMATION_CIRCLE)
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(UserInputActivity.this, R.font.vazir))
                .commit();
    }

    private void toNextActivity(){
        if ( trimm(et_faFirstName.getText().toString()).isEmpty() ||
                trimm(et_faLastName.getText().toString()).isEmpty() ||
                trimm(et_enFirstName.getText().toString()).isEmpty() ||
                trimm(et_enLastName.getText().toString()).isEmpty() ||
                trimm(et_nationalCode.getText().toString()).isEmpty() ||
                trimm(et_birthCertificateCode.getText().toString()).isEmpty() ||
                trimm(et_telephone.getText().toString()).isEmpty() ||
                trimm(et_postalCode.getText().toString()).isEmpty() ||
                trimm(et_provinceName.getText().toString()).isEmpty() ||
                trimm(et_city.getText().toString()).isEmpty() ||
                trimm(et_birthDate.getText().toString()).isEmpty())
        {
            StyleableToast.makeText(UserInputActivity.this, "لطفاً همه فیلدها را کامل کنید", Toast.LENGTH_LONG, R.style.error_toast).show();
        }
        else {
            UserCredentials userCredentials = new UserCredentials();

            userCredentials.setFaFirstName(trimm(et_faFirstName.getText().toString()));
            userCredentials.setFaLastName(trimm(et_faLastName.getText().toString()));
            userCredentials.setEnFirstName(trimm(et_enFirstName.getText().toString()));
            userCredentials.setEnLastName(trimm(et_enLastName.getText().toString()));
            userCredentials.setNationalCode(trimm(et_nationalCode.getText().toString()));
            userCredentials.setBirthCertificateCode(trimm(et_birthCertificateCode.getText().toString()));
            userCredentials.setTelephone(trimm(et_telephone.getText().toString()));
            userCredentials.setPostalCode(trimm(et_postalCode.getText().toString()));
            userCredentials.setProvinceName(trimm(et_provinceName.getText().toString()));
            userCredentials.setCity(trimm(et_city.getText().toString()));
            userCredentials.setBirthDate(trimm(et_birthDate.getText().toString()));

            if (String.valueOf(tb_toggleButtonForeign.isChecked()) == "true")
            {userCredentials.setIsForeign(trimm("true"));
            }else { userCredentials.setIsForeign(trimm("false")); }

            if (String.valueOf(tb_toggleButtonGender.isChecked()) == "true")
            {userCredentials.setIsMan(trimm("true"));
            }else { userCredentials.setIsMan(trimm("false")); }

            if (String.valueOf(tb_toggleButtonTest.isChecked()) == "true")
            {userCredentials.setIsForTest(trimm("01"));
            }else { userCredentials.setIsForTest(trimm("08")); }

            if (String.valueOf(tb_toggleButtonServer.isChecked()) == "true")
            {userCredentials.setServerURL(trimm("https://0ff3a90b-d7f2-4e13-bc80-f8ffd89fa571.mock.pstmn.io"));
            }else { userCredentials.setServerURL(trimm("http://api.raahbartrust.ir/api")); }

            Intent createPassingDataActivityIntent = new Intent(UserInputActivity.this, CreatePassingDataActivity.class);
            createPassingDataActivityIntent.putExtra("UserCredentials", userCredentials);
            startActivity(createPassingDataActivityIntent);
        }
    }

    public String trimm(String str) {
        if (str == null || str.length() == 0) {
            return str;
        } else {
            return str.trim();
        }
    }
}
