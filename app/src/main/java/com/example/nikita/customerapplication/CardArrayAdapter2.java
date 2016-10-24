package com.example.nikita.customerapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 28-09-2016.
 */
public class CardArrayAdapter2  extends ArrayAdapter<Card2> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card2> cardList = new ArrayList<>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;

    }

    public CardArrayAdapter2(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Card2 object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card2 getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.details_list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.date);
            viewHolder.line2 = (TextView) row.findViewById(R.id.distance);
            viewHolder.line3 = (TextView) row.findViewById(R.id.time);

            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Card2 card2 = getItem(position);
        viewHolder.line1.setText(card2.getLine1());
        viewHolder.line2.setText(card2.getLine2());
        viewHolder.line3.setText(card2.getLine3());

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

