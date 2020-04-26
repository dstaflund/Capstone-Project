package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;

public class FavoriteButtonClickListener implements View.OnClickListener {

    @NonNull
    private Context mContext;

    @NonNull
    private SearchResultItemFormatter mFormatter;

    public FavoriteButtonClickListener(
        @NonNull Context context,
        @NonNull SearchResultItemFormatter formatter
    ){
        super();
        mContext = context;
        mFormatter = formatter;
    }

    @Override
    public void onClick(@NonNull View v) {
        ImageButton button = (ImageButton) v;
        String id = mFormatter.getGeomemorialId();
        boolean isChecked = ! isFavorite(mContext, id);

        if (isChecked){
            button.setImageResource(R.drawable.ic_favorite_accent_24dp);
            addFavorite(mContext, id);
        }
        else {
            button.setImageResource(R.drawable.ic_favorite_border_accent_24dp);
            removeFavorite(mContext, id);
        }
    }
}
