package com.github.dstaflund.geomemorial.ui.fragment.favorites.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;

import com.github.dstaflund.geomemorial.R;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragment.GEOMEMORIAL_TAG_KEY;

public class FavoriteButtonClickListener implements View.OnClickListener {

    @NonNull
    private Context mContext;

    public FavoriteButtonClickListener(@NonNull Context context){
        super();
        mContext = context;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.list_item_favorite_favorite_button:
                ImageButton button = (ImageButton) v;
                Long geomemorialId = (Long) button.getTag(GEOMEMORIAL_TAG_KEY);
                boolean isChecked = ! isFavorite(mContext, geomemorialId.toString());

                if (isChecked){
                    button.setImageResource(R.drawable.ic_favorite_white_24dp);
                    addFavorite(mContext, geomemorialId.toString());
                }
                else {
                    button.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    removeFavorite(mContext, geomemorialId.toString());
                }
                break;
            default:
        }
    }
}
