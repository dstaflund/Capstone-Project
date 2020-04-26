package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;

public class SearchResultItemFragmentViewHolder {

    @NonNull
    public TextView recordCount;

    @NonNull
    public TextView distance;

    @NonNull
    public TextView coordinate;

    @NonNull
    public TextView geomemorial;

    @NonNull
    public TextView hometown;

    @NonNull
    public TextView name;

    @NonNull
    public TextView ntsSheet;

    @NonNull
    public TextView obit;

    @NonNull
    public TextView rank;

    @NonNull
    public ImageButton favoriteButton;

    @NonNull
    public ImageButton shareButton;

    @NonNull
    public ImageButton placeButton;

    public SearchResultItemFragmentViewHolder(@NonNull View v) {
        recordCount = (TextView) v.findViewById(R.id.record_count);
        distance = (TextView) v.findViewById(R.id.distance);
        coordinate = (TextView) v.findViewById(R.id.list_item_common_coordinate);
        geomemorial = (TextView) v.findViewById(R.id.list_item_common_geomemorial);
        hometown = (TextView) v.findViewById(R.id.list_item_common_hometown);
        name = (TextView) v.findViewById(R.id.list_item_common_name);
        ntsSheet = (TextView) v.findViewById(R.id.list_item_common_nts_sheet);
        obit = (TextView) v.findViewById(R.id.list_item_common_obit);
        rank = (TextView) v.findViewById(R.id.list_item_common_rank);
        favoriteButton = (ImageButton) v.findViewById(R.id.favorite_button);
        shareButton = (ImageButton) v.findViewById(R.id.share_button);
        placeButton = (ImageButton) v.findViewById(R.id.place_button);
    }
}
