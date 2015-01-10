package com.cardsq.cardsq.cardlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cardsq.cardsq.R;
import com.cardsq.cardsq.entity.Card;

import java.util.List;

/**
 * Customized array adapter, used to show a list of cards in a deck.
 *
 * Created by lzhu on 1/10/15.
 */
public class CardListArrayAdapter extends ArrayAdapter<Card> {


    private Activity context;
    private List<Card> cardList ;

    public CardListArrayAdapter(Activity context, int resourceId, List<Card> cardList){
        super(context, resourceId, cardList);
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.card_list_item, null, true);
        Card card = cardList.get(position);
        TextView cardTitle = (TextView) rowView.findViewById(R.id.cardName);
        cardTitle.setText(card.getTitle());
        TextView cardExplanation = (TextView) rowView.findViewById(R.id.cardExplanation);
        cardExplanation.setText(card.getExplanation());
        return rowView;
    }


}
