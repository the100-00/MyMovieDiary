package ddwucom.mobile.finalproject.ma01_20180948;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    final int RECENT_CODE = 100;
    final int SEARCH_CODE = 300;
    final int MAP_CODE = 400;
    final int UPDATE_CODE = 500;

    ListView listView = null;
    MovieDBHelper helper;
    Cursor cursor;
    ReviewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listView.setLongClickable(true);
        helper = new MovieDBHelper(this);

        adapter = new ReviewAdapter(this, R.layout.review_adpater_view, null);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, final long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title)
                        .setMessage(R.string.dialog_message)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                                String whereClause = helper.COL_ID + "=?";
                                String[] whereArgs = new String[]{String.valueOf(id)};
                                sqLiteDatabase.delete(helper.TABLE_NAME, whereClause, whereArgs);
                                if (true) {
                                    cursor = sqLiteDatabase.rawQuery("select * from " + MovieDBHelper.TABLE_NAME, null);
                                    adapter.changeCursor(cursor);
                                    Toast.makeText(MainActivity.this, "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "리뷰 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        //contactList.clear();
//        DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + MovieDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);

        helper.close();
    }

        public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_mMap:
                intent = new Intent(this,MapsActivity.class);
                startActivityForResult(intent, MAP_CODE);
                break;
            case R.id.btn_recent:
                intent = new Intent(this, RecentActivity.class);
                startActivityForResult(intent, RECENT_CODE);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
            }
        }
        else if (requestCode == UPDATE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == RECENT_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
            }
        }
        else if (requestCode == SEARCH_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
            }
        }
    }

    public void shareKakao()  {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("나의 무비 다이어리",
                        "https://www.iamherelearning.com/wp-content/uploads/2020/02/Movie-Icon-1-460x406.png",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("당신만의 영화 리뷰를 작성해보세요.")
                        .build())
                .build();


        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {}

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onMenuItemClick(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_share:
                Log.d(TAG,"shareKaKao");
                shareKakao();
                break;
            case R.id.btn_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("앱 종료");
                builder.setMessage("앱을 종료하시겠습니까?");
                builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        if (Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
                break;
        }
        return true;
    }
}

