package com.ddev.prohealth.detection;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddev.prohealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class FruitDetailAdapter extends RecyclerView.Adapter<FruitDetailAdapter.ViewHolder> {

    private final List<String> details;
    private final LayoutInflater inflater;

    public FruitDetailAdapter(Context context, List<String> details) {
        this.details = details;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_fruit_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String detail = details.get(position);
        holder.detailTitle.setText(Html.fromHtml(detail));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailTitle;

        ViewHolder(View itemView) {
            super(itemView);
            detailTitle = itemView.findViewById(R.id.fruit_detail_title);
        }
    }
}
