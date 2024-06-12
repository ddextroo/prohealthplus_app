package com.ddev.prohealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserInfoActivity extends AppCompatActivity {

    private LinearLayout linear6;
    private LinearLayout linear2;
    private TextView textview1;
    private TextView textview4;
    private TextView labeltextview;
    private EditText editText;

    private CheckBox diabetes;
    private CheckBox highblood;
    private CheckBox asthma;
    private CheckBox copd;
    private CheckBox heart;
    private CheckBox kidney;
    private CheckBox arthritis;
    private CheckBox epilepsy;
    private CheckBox depression;
    private CheckBox anxiety;
    private CheckBox gerd;
    private CheckBox bowel;
    private CheckBox osteoporosis;
    private CheckBox hepatitis;
    private CheckBox psoriasis;

    private Intent i = new Intent();
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.user_info);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        linear6 = findViewById(R.id.next);
        linear2 = findViewById(R.id.linear2);
        textview1 = findViewById(R.id.textview1);
        textview4 = findViewById(R.id.textview4);
        labeltextview = findViewById(R.id.labeltextview);
        editText = findViewById(R.id.editText);
        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        diabetes = findViewById(R.id.diabetes);
        highblood = findViewById(R.id.highblood);
        asthma = findViewById(R.id.asthma);
        copd = findViewById(R.id.copd);
        heart = findViewById(R.id.heart);
        kidney = findViewById(R.id.kidney);
        arthritis = findViewById(R.id.arthritis);
        epilepsy = findViewById(R.id.epilepsy);
        depression = findViewById(R.id.depression);
        anxiety = findViewById(R.id.anxiety);
        gerd = findViewById(R.id.gerd);
        bowel = findViewById(R.id.bowel);
        osteoporosis = findViewById(R.id.osteoporosis);
        hepatitis = findViewById(R.id.hepatitis);
        psoriasis = findViewById(R.id.psoriasis);

        linear6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    saveUserData();
                    i.setClass(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                showMessage("Your name is empty");
                }
            }
        });
    }

    private void initializeLogic() {
        textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
        textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
        labeltextview.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);

        diabetes.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        highblood.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        asthma.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        copd.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        heart.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        kidney.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        arthritis.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        epilepsy.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        depression.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        anxiety.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        gerd.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        bowel.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        osteoporosis.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        hepatitis.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        psoriasis.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);

        editText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
        _rippleRoundStroke(linear2, "#ffffff", "#ffffff", 25, 2.5, "#e0e0e0");
        _rippleRoundStroke(linear6, "#008037", "#ffffff", 25, 0, "#fefefe");
    }

    private void saveUserData() {
        String name = editText.getText().toString();
        Map<String, Boolean> conditions = new HashMap<>();

        conditions.put("diabetes", diabetes.isChecked());
        conditions.put("highblood", highblood.isChecked());
        conditions.put("asthma", asthma.isChecked());
        conditions.put("copd", copd.isChecked());
        conditions.put("heart", heart.isChecked());
        conditions.put("kidney", kidney.isChecked());
        conditions.put("arthritis", arthritis.isChecked());
        conditions.put("epilepsy", epilepsy.isChecked());
        conditions.put("depression", depression.isChecked());
        conditions.put("anxiety", anxiety.isChecked());
        conditions.put("gerd", gerd.isChecked());
        conditions.put("bowel", bowel.isChecked());
        conditions.put("osteoporosis", osteoporosis.isChecked());
        conditions.put("hepatitis", hepatitis.isChecked());
        conditions.put("psoriasis", psoriasis.isChecked());

        // Store in SharedPreferences
        SharedPreferences.Editor editor = user.edit();
        editor.putString("name", name);
        Gson gson = new Gson();
        String jsonConditions = gson.toJson(conditions);
        editor.putString("conditions", jsonConditions);
        editor.apply();
    }

    public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(Color.parseColor(_focus));
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                Color.parseColor("#" + _strokeclr.replace("#", "")));
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double)_arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
