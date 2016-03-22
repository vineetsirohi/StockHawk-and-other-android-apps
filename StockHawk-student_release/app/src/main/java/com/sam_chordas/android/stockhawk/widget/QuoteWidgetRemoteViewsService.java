package com.sam_chordas.android.stockhawk.widget;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by vineet on 20-Mar-16.
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    public static class ListProvider implements RemoteViewsService.RemoteViewsFactory {

        public static final int COL_STOCK_SYMBOL = 3;

        public static final int COL_BID_PRICE = 4;

        public static final int COL_CHANGE = 2;

        private Cursor mCursor;

        private Context context = null;

        private int appWidgetId;

        public ListProvider(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }

            mCursor = context.getContentResolver()
                    .query(QuoteProvider.Quotes.CONTENT_URI,
                            new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                                    QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                            QuoteColumns.ISCURRENT + " = ?",
                            new String[]{"1"},
                            null);

        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.list_item_quote);

            if (mCursor.moveToPosition(position)) {
                remoteView.setTextViewText(R.id.stock_symbol, mCursor.getString(mCursor.getColumnIndex("symbol")));
                remoteView.setTextViewText(R.id.bid_price, mCursor.getString(mCursor.getColumnIndex("bid_price")));
                remoteView.setTextViewText(R.id.change, mCursor.getString(mCursor.getColumnIndex("percent_change")));

            }

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }


    }
}
