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

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		initializeLogic();
	}

	private void initializeLogic() {
		String[] fruits = getIntent().getStringArrayExtra("name");
		String detailType = getIntent().getStringExtra("detail");

		textview2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/montbold.ttf"), Typeface.NORMAL);

		List<String> details = new ArrayList<>();

		try {
			JSONArray fruitsArray = new JSONArray(_loadFromAsset());
			for (String fruit : fruits) {
				System.out.println(fruit + "dexteer");
				for (int i = 0; i < fruitsArray.length(); i++) {
					JSONObject fruitObject = fruitsArray.getJSONObject(i);
					if (fruit != null && fruit.substring(2).trim().equals(fruitObject.getString("name").trim())) {
						String name = fruitObject.getString("name");
						String description = fruitObject.getString("description");
						String history = fruitObject.getString("history");
						String effects = fruitObject.getString("effects");

						JSONArray nutrientsArray = fruitObject.getJSONArray("nutrients");
						StringBuilder detailBuilder = new StringBuilder();
						Object effectsObject = fruitObject.get("effects");

						detailBuilder.append("<b><h1>").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("</h1></b>");

						if ("nutrient".equals(detailType)) {
							textview2.setText(Html.fromHtml("<b><h2>Nutrients</h2></b>"));
							for (int j = 0; j < nutrientsArray.length(); j++) {
								JSONObject nutrientObject = nutrientsArray.getJSONObject(j);
								String nutrientTitle = nutrientObject.getString("title");
								String nutrientContent = nutrientObject.getString("content");
								detailBuilder.append("<br/><b><p>").append(nutrientTitle).append("</p></b>").append(nutrientContent);
							}
						} else if ("description".equals(detailType)) {
							textview2.setText(Html.fromHtml("<br/><b><h2>Description and History</h2></b>"));
							detailBuilder.append("<br/><b><h2>Description</h2></b>").append(description)
									.append("<br/><br/><b><h2>History</h2></b>").append(history);
						} else if ("effect".equals(detailType)) {
							textview2.setText(Html.fromHtml("<b><h2>Effects</h2></b><br/>"));
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
						break;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter = new FruitDetailAdapter(this, details);
		recyclerView.setAdapter(adapter);
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
