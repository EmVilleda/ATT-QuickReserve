package quickreserve.app;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SectionFragment extends Fragment {

    private View mInflatedView;
    private Button mReserveButton;
    private Spinner mSeatSpinner;
    private LinearLayout mSelectionLayout;
    private MySQLiteHelper mySQLiteHelper;
    private String tempID;
    private int tempDate_int;
    private int flag;
    private boolean firstClick;
    private int hour_start;
    private int min_start;
    private int hour_end;
    private int min_end;
    private int day_selected;
    private int month_selected;
    private int year_selected;



    public ImageView getmSectionImage() {
        return mSectionImage;
    }

    private ImageView mSectionImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mInflatedView = inflater.inflate(R.layout.fragment_section, container, false);
        mSectionImage = (ImageView) mInflatedView.findViewById(R.id.sectionImage);
        mSeatSpinner = (Spinner)mInflatedView.findViewById(R.id.seatSpinner);
        mReserveButton = (Button)mInflatedView.findViewById(R.id.reserveButton);
        mSelectionLayout = (LinearLayout)mInflatedView.findViewById(R.id.selectionLayout);
        mySQLiteHelper = new MySQLiteHelper(getActivity());

        Intent intent = getActivity().getIntent();
        tempID = intent.getStringExtra("att_uid");
        tempDate_int = intent.getIntExtra("date_selected", -1);
        final int newDate = tempDate_int;
        hour_start = intent.getIntExtra("hour_start", -1);
        min_start = intent.getIntExtra("min_start", -1);
        hour_end = intent.getIntExtra("hour_end", -1);
        min_end = intent.getIntExtra("min_end", -1);



        final int start_time = (hour_start * 100) + min_start;
        final int end_time = (hour_end * 100) + min_end;


        if(flag == 2)
        {
            mSectionImage.setImageResource(R.drawable.section_b);

        }
        else if(flag == 3)
        {
            mSectionImage.setImageResource(R.drawable.section_c);
        }

        populateSpinner();





        mReserveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ReservationController controller = new ReservationController(getActivity());
            Toast.makeText(getActivity(), tempID + " " + newDate, Toast.LENGTH_SHORT).show();
            int result = controller.createReservation(mSeatSpinner.getSelectedItem().toString(),tempID, start_time, end_time, newDate);
            if(result == 0)
                Toast.makeText(getActivity(), "Database Error occurred, see LogCat", Toast.LENGTH_SHORT).show();
            else if(result == 1)
                Toast.makeText(getActivity(), "You've already reserved a seat during this time frame", Toast.LENGTH_SHORT).show();
            else if(result == 2){
                Toast.makeText(getActivity(), "Reservation successful", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else
                Toast.makeText(getActivity(), "General error: int flag not set or recognized", Toast.LENGTH_SHORT).show();
        }
    });

        return mInflatedView;


    }

    private void populateSpinner() {
        try
        {
            tempDate_int = year_selected * 10000 + (month_selected+1) * 100 + day_selected;
            Log.e("test","flag:  " + flag);
            List<Workspace> workspaces = mySQLiteHelper.getMasterWorkspacesList(tempDate_int, hour_start*100 + min_start
                    , hour_end *100 + min_end, flag);
            ArrayList<String> workspaceNames = new ArrayList<String>();
            int size = workspaces.size();
            for (int i = 0; i < size; i++) {
                if (workspaces.get(i).getIsBooked() == 0)
                    workspaceNames.add(workspaces.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, workspaceNames);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mSeatSpinner.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_SHORT).show();

        }
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
