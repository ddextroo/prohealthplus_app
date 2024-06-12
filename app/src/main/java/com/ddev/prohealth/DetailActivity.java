package com.ddev.prohealth;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddev.prohealth.detection.FruitDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

//	private LinearLayout linear1;
//	private LinearLayout linear2;
	private TextView textview2;
	private TextView fruitName;
	private RecyclerView recyclerView;
	private FruitDetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

//		linear1 = findViewById(R.id.linear1);
//		linear2 = findViewById(R.id.linear2);
		textview2 = findViewById(R.id.detectedTitle);
		recyclerView = findViewById(R.id.recyclerView);
		fruitName = findViewById(R.id.fruitName);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		initializeLogic();
	}

	private void initializeLogic() {
		String fruit = getIntent().getStringExtra("name");
		String detailType = getIntent().getStringExtra("detail");

		textview2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/montbold.ttf"), Typeface.NORMAL);
		fruitName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/montbold.ttf"), Typeface.NORMAL);

		List<String> details = new ArrayList<>();

		try {
			JSONArray fruitsArray = new JSONArray(_loadFromAsset());
			for (int i = 0; i < fruitsArray.length(); i++) {
				JSONObject fruitObject = fruitsArray.getJSONObject(i);
				if (fruit != null && fruit.trim().equalsIgnoreCase(fruitObject.getString("name").trim())) {
					String name = fruitObject.getString("name");
					String description = fruitObject.getString("description");
					String history = fruitObject.getString("history");
					String effects = fruitObject.getString("effects");

					JSONArray nutrientsArray = fruitObject.getJSONArray("nutrients");
					StringBuilder detailBuilder = new StringBuilder();
					Object effectsObject = fruitObject.get("effects");

//					detailBuilder.append("<b><h1>").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("</h1></b>");
					fruitName.setText(capitalizeFirstLetter(name));
					if ("nutrient".equals(detailType)) {
						textview2.setText(Html.fromHtml("<b><h2>Nutrients</h2></b>"));
						for (int j = 0; j < nutrientsArray.length(); j++) {
							JSONObject nutrientObject = nutrientsArray.getJSONObject(j);
							String nutrientTitle = nutrientObject.getString("title");
							String nutrientContent = nutrientObject.getString("content");
							detailBuilder.append("<br/><b><p>").append(nutrientTitle).append("</p></b>").append(nutrientContent);
						}
					} else if ("description".equals(detailType)) {
						textview2.setText(Html.fromHtml("<b><h2>Description and History</b><br/>"));
						detailBuilder.append("<br/><b><p>Description</p></b>").append(description)
								.append("<br/><b><p>History</p></b>").append(history);
					} else if ("effect".equals(detailType)) {
						textview2.setText(Html.fromHtml("<b><h2>Effects</b><br/>"));
						if (effectsObject instanceof String) {
							detailBuilder.append("<br/><br/>").append(effectsObject);
						} else if (effectsObject instanceof JSONArray) {
							JSONArray effectsArray = (JSONArray) effectsObject;
							for (int k = 0; k < effectsArray.length(); k++) {
								JSONObject effectObject = effectsArray.getJSONObject(k);
								String effectTitle = effectObject.getString("title");
								String effectContent = effectObject.getString("content");
								detailBuilder.append("<br/><br/><b>").append(effectTitle).append("</b>: ").append(effectContent);
							}
						}
					}

					details.add(detailBuilder.toString());
					break; // Stop the loop after the first match
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter = new FruitDetailAdapter(this, details);
		recyclerView.setAdapter(adapter);
	}

	private String capitalizeFirstLetter(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}


	private String _loadFromAsset() {
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
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
