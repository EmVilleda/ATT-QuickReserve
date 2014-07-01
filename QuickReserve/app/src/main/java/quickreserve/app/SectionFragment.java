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
        tempID = intent.getStringExtra("ID");
        hour_start = intent.getIntExtra("hour_start", -1);
        min_start = intent.getIntExtra("min_start", -1);
        hour_end = intent.getIntExtra("hour_end", -1);
        min_end = intent.getIntExtra("min_end", -1);

        if(flag == 2)
        {
            mSectionImage.setImageResource(R.drawable.section_b);

        }
        else if(flag == 3)
        {
            mSectionImage.setImageResource(R.drawable.section_c);
        }



/*
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayofmonth) {

                tempDate = (month + 1)  + "/" + dayofmonth;

                Log.e("test","date changed");
                Log.e("test","button: " + mSelectDateButton.getText());
                Log.e("test","temp: " + tempDate);


                if(firstClick || (!tempDate.equals(mSelectDateButton.getText()) && !mSelectDateButton.getText().toString().equals("Select Date")))
                {
                    mSectionImage.setVisibility(View.VISIBLE);
                    mCalendarView.setVisibility(View.GONE);
                    mSelectionLayout.setVisibility(View.VISIBLE);
                    if(firstClick)
                    {
                        firstClick = false;

                    }
                }

                mSelectDateButton.setText(tempDate);

                tempDate_int = (year * 10000 + (month+1) * 100 + dayofmonth);
                Log.e("test","asd: " + tempDate_int);


                try
                {
                    Log.e("test","flag:  " + flag);
                    List<Workspace> workspaces = mySQLiteHelper.getMasterWorkspacesList(tempDate_int, 800, 1700, flag);
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

                mReserveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ReservationController controller = new ReservationController(getActivity());
                        int result = controller.createReservation(mSeatSpinner.getSelectedItem().toString() ,tempID, 700, 1700, tempDate_int);
                        if(result == 0)
                            Toast.makeText(getActivity(), "Database Error occurred, see LogCat", Toast.LENGTH_SHORT).show();
                        else if(result == 1)
                            Toast.makeText(getActivity(), "You've already reserved a seat during this time frame", Toast.LENGTH_SHORT).show();
                        else if(result == 2)
                            Toast.makeText(getActivity(), "Reservation successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "General error: int flag not set or recognized", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/

        return mInflatedView;


    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }



}
