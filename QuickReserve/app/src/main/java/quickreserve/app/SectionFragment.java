package quickreserve.app;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SectionFragment extends Fragment {

    private View mInflatedView;
    private Button mReserveButton;
    private ListView mAvailableSeatList;
    private LinearLayout mSelectionLayout;
    private MySQLiteHelper mySQLiteHelper;
    private String att_uid;
    private int tempDate_int;
    private int flag;
    private boolean firstClick;
    private int start_time;
    private int end_time;
    private int day_selected;
    private int month_selected;
    private int year_selected;
    private String selectedSeat;



    public TouchImageView getmSectionImage() {
        return mSectionImage;
    }

    private TouchImageView mSectionImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle(getString(R.string.selectSeat));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle(getString(R.string.selectSeat));
        mInflatedView = inflater.inflate(R.layout.fragment_section, container, false);
        mSectionImage = (TouchImageView) mInflatedView.findViewById(R.id.sectionImage);
        mAvailableSeatList = (ListView)mInflatedView.findViewById(R.id.availableSeatList);
        mReserveButton = (Button)mInflatedView.findViewById(R.id.reserveButton);
        mSelectionLayout = (LinearLayout)mInflatedView.findViewById(R.id.selectionLayout);
        mySQLiteHelper = new MySQLiteHelper(getActivity());

        Intent intent = getActivity().getIntent();
        att_uid = intent.getStringExtra("att_uid");
        tempDate_int = intent.getIntExtra("date_selected", -1);
        final int newDate = tempDate_int;
        start_time = intent.getIntExtra("start_time", -1);
        end_time = intent.getIntExtra("end_time", -1);
        Toast.makeText(getActivity(), start_time + " " + end_time, Toast.LENGTH_SHORT).show();

        final int newStartTime = start_time;
        final int newEndTime = end_time;




        if(flag == 2)
        {
            mSectionImage.setImageResource(R.drawable.section_b);

        }
        else if(flag == 3)
        {
            mSectionImage.setImageResource(R.drawable.section_c);
        }

        populateSpinner();


        mAvailableSeatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("test", "" + mAvailableSeatList.getAdapter().getItem(position).toString());
                selectedSeat = mAvailableSeatList.getAdapter().getItem(position).toString();
            }
        });

        mReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReservationController controller = new ReservationController(getActivity());
                //Toast.makeText(getActivity(), att_uid + " " + "", Toast.LENGTH_SHORT).show();

                if(selectedSeat != null)
                {
                    int result = controller.createReservation(selectedSeat, att_uid, newStartTime, newEndTime, newDate);
                    if (result == 0)
                        Toast.makeText(getActivity(), "Database Error occurred, see LogCat", Toast.LENGTH_SHORT).show();
                    else if (result == 1)
                        Toast.makeText(getActivity(), "You've already reserved a seat during this time frame", Toast.LENGTH_SHORT).show();
                    else if (result == 2) {
                        Toast.makeText(getActivity(), "Reservation successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), MyReservationActivity.class);
                        i.putExtra("att_uid", att_uid);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    } else
                        Toast.makeText(getActivity(), "General error: int flag not set or recognized", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Please select a seat", Toast.LENGTH_SHORT).show();

                }


            }
        });

        return mInflatedView;


    }

    private void populateSpinner() {
        try
        {
            Log.e("test","flag:  " + flag);
            List<Workspace> workspaces = mySQLiteHelper.getOpenWorkspaces(tempDate_int, start_time
                    , end_time);
            ArrayList<String> workspaceNames = new ArrayList<String>();
            int size = workspaces.size();
            for (int i = 0; i < size; i++) {
                if (workspaces.get(i).getSector() == flag)
                    workspaceNames.add(workspaces.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_single_choice, workspaceNames);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mAvailableSeatList.setAdapter(adapter);
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
