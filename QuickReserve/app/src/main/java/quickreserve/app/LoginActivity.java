package quickreserve.app;

/*
*   Awesome login activity with an awesome animation
*
*
                                                /0000\
                                               /000000)
                                              (000000/
                                              |00000/
                                 _______      |00000|
                               /000  o000000/ /000000\
                              000000  0000 //00000000\
                              \0000000  000||000000000|
                        /000000 \000000  00||000000000|
                       0000000o  0oooooo 00/0000000000|
                   /0000 \0000000o 0oooo) //0000000000|
       --ooo_____ |00000  \0000000) //00000--000000000|
      (0000000000\ \\\00000  0ooo/ //00000000000000000/
      \00000000000 \\\00ooo) /|||||/00000000000000000/
            --ooooo_ \\---00000000000000000000000000/
                       \\00000000000000000000000000/
                         \\0000000000000000000000/
                             -ooooooooooooooooo-
*/


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class LoginActivity extends Activity implements Animation.AnimationListener {


    private RelativeLayout mLoginView;
    private FrameLayout mTempFrame;
    private EditText mUserID_field;
    private EditText mPassword_field;
    private Button mLoginButton;
    private MySQLiteHelper mySQLiteHelper;
    private String att_uid;

    private Animation slideUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);

        setContentView(R.layout.activity_login);
        final Context context = this;

        mLoginView = (RelativeLayout)findViewById(R.id.loginView);
        mTempFrame = (FrameLayout)findViewById(R.id.tempFrame);
        mUserID_field = (EditText)findViewById(R.id.userID);
        mPassword_field = (EditText)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mySQLiteHelper = new MySQLiteHelper(this);

        slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up_login_info);
        slideUp.setAnimationListener(this);

        mLoginView.setVisibility(View.VISIBLE);
        mLoginView.startAnimation(slideUp);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                att_uid = mUserID_field.getText().toString();

                if(mySQLiteHelper.getUser(att_uid.toLowerCase()) == null)
                {
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    if(mySQLiteHelper.getUserReservations(att_uid.toLowerCase()) == null)
                    {
                        Intent intent = new Intent(context, DateTimeActivity.class);
                        intent.putExtra("att_uid", att_uid.toLowerCase());
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, MyReservationActivity.class);
                        intent.putExtra("att_uid", att_uid.toLowerCase());
                        startActivity(intent);
                    }
                }
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
        // run your one time code
            mySQLiteHelper.firstRun();
            Log.v("first time", "running first time");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimationStart(Animation animation) {

        //mLoginView.setVisibility(View.INVISIBLE);
        mTempFrame.setVisibility(View.GONE);

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //mTempFrame.setVisibility(View.GONE);
        //mLoginView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
