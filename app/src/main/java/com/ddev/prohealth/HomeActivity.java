package com.ddev.prohealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;


public class HomeActivity extends AppCompatActivity {
	
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
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
	
	private Intent i = new Intent();
	private StorageReference fstore = _firebase_storage.getReference("imgs");
	private OnCompleteListener<Uri> _fstore_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _fstore_download_success_listener;
	private OnSuccessListener _fstore_delete_success_listener;
	private OnProgressListener _fstore_upload_progress_listener;
	private OnProgressListener _fstore_download_progress_listener;
	private OnFailureListener _fstore_failure_listener;
	
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
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
				_PickFile("image/*");
			}
		});
		
		_fstore_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_fstore_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_fstore_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				i.setClass(getApplicationContext(), ViewActivity.class);
				i.putExtra("img", _downloadUrl);
				startActivity(i);
			}
		};
		
		_fstore_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_fstore_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_fstore_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				
			}
		};
	}
	
	private void initializeLogic() {
		_rippleRoundStroke(linear4, "#008037", "#fefefe", 25, 0, "#fefefe");
		_rippleRoundStroke(linear5, "#008037", "#fefefe", 25, 0, "#fefefe");
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montbold.ttf"), Typeface.NORMAL);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montreg.ttf"), Typeface.BOLD);
		textview4.setVisibility(View.GONE);
		imageview1.setVisibility(View.GONE);
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
	
	
	public void _PickFileListener() {
	}
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// If no sellection was made, we return back to the activity
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			SketchwareUtil.showMessage(getApplicationContext(), "File not selected");
			return;
		} else {
			if (requestCode == 100) {
				//File picker returning a Uri
				Uri uri = data.getData();
				//App data directory
				String dataPath = "/data/data/" + getApplicationContext().getPackageName();

				Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
				/*
				 * Get the column indexes of the data in the Cursor,
				 * move to the first row in the Cursor, get the data,
				 * and display it.
				 */
				int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
				int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
				returnCursor.moveToFirst();
				final String fileName = returnCursor.getString(nameIndex);
				String fileSize = Long.toString(returnCursor.getLong(sizeIndex));
				try {
					InputStream in = null;
					OutputStream out = null;
					try {
						// open the user-picked file for reading:
						in = getContentResolver().openInputStream(uri);
						// open the output-file,(to your app data Directory)
						out = new FileOutputStream(new File(dataPath + "/" + fileName));
						// copy the file
						byte[] buffer = new byte[1024];
						int len;
						while ((len = in.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						// Contents are copied
					} finally {
						if (in != null) {
							in.close();
						}
						if (out != null) {
							out.close();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					// handle exception correctly.
				}
				filePath = dataPath.concat("/".concat(fileName));
				fstore.child(fileName).putFile(Uri.fromFile(new File(filePath))).addOnFailureListener(_fstore_failure_listener).addOnProgressListener(_fstore_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return fstore.child(fileName).getDownloadUrl();
					}
				}).addOnCompleteListener(_fstore_upload_success_listener);
				_Custom_Loading(true);
			}
		}
	}
	
	
	public void _PickFile(final String _extension) {
		//New Intent 
		Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		i.setType(_extension);
		i = Intent.createChooser(i, "Choose an File");
		startActivityForResult(i, 100);
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
