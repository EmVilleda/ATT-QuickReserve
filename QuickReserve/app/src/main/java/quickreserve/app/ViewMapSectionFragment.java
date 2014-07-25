package quickreserve.app;

/*
* Fragment which zooms in to the selected sections map
* */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DM0497 on 7/24/2014.
 */
public class ViewMapSectionFragment extends Fragment {

    private View mInflatedView;
    private String att_uid;
    private int flag;
    private TouchImageView mSectionImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle(getString(R.string.selectSeat));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle("Section " + selectedSection);

        mInflatedView = inflater.inflate(R.layout.fragment_viewmap_section, container, false);
        mSectionImage = (TouchImageView) mInflatedView.findViewById(R.id.sectionImage);

        Intent intent = getActivity().getIntent();
        att_uid = intent.getStringExtra("att_uid");


        if(flag == 2)
        {
            mSectionImage.setImageResource(R.drawable.section_b);

        }
        else if(flag == 3)
        {
            mSectionImage.setImageResource(R.drawable.section_c);
        }

        return mInflatedView;

    }

    private void changeImageForSeat(String selectedSeat) {

        selectedSeat = selectedSeat.substring(1,selectedSeat.length());
        int seat = Integer.parseInt(selectedSeat);

        if(seat >= 27 && seat <= 32  )
        {
            mSectionImage.setZoom(2f);
            mSectionImage.setScrollPosition(100f,100f);

        }
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSelectedSection() {
        return selectedSection;
    }

    public void setSelectedSection(String selectedSection) {
        this.selectedSection = selectedSection;
    }

    private String selectedSection;

    public TouchImageView getmSectionImage() {
        return mSectionImage;
    }






}
