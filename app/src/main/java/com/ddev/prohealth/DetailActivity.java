package com.ddev.prohealth;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

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
	private TextView textview1;
	private TextView textview2;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.detail);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
	}
	
	private void initializeLogic() {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
		_firsletter(textview2, getIntent().getStringExtra("name"));
		try {
			    JSONArray fruitsArray = new JSONArray(_loadFromAsset());
			    for (int i = 0; i < fruitsArray.length(); i++) {
				        JSONObject fruitObject = fruitsArray.getJSONObject(i);
				
				        if (getIntent().getStringExtra("name").equals(fruitObject.getString("name"))) {
					            String name = fruitObject.getString("name");
					            String description = fruitObject.getString("description");
					            String history = fruitObject.getString("history");
					            String effects = fruitObject.getString("effects");
					
					            JSONArray nutrientsArray = fruitObject.getJSONArray("nutrients");
					            StringBuilder combine = new StringBuilder();
					
					            for (int j = 0; j < nutrientsArray.length(); j++) {
						                JSONObject nutrientObject = nutrientsArray.getJSONObject(j);
						
						                if (getIntent().getStringExtra("name").equals(name)) {
							                    String nutrientTitle = nutrientObject.getString("title");
							                    String nutrientContent = nutrientObject.getString("content");
							
												combine.append("<br/><b><h2>").append(nutrientTitle).append("</h2></b>").append(nutrientContent);
							                }
						            }
					
					            if (getIntent().getStringExtra("detail").equals("nutrient")) {
										textview1.setText(Html.fromHtml("<b><h2>Nutrients</h2></b>" + combine.toString()));
						            } else if (getIntent().getStringExtra("detail").equals("description")) {
										textview1.setText(Html.fromHtml("<br/><b><h2>Description</h2></b>" + description + "<br/><br/><b><h2>History</h2></b>" + history));
						            } else if (getIntent().getStringExtra("detail").equals("effect")) {
										textview1.setText(Html.fromHtml("<b><h2>Effects</h2></b><br/>" + effects));
						            }
					        }
				    }
		} catch (JSONException e) {
			    e.printStackTrace();
		}
	}
	public void _firsletter(final TextView _textview, final String _text) {
				String text_textview = _text;
					String firstLetter_textview = text_textview.substring(0, 1);
					    String remainingLetters_textview = text_textview.substring(1, text_textview.length());
					
					    // change the first letter to uppercase
					    firstLetter_textview = firstLetter_textview.toUpperCase();
					
					    // join the two substrings
					    text_textview = firstLetter_textview + remainingLetters_textview;
					
					_textview.setText(text_textview);

		}
		
		
		public void _capitalizeFirstEveryWord(final TextView _textview, final String _text) {
					// create a string
					    String text_textview = _text;
					
					    // stores each characters to a char array
					    char[] charArray_textview = text_textview.toCharArray();
					    boolean foundSpace_textview = true;
					
					    for(int i = 0; i < charArray_textview.length; i++) {
									
									      // if the array element is a letter
									      if(Character.isLetter(charArray_textview[i])) {
													
													        // check space is present before the letter
													        if(foundSpace_textview) {
																	
																	          // change the letter into uppercase
																	          charArray_textview[i] = Character.toUpperCase(charArray_textview[i]);
																	          foundSpace_textview = false;
																	        }
													      }
									
									      else {
													        // if the new character is not character
													        foundSpace_textview = true;
													      }
									    }
					    // convert the char array to the string
					    text_textview = String.valueOf(charArray_textview);
					_textview.setText(text_textview);
		}
	
	{
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
