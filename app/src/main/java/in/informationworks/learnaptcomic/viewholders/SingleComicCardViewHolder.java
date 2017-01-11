package in.informationworks.learnaptcomic.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.informationworks.learnaptcomic.Models.CommonRecyclerItem;
import in.informationworks.learnaptcomic.R;

/**
 * Created by Riya on 10-Jan-17.
 */

public class SingleComicCardViewHolder extends RecyclerView.ViewHolder {
    ImageView comic_card_image;
    TextView comic_card_text;

    public SingleComicCardViewHolder(View itemView) {
        super(itemView);


        comic_card_image=(ImageView)itemView.findViewById(R.id.comic_card_image);
        comic_card_text=(TextView)itemView.findViewById(R.id.comic_card_text);
    }
    public void bindCRI(Context context, CommonRecyclerItem recyclerItem)
    {

    }
}

