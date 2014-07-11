package quickreserve.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import quickreserve.app.R;

public class FavoriteSeatActivity extends Activity {

    protected TextView seat1Text;
    protected TextView seat2Text;
    protected TextView seat3Text;
    protected Button seat1Button;
    protected Button seat2Button;
    protected Button seat3Button;
    protected Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_seat);
        final Context context = this;

        seat1Text = (TextView) findViewById(R.id.favoriteSeatText1);
        seat2Text = (TextView) findViewById(R.id.favoriteSeatText2);
        seat3Text = (TextView) findViewById(R.id.favoriteSeatText3);

        seat1Button = (Button) findViewById(R.id.favoriteSeatButton1);
        seat1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 1);
                String seat = seat1Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat2Button = (Button) findViewById(R.id.favoriteSeatButton2);
        seat2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 2);
                String seat = seat2Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });
        seat3Button = (Button) findViewById(R.id.favoriteSeatButton3);
        seat3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 3);
                String seat = seat3Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        submitButton = (Button) findViewById(R.id.favoriteSeatSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = p.edit();
                editor.putString("seat1", seat1Text.getText().toString());
                editor.putString("seat2", seat2Text.getText().toString());
                editor.putString("seat3", seat3Text.getText().toString());
                editor.commit();
                Toast.makeText(context, "Favorite seats successfully saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        initialSeats();
    }

    //sets seats to initial preffered values
    public void initialSeats(){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        updateSeat(seat1Text, p.getString("seat1", ""));
        updateSeat(seat2Text, p.getString("seat2", ""));
        updateSeat(seat3Text, p.getString("seat3", ""));
    }

    public void updateSeat(TextView view, String seat){
        if(seat.equals("")){
            view.setText("Select a seat");
        }
        else{
            view.setText(seat);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //selected = # of favorite
                int selectedSeat = data.getIntExtra("selectedFavorite", -1);
                String seatName = data.getStringExtra("selectedSeat");
                if(seatName == null || seatName.equals("")){
                    seatName = "Select a seat";
                }

                if(selectedSeat==1){
                    updateSeat(seat1Text, seatName);
                }
                else if(selectedSeat==2){
                    updateSeat(seat2Text, seatName);
                }
                else if(selectedSeat==3){
                    updateSeat(seat3Text, seatName);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_seat, menu);
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
}
