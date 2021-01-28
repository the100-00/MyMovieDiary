package ddwucom.mobile.finalproject.ma01_20180948;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static android.content.ContentValues.TAG;

public class ReviewAdapter extends CursorAdapter {

    private String TAG = "ReviewAdapter";
    private Context context;
    private int layout;
    private LayoutInflater layoutInflater;

    public ReviewAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final String title = (cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_TITLE)));
        final String originaltitle = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_ORIGINAL_TITLE));
        final String date = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_DATE));
        final String place = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_PLACE));
        final String review = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_REVIEW));
        final String rank = cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_RANK));

        if(holder.Title == null){
            holder.Title = view.findViewById(R.id.r_title);
            holder.OriginalTitle = view.findViewById(R.id.r_original_title);
            holder.Date = view.findViewById(R.id.r_date);
            holder.Place = view.findViewById(R.id.r_place);
            holder.Review = view.findViewById(R.id.r_review);
            holder.Poster = view.findViewById(R.id.r_poster);
            holder.Rank = view.findViewById(R.id.r_rank);
        }

        holder.Title.setText(title);
        holder.OriginalTitle.setText(originaltitle);
        holder.Date.setText(date);
        holder.Place.setText(place);
        holder.Review.setText(review);
        holder.Rank.setText(rank);

        String url = "https://image.tmdb.org/t/p/w500" + cursor.getString(cursor.getColumnIndex(MovieDBHelper.COL_POSTER));
        Glide.with(context).load(url).into(holder.Poster);

        Log.d(TAG,"이미지 로딩 중");

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewUpdateActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("original_title", originaltitle);
                intent.putExtra("date", date);
                intent.putExtra("place", place);
                intent.putExtra("review", review);
                intent.putExtra("rank",rank);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(layout,parent,false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    static class ViewHolder{
        TextView Title;
        TextView OriginalTitle;
        TextView Date;
        TextView Place;
        TextView Review;
        TextView Rank;
        ImageView Poster;
    }

}
