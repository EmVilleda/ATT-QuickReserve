package quickreserve.app;


/*
*   Help activity class. Something like an FAQ section
*
* */

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import quickreserve.app.R;

public class HelpActivity extends Activity {

    private Button mQuestion1;
    private TextView mAnswer1;
    private Button mQuestion2;
    private TextView mAnswer2;
    private Button mQuestion3;
    private TextView mAnswer3;
    private Button mQuestion4;
    private TextView mAnswer4;
    private Button mQuestion5;
    private TextView mAnswer5;
    private Button mQuestion6;
    private TextView mAnswer6;
    private Button mQuestion7;
    private TextView mAnswer7;
    private Animation fadeIn;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String att_uid;
    private static final String ACTIVITY_DRAWER_REF = "Help";
    private ValueAnimator mAnimator;
    private int mHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getActionBar().setTitle("Help");
        att_uid = getIntent().getStringExtra("att_uid");

        mQuestion1 = (Button)findViewById(R.id.question1);
        mQuestion2 = (Button)findViewById(R.id.question2);
        mQuestion3 = (Button)findViewById(R.id.question3);
        mQuestion4 = (Button)findViewById(R.id.question4);
        mQuestion5 = (Button)findViewById(R.id.question5);
        mQuestion6 = (Button)findViewById(R.id.question6);
        mQuestion7 = (Button)findViewById(R.id.question7);

        mAnswer1 = (TextView)findViewById(R.id.answer1);
        mAnswer2 = (TextView)findViewById(R.id.answer2);
        mAnswer3 = (TextView)findViewById(R.id.answer3);
        mAnswer4 = (TextView)findViewById(R.id.answer4);
        mAnswer5 = (TextView)findViewById(R.id.answer5);
        mAnswer6 = (TextView)findViewById(R.id.answer6);
        mAnswer7 = (TextView)findViewById(R.id.answer7);

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);



        mOptionsList = getResources().getStringArray(R.array.options_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> drawerOptions = new ArrayList<String>(Arrays.asList(mOptionsList));



        // Set the adapter for the list view
        mDrawerList.setAdapter(new MyDrawerRowAdapter(this,
                R.layout.my_drawer_row_layout, drawerOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(att_uid, ACTIVITY_DRAWER_REF
                ,getApplicationContext(),this,mDrawerLayout,mOptionsList));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Help");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(getString(R.string.options));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer1.getVisibility() == View.VISIBLE)
                            mAnswer1.setVisibility(View.GONE);
                        else
                            mAnswer1.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer1.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer1.setAnimation(fadeIn);
                    mAnswer1.setVisibility(View.INVISIBLE);
                    mAnswer1.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer1.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }
            }
        });

        mQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer2.getVisibility() == View.VISIBLE)
                            mAnswer2.setVisibility(View.GONE);
                        else
                            mAnswer2.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer2.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer2.setAnimation(fadeIn);
                    mAnswer2.setVisibility(View.INVISIBLE);
                    mAnswer2.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer2.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

        mQuestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer3.getVisibility() == View.VISIBLE)
                            mAnswer3.setVisibility(View.GONE);
                        else
                            mAnswer3.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer3.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer3.setAnimation(fadeIn);
                    mAnswer3.setVisibility(View.INVISIBLE);
                    mAnswer3.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer3.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

        mQuestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer4.getVisibility() == View.VISIBLE)
                            mAnswer4.setVisibility(View.GONE);
                        else
                            mAnswer4.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer4.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer4.setAnimation(fadeIn);
                    mAnswer4.setVisibility(View.INVISIBLE);
                    mAnswer4.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer4.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

        mQuestion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer5.getVisibility() == View.VISIBLE)
                            mAnswer5.setVisibility(View.GONE);
                        else
                            mAnswer5.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer5.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer5.setAnimation(fadeIn);
                    mAnswer5.setVisibility(View.INVISIBLE);
                    mAnswer5.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer5.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

        mQuestion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer6.getVisibility() == View.VISIBLE)
                            mAnswer6.setVisibility(View.GONE);
                        else
                            mAnswer6.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer6.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer6.setAnimation(fadeIn);
                    mAnswer6.setVisibility(View.INVISIBLE);
                    mAnswer6.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer6.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

        mQuestion7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("test", "animation should start");
                        if(mAnswer7.getVisibility() == View.VISIBLE)
                            mAnswer7.setVisibility(View.GONE);
                        else
                            mAnswer7.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (mAnswer7.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mAnswer7.setAnimation(fadeIn);
                    mAnswer7.setVisibility(View.INVISIBLE);
                    mAnswer7.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mAnswer7.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }

            }
        });

    }

    private void getHeightForAnimation(View view){

        final View viewTemp = view;

        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // gets called after layout has been done but before display
                        // so we can get the height then hide the view

                        mHeight = viewTemp.getHeight();

                        viewTemp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mAnimator = slideAnimator(0, mHeight);
                        viewTemp.setVisibility(View.GONE);
                    }

                }
        );

    }

    private void expand(View view) {
        //set Visible
        view.setVisibility(View.VISIBLE);

		/* Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/

        mAnimator.start();
    }

    private void collapse(View view)
    {
        final View viewTemp = view;
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                viewTemp.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mAnswer1.getLayoutParams();
                layoutParams.height = value;
                mAnswer1.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    public void expandOrCollapse(final View v,String exp_or_colpse) {
        TranslateAnimation anim = null;
        if(exp_or_colpse.equals("expand"))
        {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        }
        else{
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
            Animation.AnimationListener collapselistener= new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            anim.setAnimationListener(collapselistener);
        }

        // To Collapse
        //

        anim.setDuration(300);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
