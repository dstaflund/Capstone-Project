package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;

public class SearchResultItemFragmentHolder {

    @NonNull
    TextView recordCount;

    @NonNull
    public TextView distance;

    @NonNull
    TextView coordinate;

    @NonNull
    public TextView geomemorial;

    @NonNull
    public TextView hometown;

    @NonNull
    public TextView name;

    @NonNull
    TextView ntsSheet;

    @NonNull
    public TextView obit;

    @NonNull
    public TextView rank;

    @NonNull
    ImageButton favoriteButton;

    @NonNull
    ImageButton shareButton;

    @NonNull
    ImageButton placeButton;

    SearchResultItemFragmentHolder(@NonNull View v) {
        recordCount = v.findViewById(R.id.record_count);
        distance = v.findViewById(R.id.distance);
        coordinate = v.findViewById(R.id.list_item_common_coordinate);
        geomemorial = v.findViewById(R.id.list_item_common_geomemorial);
        hometown = v.findViewById(R.id.list_item_common_hometown);
        name = v.findViewById(R.id.list_item_common_name);
        ntsSheet = v.findViewById(R.id.list_item_common_nts_sheet);
        obit = v.findViewById(R.id.list_item_common_obit);
        rank = v.findViewById(R.id.list_item_common_rank);
        favoriteButton = v.findViewById(R.id.favorite_button);
        shareButton = v.findViewById(R.id.share_button);
        placeButton = v.findViewById(R.id.place_button);
    }
}
