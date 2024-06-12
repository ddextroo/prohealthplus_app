package com.ddev.prohealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
	

	private String filePath = "";
	private String dataPath = "";
	private String fileName = "";
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private ImageView imageview1;
	private TextView textview4;
	private TextView textview1;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private TextView textview2;
	private TextView textview3;
	private TextView detection;
	private TextView name;
	private TextView textview10;
	private TextView advice;

	private Intent i = new Intent();
	private static final int PICK_IMAGE_REQUEST = 1;



	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		imageview1 = findViewById(R.id.imageview1);
		textview4 = findViewById(R.id.textview4);
		textview1 = findViewById(R.id.textview1);
		linear4 = findViewById(R.id.linearDetected);
		linear5 = findViewById(R.id.linear5);
		textview2 = findViewById(R.id.detectedTitle);
		textview3 = findViewById(R.id.detected);
		detection = findViewById(R.id.detection);
		textview10 = findViewById(R.id.textview10);
		name = findViewById(R.id.name);
		advice = findViewById(R.id.advice);

		linear4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), CameraActivity.class);
				startActivity(i);
			}
		});
		
		linear5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				pickImage();
			}
		});
	}
	private void pickImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*"); // Set MIME type to filter only images
		startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			// Get the selected image URI
			Uri selectedImageUri = data.getData();

			i.setClass(getApplicationContext(), ViewActivity.class);
			i.putExtra("img", selectedImageUri.toString());
			i.putExtra("type", "file");
			startActivity(i);

			// Alternatively, you can get the file path from the URI
			// String imagePath = getPathFromUri(selectedImageUri);
		}
	}

	private void initializeLogic() {
		_rippleRoundStroke(linear4, "#008037", "#fefefe", 25, 0, "#fefefe");
		_rippleRoundStroke(linear5, "#008037", "#fefefe", 25, 0, "#fefefe");
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview10.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		advice.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		name.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.BOLD);
		detection.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.BOLD);
		textview4.setVisibility(View.GONE);
		imageview1.setVisibility(View.GONE);

		loadUserDataAndAdvice();
	}

	private void loadUserDataAndAdvice() {
		SharedPreferences userPrefs = getSharedPreferences("user", MODE_PRIVATE);
		String conditionsJson = userPrefs.getString("conditions", "{}");
		System.out.println(conditionsJson);

		String userName = userPrefs.getString("name", "User");
		Map<String, Boolean> userConditions = new Gson().fromJson(conditionsJson, new TypeToken<Map<String, Boolean>>() {}.getType());

		name.setText(userName);

		String jsonData = _loadFromAsset();
		if (jsonData == null) {
			showMessage("Error loading data");
			return;
		}

		Type listType = new TypeToken<List<ConditionAdvice>>() {}.getType();
		List<ConditionAdvice> adviceList = new Gson().fromJson(jsonData, listType);

		SpannableStringBuilder adviceText = new SpannableStringBuilder();

		for (Map.Entry<String, Boolean> entry : userConditions.entrySet()) {
			if (entry.getValue()) {
				for (ConditionAdvice ca : adviceList) {
					if (ca.getId().equalsIgnoreCase(entry.getKey())) {
						// Create a SpannableString for the styled text
						SpannableString styledCondition = new SpannableString(ca.getCondition());

						// Apply color and bold style
						styledCondition.setSpan(new ForegroundColorSpan(Color.parseColor("#171717")), 0, styledCondition.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						styledCondition.setSpan(new StyleSpan(Typeface.BOLD), 0, styledCondition.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						// Append the styled advice to adviceText
						adviceText.append(styledCondition).append("\n\n").append(ca.getAdvice()).append("\n\n");
						break;
					}
				}
			}
		}

		advice.setText(adviceText, TextView.BufferType.SPANNABLE);
	}


	private String _loadFromAsset() {
		String result = null;
		try {
			InputStream is = getAssets().open("usercondition.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			result = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return result;
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
	
	
	public void _Custom_Loading(final boolean _ifShow) {
		if (_ifShow) {
			if (coreprog == null){
				coreprog = new ProgressDialog(this);
				coreprog.setCancelable(false);
				coreprog.setCanceledOnTouchOutside(false);
				
				coreprog.requestWindowFeature(Window.FEATURE_NO_TITLE);  coreprog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
				
			}
			coreprog.setMessage(null);
			coreprog.show();
			View _view = getLayoutInflater().inflate(R.layout.cus, null);
			coreprog.setContentView(_view);
		}
		else {
			if (coreprog != null){
				coreprog.dismiss();
			}
		}
	}
	private ProgressDialog coreprog;
	{
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

	private class ConditionAdvice {
		private String id;
		private String condition;
		private String question;
		private String advice;

		public String getId() {
			return id;
		}

		public String getQuestion() {
			return question;
		}

		public String getAdvice() {
			return advice;
		}
		public String getCondition() {
			return condition;
		}
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
