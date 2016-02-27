package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vineet on 28-Feb-16.
 */
public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    public static class ListProvider implements RemoteViewsService.RemoteViewsFactory {

        public static final int COL_HOME = 3;

        public static final int COL_AWAY = 4;

        public static final int COL_HOME_GOALS = 6;

        public static final int COL_AWAY_GOALS = 7;

        public static final int COL_DATE = 1;

        public static final int COL_LEAGUE = 5;

        public static final int COL_MATCHDAY = 9;

        public static final int COL_ID = 8;

        public static final int COL_MATCHTIME = 2;

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

//            load today's data
            Date fragmentdate = new Date(System.currentTimeMillis());
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

            mCursor = context.getContentResolver()
                    .query(DatabaseContract.scores_table.buildScoreWithDate(), null, null,
                            new String[]{mformat.format(fragmentdate)},
                            null);

            Log.d("Football scores",
                    "barqsoft.footballscores.WidgetService.ListProvider.onDataSetChanged" + ": "
                            + mCursor.getCount());
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
                    R.layout.scores_list_item);

            if (mCursor.moveToPosition(position)) {
                Log.d("Football scores",
                        "barqsoft.footballscores.WidgetService.ListProvider.getViewAt" + ": "
                                + position + " " + mCursor.getString(COL_HOME));

                remoteView.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
                remoteView.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
                remoteView.setTextViewText(R.id.data_textview, mCursor.getString(COL_MATCHTIME));
                remoteView.setTextViewText(R.id.score_textview,
                        Utilies.getScores(mCursor.getInt(COL_HOME_GOALS),
                                mCursor.getInt(COL_AWAY_GOALS))
                );
                remoteView.setImageViewResource(R.id.home_crest,
                        Utilies.getTeamCrestByTeamName(mCursor.getString(COL_HOME)));
                remoteView.setImageViewResource(R.id.away_crest,
                        Utilies.getTeamCrestByTeamName(mCursor.getString(COL_AWAY)));
            }

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }


    }
}