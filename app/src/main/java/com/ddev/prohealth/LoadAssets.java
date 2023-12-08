package com.ddev.prohealth;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class LoadAssets {

    private Context context;

    public LoadAssets(Context context) {
        this.context = context;
    }

    public String loadFromAsset(String assetFileName) {
        String result = null;
        try {
            InputStream is = context.getAssets().open(assetFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

