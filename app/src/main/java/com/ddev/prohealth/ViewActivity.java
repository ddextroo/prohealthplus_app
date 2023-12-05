package com.ddev.prohealth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ViewActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> Header2 = new HashMap<>();
	private HashMap<String, Object> body2 = new HashMap<>();
	private HashMap<String, Object> m = new HashMap<>();
	private HashMap<String, Object> m2 = new HashMap<>();
	private HashMap<String, Object> m3 = new HashMap<>();
	private String jsonResponse = "";
	private String pred = "";
	
	private LinearLayout linear2;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private LinearLayout linear8;
	private LinearLayout linear10;
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private LottieAnimationView lottie1;
	private ImageView imageview1;
	private TextView textview4;
	private TextView textview6;
	private TextView textview8;
	
	private RequestNetwork requestnet2;
	private RequestNetwork.RequestListener _requestnet2_request_listener;
	private Intent i = new Intent();
	private TimerTask t;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.view);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		linear4 = findViewById(R.id.linear4);
		linear5 = findViewById(R.id.linear5);
		linear6 = findViewById(R.id.linear6);
		linear8 = findViewById(R.id.linear8);
		linear10 = findViewById(R.id.linear10);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		textview3 = findViewById(R.id.textview3);
		lottie1 = findViewById(R.id.lottie1);
		imageview1 = findViewById(R.id.imageview1);
		textview4 = findViewById(R.id.textview4);
		textview6 = findViewById(R.id.textview6);
		textview8 = findViewById(R.id.textview8);
		requestnet2 = new RequestNetwork(this);
		
		linear6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), DetailActivity.class);
				i.putExtra("name", textview3.getText().toString().toLowerCase());
				i.putExtra("detail", "nutrient");
				startActivity(i);
			}
		});
		
		linear8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), DetailActivity.class);
				i.putExtra("name", textview3.getText().toString().toLowerCase());
				i.putExtra("detail", "description");
				startActivity(i);
			}
		});
		
		linear10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), DetailActivity.class);
				i.putExtra("name", textview3.getText().toString().toLowerCase());
				i.putExtra("detail", "effect");
				startActivity(i);
			}
		});
		
		_requestnet2_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				m = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
				lottie1.setVisibility(View.GONE);
				imageview1.setVisibility(View.VISIBLE);
				byte[] decoded= android.util.Base64.decode(m.get("encoded_image").toString(), android.util.Base64.DEFAULT); 		android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(decoded, 0, decoded.length); 		android.graphics.drawable.BitmapDrawable bd = new android.graphics.drawable.BitmapDrawable(bmp); 		imageview1.setImageDrawable(bd);
				jsonResponse = _response;
				try{
						
						JSONObject jsonResponse2 = new JSONObject(jsonResponse);
						JSONObject response = jsonResponse2.getJSONObject("response");
						JSONArray predictions = response.getJSONArray("predictions");
						if (predictions.length() == 0) {
								// System.out.println("No predictions found.");
						        pred = "No predictions found";
						} else {
								JSONObject prediction = predictions.getJSONObject(0);
								pred = prediction.getString("class");
						}
				} catch (Exception e) {
						e.printStackTrace();
				}
				
				_firsletter(textview3, pred);
				if (pred.equals("No predictions found")) {
					
				}
				else {
					linear6.setEnabled(true);
					linear8.setEnabled(true);
					linear10.setEnabled(true);
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.NORMAL);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		_rippleRoundStroke(linear5, "#fefefe", "#e0e0e0", 25, 2.5d, "#e0e0e0");
		_rippleRoundStroke(linear6, "#008073", "#fefefe", 25, 0, "#fefefe");
		_rippleRoundStroke(linear8, "#008073", "#fefefe", 25, 0, "#fefefe");
		_rippleRoundStroke(linear10, "#008073", "#fefefe", 25, 0, "#fefefe");
		imageview1.setVisibility(View.GONE);
		linear6.setEnabled(false);
		linear8.setEnabled(false);
		linear10.setEnabled(false);
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
	
	@Override
	public void onStart() {
		super.onStart();
		t = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Header2 = new HashMap<>();
						Header2.put("Content-Type", "application/json");
						body2.put("image_url", getIntent().getStringExtra("img"));
						requestnet2.setHeaders(Header2);
						requestnet2.setParams(body2, RequestNetworkController.REQUEST_BODY);
						requestnet2.startRequestNetwork(RequestNetworkController.POST, "https://prohealth.onrender.com/predict", "", _requestnet2_request_listener);
					}
				});
			}
		};
		_timer.schedule(t, (int)(500));
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
