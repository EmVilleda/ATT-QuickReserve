package quickreserve.app;

/*
*   Initial fragment for the MapActivity screen
* */

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class InitFragment extends Fragment {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Toast toast;

    private ImageView mSectionImage;
    private View mInflatedView;
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle(getString(R.string.select_a_section));
        mInflatedView = inflater.inflate(R.layout.fragment_map, container, false);

        mButton1 = (Button) mInflatedView.findViewById(R.id.button1);
        mButton2 = (Button) mInflatedView.findViewById(R.id.button2);
        mButton3 = (Button) mInflatedView.findViewById(R.id.button3);
        mButton4 = (Button) mInflatedView.findViewById(R.id.button4);
        Log.v("dev_init", "done finding buttons");

        mSectionImage = (ImageView) mInflatedView.findViewById(R.id.sectionImage);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toast != null)
                {
                    toast.cancel();
                }
                Log.v("dev_init", "NW clicked");


                SectionFragment sectionFragment = new SectionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sectionFragment.setFlag(1);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.setCustomAnimations(R.anim.activity_open_translate,R.anim.activity_close_scale);
                transaction.replace(R.id.container, sectionFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toast != null)
                {
                    toast.cancel();
                }

                SectionFragment sectionFragment = new SectionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sectionFragment.setFlag(2);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.setCustomAnimations(R.anim.activity_open_translate,R.anim.activity_close_scale);
                transaction.replace(R.id.container, sectionFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("dev_init", "showing SW toast");
                //zoomImageFromThumb(mButton3, R.drawable.part3);

                SectionFragment sectionFragment = new SectionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sectionFragment.setFlag(3);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.setCustomAnimations(R.anim.activity_open_translate,R.anim.activity_close_scale);
                transaction.replace(R.id.container, sectionFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toast != null)
                {
                    toast.cancel();
                }
                toast = Toast.makeText(getActivity(), "No reservable seats in selected section", Toast.LENGTH_SHORT);
                toast.show();
                Log.v("dev_init", "showing SE toast");
            }
        });

        // Inflate the layout for this fragment
        return mInflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("dev_init", "reached onCreate");


        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (toast != null)
        {
            toast.cancel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toast != null)
        {
            toast.cancel();
        }
    }

}
