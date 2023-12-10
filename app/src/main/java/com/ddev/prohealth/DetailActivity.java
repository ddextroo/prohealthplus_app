package com.ddev.prohealth;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class DetailActivity extends AppCompatActivity {
	
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private TextView textview2;
	private TextView fruit1;
	private TextView fruit2;
	private TextView fruit3;
	private TextView fruit4;
	private TextView fruit5;
	private TextView fruit6;
	private TextView fruit7;
	private TextView fruit8;
	private TextView fruit9;
	private TextView fruit10;


	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.detail);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		textview2 = findViewById(R.id.detectedTitle);
		fruit1 = findViewById(R.id.fruit1);
		fruit2 = findViewById(R.id.fruit2);
		fruit3 = findViewById(R.id.fruit3);
		fruit4 = findViewById(R.id.fruit4);
		fruit5 = findViewById(R.id.fruit5);
		fruit6 = findViewById(R.id.fruit6);
		fruit7 = findViewById(R.id.fruit7);
		fruit8 = findViewById(R.id.fruit8);
		fruit9 = findViewById(R.id.fruit9);
		fruit10 = findViewById(R.id.fruit10);
	}
	
	private void initializeLogic() {
		StringBuilder t = new StringBuilder();
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
		String[] fruits = getIntent().getStringArrayExtra("name");
		String fruitLengthString = String.valueOf(fruits.length);
		int fruitsLength = Integer.parseInt(fruitLengthString);
		TextView[] textview1 = new TextView[fruits.length];
		int[] textViewIds = new int[]{R.id.fruit1, R.id.fruit2,R.id.fruit3,R.id.fruit4,R.id.fruit5,R.id.fruit6,R.id.fruit7,R.id.fruit8,R.id.fruit9,R.id.fruit10};
		for (int k = 0; k < fruitsLength; k++) {
			String currentFruit = fruits[k];
			t.append(fruits.length);
			textview1[k] = findViewById(textViewIds[k]);
			if (currentFruit != null) {
				textview1[k].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/montreg.ttf"), Typeface.NORMAL);

				try {
					JSONArray fruitsArray = new JSONArray(_loadFromAsset());
					for (int i = 0; i < fruitsArray.length(); i++) {
						JSONObject fruitObject = fruitsArray.getJSONObject(i);
						if (currentFruit.trim().equals(fruitObject.getString("name").trim())) {
							String name = fruitObject.getString("name");
							String description = fruitObject.getString("description");
							String history = fruitObject.getString("history");
							String effects = fruitObject.getString("effects");


							JSONArray nutrientsArray = fruitObject.getJSONArray("nutrients");
							StringBuilder combine = new StringBuilder();
							StringBuilder combine2 = new StringBuilder();
							combine.append("<b><h1>").append(name.substring(0, 1).toUpperCase() + name.substring(1)).append("</b></h1>");
							for (int j = 0; j < nutrientsArray.length(); j++) {
								JSONObject nutrientObject = nutrientsArray.getJSONObject(j);

								if (currentFruit.trim().equals(name.trim())) {
									String nutrientTitle = nutrientObject.getString("title");
									String nutrientContent = nutrientObject.getString("content");

									combine.append("<br/><b><p>").append(nutrientTitle).append("</p></b>").append(nutrientContent);
								}
							}
							textview1[k].setGravity(Gravity.CENTER);
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
								textview1[k].setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
							}
							String title = combine2.append("<b><h1>").append(name.substring(0, 1).toUpperCase() + name.substring(1)).append("</b></h1>").toString();
							if (getIntent().getStringExtra("detail").equals("nutrient")) {
								textview2.setText(Html.fromHtml("<b><h2>Nutrients</h2></b>"));
								textview1[k].setText(Html.fromHtml(combine.toString()));
							} else if (getIntent().getStringExtra("detail").equals("description")) {
								textview2.setText(Html.fromHtml("<br/><b><h2>Description and History</h2></b>"));
								textview1[k].setText(Html.fromHtml("<b><h1>" + title + "</h1></b><br/>" + "<br/><b><h2>Description</h2></b>" + description + "<br/><br/><b><h2>History</h2></b>" + history));
							} else if (getIntent().getStringExtra("detail").equals("effect")) {
								textview2.setText(Html.fromHtml("<b><h2>Effects</h2></b><br/>"));
								textview1[k].setText(Html.fromHtml("<b><h1>" + title + "</h1></b><br/><br/>" + effects));
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				textview1[k].setVisibility(View.GONE);
			}
		}

	}
	
	public String _loadFromAsset() {
		String result = null;
		try {
					InputStream is = getAssets().open("fruitdetails.json");
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
	
	
	public void _extra() {
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
