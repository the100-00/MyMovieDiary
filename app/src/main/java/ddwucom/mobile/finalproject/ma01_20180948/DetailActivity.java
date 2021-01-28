package ddwucom.mobile.finalproject.ma01_20180948;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private MovieDBHelper helper;
    private String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        helper = new MovieDBHelper(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String original_title = intent.getStringExtra("original_title");
        String poster_path = intent.getStringExtra("poster_path");
        String overview = intent.getStringExtra("overview");
        String release_date = intent.getStringExtra("release_date");

        TextView textView_title = (TextView)findViewById(R.id.tv_title);
        textView_title.setText(title);
        TextView textView_original_title = (TextView)findViewById(R.id.tv_original_title);
        textView_original_title.setText(original_title);

        TextView textView_overview = (TextView)findViewById(R.id.txt_overview);
        textView_overview.setText(overview);
        TextView textView_release_date = (TextView)findViewById(R.id.et_date);
        textView_release_date.setText(release_date);
    }

    public void onClick(View v) {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String original_title = intent.getStringExtra("original_title");
        String poster_path = intent.getStringExtra("poster_path");
        String overview = intent.getStringExtra("overview");
        String release_date = intent.getStringExtra("release_date");

        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {helper.COL_TITLE};
        String selection = helper.COL_TITLE+"=?";
        String[] selectArgs = new String[]{title};
        Cursor cursor = db.query(helper.TABLE_NAME, columns, selection, selectArgs, null, null, null, null);
        String search = "";
        while(cursor.moveToNext()){
            search = cursor.getString(0);
        }
        helper.close();
        switch (v.getId()) {
            case R.id.btn_goReview:
                if(search.equals(title)){
                    Toast.makeText(DetailActivity.this, "이미 작성한 리뷰가 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                intent = new Intent(this, ReviewAddActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("original_title", original_title);
                intent.putExtra("overview", overview);
                intent.putExtra("poster_path", poster_path);
                intent.putExtra("release_date", release_date);
                startActivityForResult(intent,100);
                }
                break;
            case R.id.btn_goBack:   // 취소에 따른 처리
                finish();
                break;
        }
    }
}