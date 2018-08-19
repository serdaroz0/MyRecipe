package com.daxstyle.recipe.activity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.daxstyle.recipe.R;
import com.daxstyle.recipe.helper.Services;
import com.daxstyle.recipe.helper.Util;

import java.util.Date;


public class LoginActivity extends AppCompatActivity {
    Spinner spnGender, spnBirthDate, spnBirthDate2, spnBirthDate3;
    EditText edtName, edtSurname, edtPhoneNumber, edtEmail;
    boolean previouslyStarted;
    String currentDateandTime;
    TextView tvPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spnGender = findViewById(R.id.spnGender);
        edtName = findViewById(R.id.edtName);
        spnBirthDate = findViewById(R.id.spnBirthDate);
        spnBirthDate2 = findViewById(R.id.spnBirthDate2);
        spnBirthDate3 = findViewById(R.id.spnBirthDate3);
        edtSurname = findViewById(R.id.edtSurname);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        edtEmail = findViewById(R.id.edtEmail);
        Util.setSoftKeys(this);
//        Locale locale3 = new Locale("tr");
//        Locale.setDefault(locale3);
//        Configuration config2 = new Configuration();
//        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
//        loadLocale();
        previouslyStarted = Util.getPrefBoolean(this, "firstTime");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(adapter);
        ArrayAdapter<CharSequence> days = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_dropdown_item);
        spnBirthDate.setAdapter(days);
        ArrayAdapter<CharSequence> months = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_dropdown_item);
        spnBirthDate2.setAdapter(months);
        ArrayAdapter<CharSequence> years = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_dropdown_item);
        spnBirthDate3.setAdapter(years);
        if (previouslyStarted) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void save(View view) {
        String birthDate = spnBirthDate.getSelectedItem().toString() + "/" + spnBirthDate2.getSelectedItem().toString() + "/" + spnBirthDate3.getSelectedItem().toString();

        if (edtName.getText().length() == 0) {
            edtName.setError(getString(R.string.required_field));
            return;
        }
        if (edtSurname.getText().length() == 0) {
            edtSurname.setError(getString(R.string.required_field));
            return;
        }
        if (edtPhoneNumber.getText().length() < 10) {
            edtPhoneNumber.setError(getString(R.string.required_field));
            return;
        }
        if (edtEmail.getText().length() < 7 || !edtEmail.getText().toString().contains("@")) {
            edtEmail.setError(getString(R.string.required_field));

        } else {
            Util.showToast(this, R.string.saved_toast);
            Intent intent = new Intent(this, MainActivity.class);
            SimpleDateFormat sdf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
                currentDateandTime = sdf.format(new Date());
            }
            Services.getInstance().sendPost(this, edtSurname.getText().toString(), birthDate, spnGender.getSelectedItem().toString(), edtEmail.getText().toString(), edtPhoneNumber.getText().toString(), currentDateandTime, edtName.getText().toString(), new Services.OnFinishListener() {
                @Override
                public void onFinish(Object obj) {
//              Result result = (Result) obj;
                }
            }, true);
            Util.setPrefBoolean(this, "firstTime", true);
            startActivity(intent);
        }
    }

    public void privacy(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/daxstyle/home"));
        startActivity(browserIntent);
    }

//    public void loadLocale() {
//        String langPref = "Language";
//        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
//                Activity.MODE_PRIVATE);
//        String language = prefs.getString(langPref, "");
//        changeLang(language);
//    }
//
//    public void changeLang(String lang) {
//        if (lang.equalsIgnoreCase(""))
//            return;
//        Locale myLocale = new Locale(lang);
//        saveLocale(lang);
//        android.content.res.Configuration config = new android.content.res.Configuration();
//        config.locale = myLocale;
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//    }
//
//    public void saveLocale(String lang) {
//        String langPref = "Language";
//        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
//                Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(langPref, lang);
//        editor.apply();
//    }


}



