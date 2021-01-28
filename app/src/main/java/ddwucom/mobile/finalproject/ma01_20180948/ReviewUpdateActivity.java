package ddwucom.mobile.finalproject.ma01_20180948;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReviewUpdateActivity extends AppCompatActivity {

    MovieDBHelper helper;
    final int PLACE_CODE = 100;
    EditText etDate;
    EditText etPlace;
    EditText etReview;
    Spinner spinner;
    String datas[] = {"1","2","3","4","5"}; // 스피너에 표시할 항목을 정의.


    DatePickerDialog.OnDateSetListener method;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);

        etDate = findViewById(R.id.et_date2);
        etPlace = findViewById(R.id.et_place2);
        etReview = findViewById(R.id.et_review2);
        helper = new MovieDBHelper(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String original_title = intent.getStringExtra("original_title");
        String review = intent.getStringExtra("review");
        String date = intent.getStringExtra("date");
        String place = intent.getStringExtra("place");
        String rank = intent.getStringExtra("rank");

        TextView textView_title = (TextView)findViewById(R.id.txt_title2);
        textView_title.setText(title);
        TextView textView_original_title = (TextView)findViewById(R.id.txt_original_title2);
        textView_original_title.setText(original_title);


        int putRank = Integer.parseInt(rank) -1;

        etDate.setText(date);
        etPlace.setText(place);
        etReview.setText(review);

        spinner = (Spinner) findViewById(R.id.spinner_review);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);

        spinner.setAdapter(adapter);
        spinner.setSelection(putRank);

        this.IntilalizelListner();
    }

    public void IntilalizelListner(){
        method = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        };
    }

    public void OnclickHandler(View v){
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog =  new DatePickerDialog(this, method, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dialog.show();
    }

    public void onClick(View v) {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String rank = spinner.getSelectedItem().toString();

        switch(v.getId()) {
            case R.id.btn_update:
                if(etDate.getText().toString().equals("") || etPlace.getText().toString().equals("") || etReview.getText().toString().equals(""))
                    Toast.makeText(this, "모든 정보를 작성해주세요.", Toast.LENGTH_SHORT).show();
                else{
                    SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                    ContentValues row = new ContentValues();
                    row.put(helper.COL_DATE, etDate.getText().toString());
                    row.put(helper.COL_PLACE,etPlace.getText().toString());
                    row.put(helper.COL_REVIEW,etReview.getText().toString());
                    row.put(helper.COL_RANK,rank);
                    String whereClause = helper.COL_TITLE + "=?";
                    String[] whereArgs = new String[] { title };
                    sqLiteDatabase.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                    helper.close();


                Toast.makeText(this, "리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(ReviewUpdateActivity.this, MainActivity.class);
                startActivityForResult(intent,500);
                }
                break;
            case R.id.btn_cancel2:
                Toast.makeText(this, "수정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_place2:
                intent = new Intent(ReviewUpdateActivity.this, MapsSelectActivity.class);
                startActivityForResult(intent, PLACE_CODE);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_CODE) {  // AddActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    etPlace.setText(data.getStringExtra("title"));
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "장소 검색 실패", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
