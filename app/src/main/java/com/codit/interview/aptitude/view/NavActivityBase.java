package com.codit.interview.aptitude.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codit.interview.aptitude.R;
import com.codit.interview.aptitude.model.NavListRow;
import com.codit.interview.aptitude.model.ParentCategory;
import com.codit.interview.aptitude.util.APPSTATE;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

/**
 * Created by Sreejith on 26-Jul-16.
 */
public class NavActivityBase extends AppCompatActivity {
    CircleProgressBar navProgress;
    protected int bottomTabItemId = -1;
    protected boolean showBottomBar = true;


    boolean clicked = false;
    static final String MAIN_ACTIVITY = "main activity";
    static final String OTHER_ACTIVITY = "other activity";
    public static final String GK_FRAGMENT = "gk frag";
    public String currentActivity;

    public ListView settingsNav, menuView;
    public DrawerLayout drawer;
    Toolbar toolbar;
    boolean showToggleIcon, showDoubleDrawer;
    FrameLayout container;
    AppBarLayout appLayout;
    int contentLayout;

    NavListAdapter adapter, adapter1;

    ViewPager viewPager;

    public TabLayout tabLayout;
    public TabAdapter tabAdapter;

    public static final String TAG = "aptitude";


    public void initialize(final int contentView) {


        if (APPSTATE.CURRENT_THEME == 0) {
            APPSTATE.CURRENT_THEME = getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE)
                    .getInt("app_theme", APPSTATE.THEME_DEFAULT);
        }
        if (APPSTATE.CURRENT_THEME != 0) {
            setTheme(APPSTATE.CURRENT_THEME);
        } else {
            APPSTATE.CURRENT_THEME = APPSTATE.THEME_DEFAULT;
            setTheme(APPSTATE.THEME_DEFAULT);

        }

        this.contentLayout = contentView;


        showDoubleDrawer = true;
        showToggleIcon = true;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        overridePendingTransition(R.anim.trans_slide_right, R.anim.trans_slide_left);

        if (APPSTATE.BACK_FLAG) {
            APPSTATE.BACK_FLAG = false;
            overridePendingTransition(R.anim.trans_back_exit, R.anim.trans_bac_enter);
        } else if (APPSTATE.THEME_FLAG) {
            APPSTATE.THEME_FLAG = false;
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        }


        setContentView(R.layout.activity_main);

        appLayout = (AppBarLayout) findViewById(R.id.appBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.more_black));


        if (currentActivity.equals(MAIN_ACTIVITY)) {
            toolbar.setTitle("Analytics");

        }
        setSupportActionBar(toolbar);


        container = (FrameLayout) findViewById(R.id.containerLayout);

        if (contentLayout != 0) {
            container.removeAllViews();

            LayoutInflater layoutInflater = (LayoutInflater)
                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(contentLayout, container, false);

            container.addView(layout);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        if (!showToggleIcon) {
            toggle.setDrawerIndicatorEnabled(false);
        }

        drawer.setDrawerListener(toggle);
        drawer.setBackgroundColor(Color.WHITE);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED, androidx.core.view.GravityCompat.START);


        menuView = (ListView) findViewById(R.id.left_nav_view);


        settingsNav = (ListView) findViewById(R.id.right_nav_view);


        NavListRow[] rows = new NavListRow[5];
        rows[0] = new NavListRow(R.drawable.ic_question, "Questions");
        rows[1] = new NavListRow(R.drawable.ic_interview, "Interview");
        rows[2] = new NavListRow(R.drawable.ic_square_root, "Formulas");
        rows[4] = new NavListRow(R.drawable.ic_heart, "Favourites");
        rows[3] = new NavListRow(R.drawable.ic_exam, "Mock Test");

        NavListRow[] settingsRows = new NavListRow[6];
        settingsRows[1] = new NavListRow(R.drawable.ic_settings, "Settings");
        settingsRows[0] = new NavListRow(R.drawable.ic_theme, "Themes");
        settingsRows[2] = new NavListRow(R.drawable.ic_share_main, "Share App");
        settingsRows[3] = new NavListRow(R.drawable.ic_star, "Rate us");
        settingsRows[4] = new NavListRow(R.drawable.ic_about, "About Us");
        settingsRows[5] = new NavListRow(R.drawable.ic_moon, "Night Mode");


        adapter = new NavListAdapter(this, R.layout.settings_nav_row, settingsRows);


        {
            adapter1 = new NavListAdapter(this, R.layout.menu_nav_row, rows);

            int width = getResources().getDisplayMetrics().widthPixels;


            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) menuView.getLayoutParams();
            params.width = (int) (width * .78);

            View header = (View) getLayoutInflater().inflate(R.layout.nav_header_main, null);
            View border = header.findViewById(R.id.border);
            if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_BLACK) {
                border.setVisibility(View.VISIBLE);
            } else {
                border.setVisibility(View.GONE);
            }
            AbsListView.LayoutParams headerParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
            headerParams.height = (int) (getResources().getDisplayMetrics().heightPixels * .25);
            header.setLayoutParams(headerParams);

            navProgress = (CircleProgressBar) header.findViewById(R.id.navCircle);

            SharedPreferences progressPreference = getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

            int aptiAttempted = progressPreference.getInt("APTI_ATTEMPTED", 0);
            int gkAttempted = progressPreference.getInt("GK_ATTEMPTED", 0);

            int grandTotal = APPSTATE.APTI_QUE_COUNT + APPSTATE.GK_QUE_COUNT;


            navProgress.setProgress(((aptiAttempted + gkAttempted) * 100) / grandTotal);

            menuView.addHeaderView(header);

            menuView.setLayoutParams(params);

        }


        menuView.setAdapter(adapter1);

        settingsNav.setAdapter(adapter);
        int width1 = getResources().getDisplayMetrics().widthPixels / 4;


        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) settingsNav.getLayoutParams();
        params.width = width1;
        settingsNav.setLayoutParams(params);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                SharedPreferences progressPreference = getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

                int aptiAttempted = progressPreference.getInt("APTI_ATTEMPTED", 0);
                int gkAttempted = progressPreference.getInt("GK_ATTEMPTED", 0);

                int grandTotal = APPSTATE.APTI_QUE_COUNT + APPSTATE.GK_QUE_COUNT;


                navProgress.setProgress(((aptiAttempted + gkAttempted) * 100) / grandTotal);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                APPSTATE.CURRENT_SELECTED_DRAWER_ITEM = i;

                SharedPreferences pref = getBaseContext().getSharedPreferences("timer", MODE_PRIVATE);

                pref.edit().putBoolean("stopTimer", true).apply();

                clicked = true;
                closeDrawer(menuView);
                drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                        if (clicked) {
                            switch (i) {

                                case 0:
                                    Intent intent0 = new Intent(getBaseContext(), ParentCategory.class);
                                    startActivity(intent0);
                                    break;

                                case 1:
                                    Intent intent = new Intent(getBaseContext(), ParentCategory.class);
                                    startActivity(intent);
                                    break;

                                case 2:
                                    Intent intent1 = new Intent(getBaseContext(), InterviewActivity.class);
                                    startActivity(intent1);
                                    break;

                                case 3:
                                    Intent intent2 = new Intent(getBaseContext(), ConceptsActivity.class);
                                    startActivity(intent2);
                                    break;
                                case 5:
                                    Intent intent3 = new Intent(getBaseContext(), FavActivity.class);
                                    startActivity(intent3);
                                    break;
                                case 4:
                                    Intent intent4 = new Intent(getBaseContext(), MockActivity.class);
                                    startActivity(intent4);
                                    break;

                            }
                            clicked = false;
                        }


                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                });


            }
        });

        settingsNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        changeTheme();

                        break;

                    case 1:
                        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        shareApp();
                        break;
                    case 4:
                        about();

                        break;


                    case 3:
                        showFeedback();
                        break;

                    case 5:

                        if (APPSTATE.CURRENT_THEME != APPSTATE.THEME_BLACK) {
                            APPSTATE.CURRENT_THEME = APPSTATE.THEME_BLACK;
                            getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE)
                                    .edit().putInt("app_theme", APPSTATE.CURRENT_THEME).apply();
                            RestartActivty();
                            APPSTATE.THEME_FLAG = true;
                        } else {
                            Toast.makeText(getBaseContext(), "Night mode already activate !", Toast.LENGTH_SHORT).show();
                        }
                        break;


                }

                closeDrawer(settingsNav);

            }
        });


        if (!showDoubleDrawer) {
            drawer.removeView(settingsNav);
        }

        if (APPSTATE.quanti == null || APPSTATE.logical == null || APPSTATE.verbal == null) {
            new com.codit.interview.aptitude.util.UserStateDB(this).readAptiCategs();
        }

        ensureNotificationPermission();
        SharedPreferences firstRun = getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        if (firstRun.getInt("visitCount", 0) == 0) {
            com.codit.interview.aptitude.util.RebootReceiver.scheduleDaily(this);
            firstRun.edit().putInt("visitCount", 1).apply();
        }

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setItemIconTintList(null);
        bottomNav.setLabelVisibilityMode(com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        if (!showBottomBar) {
            bottomNav.setVisibility(View.GONE);
        } else {
            if (bottomTabItemId != -1) {
                bottomNav.setSelectedItemId(bottomTabItemId);
            }
            bottomNav.setOnItemSelectedListener(new com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(android.view.MenuItem item) {
                    int id = item.getItemId();
                    if (id == bottomTabItemId) {
                        return true;
                    }
                    Intent target = null;
                    if (id == R.id.nav_questions) {
                        target = new Intent(NavActivityBase.this, ParentCategory.class);
                    } else if (id == R.id.nav_interview) {
                        target = new Intent(NavActivityBase.this, InterviewActivity.class);
                    } else if (id == R.id.nav_formulas) {
                        target = new Intent(NavActivityBase.this, ConceptsActivity.class);
                    } else if (id == R.id.nav_mock) {
                        target = new Intent(NavActivityBase.this, MockActivity.class);
                    } else if (id == R.id.nav_favorites) {
                        target = new Intent(NavActivityBase.this, FavActivity.class);
                    }
                    if (target != null) {
                        startActivity(target);
                    }
                    return true;
                }
            });
        }
    }

    private static final int REQ_NOTIFY = 1001;

    public void ensureNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onBack() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (showDoubleDrawer && drawer.isDrawerOpen(settingsNav)) {
            drawer.closeDrawer(settingsNav);

        } else {
            exitApp(true);
        }
    }


    public void closeDrawer(ListView listView) {
        if (drawer.isDrawerOpen(listView)) {
            drawer.closeDrawer(listView);
        }
    }


    public void intitTab(HashMap<Integer, Fragment> fragments, HashMap<Integer, String> titles, final int contentView) {


        initialize(contentView);

        viewPager = (ViewPager) findViewById(R.id.viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);

        if (APPSTATE.CURRENT_THEME == APPSTATE.THEME_DEFAULT) {
            tabLayout.setTabTextColors(Color.parseColor("#E0E0E0"), Color.parseColor("#ffffff"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        } else {
            tabLayout.setTabTextColors(Color.parseColor("#E0E0E0"), Color.parseColor("#00acc1"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00acc1"));
        }

        boolean flag = false;
        if (currentActivity.equals(GK_FRAGMENT))
            flag = true;

        tabAdapter = new TabAdapter(getSupportFragmentManager(), fragments, titles, flag);


        viewPager.setAdapter(tabAdapter);


    }

    public void showFeedback() {
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);


        View titleView = getLayoutInflater().inflate(R.layout.dialog_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.dialogTitle);
        ImageView icon = (ImageView) titleView.findViewById(R.id.dialogIcon);
        icon.setImageResource(R.drawable.ic_about);
        title.setText("Feedback");
        builder.setCustomTitle(titleView);


        String[] choice = {"Rate and Review", "Email Us"};
        builder.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
                feedbackListener(i);
            }
        })
                .setNegativeButton("Cancel", null);

        Dialog dialog = builder.create();

        dialog.show();


    }

    public void feedbackListener(int i) {
        if (i == 0) {
            openStore();
        } else if (i == 1) {
            final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setCancelable(false);

            View titleView = getLayoutInflater().inflate(R.layout.dialog_title, null);
            TextView title = (TextView) titleView.findViewById(R.id.dialogTitle);
            title.setText("Email us");
            ImageView icon = (ImageView) titleView.findViewById(R.id.dialogIcon);
            icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail));
            builder.setCustomTitle(titleView);

            final EditText feedback = new EditText(getBaseContext());
            feedback.setLines(5);

            feedback.setGravity(Gravity.CENTER);
            feedback.setTextColor(Color.BLACK);
            feedback.setPadding(10, 10, 0, 0);


            feedback.setHint("Share your feedback here!");
            feedback.setHintTextColor(Color.LTGRAY);

            builder.setView(feedback)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (feedback.getText().length() != 0) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "codit.apps@gmail.com", null));


                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                intent.putExtra(Intent.EXTRA_TEXT, feedback.getText());

                                startActivity(Intent.createChooser(intent, "Send feedback"));
                            } else {
                                Toast.makeText(getBaseContext(), "Please enter feedback !", Toast.LENGTH_SHORT).show();
                                feedbackListener(1);
                            }

                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(feedback.getWindowToken(), 0);

                            showFeedback();

                        }
                    });
            Dialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.show();
        }
    }

    public void exitApp(final boolean onMain) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Exit application?")
                .setCancelable(true)
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create().show();
    }


    public void changeTheme() {
        final int prevTheme = APPSTATE.CURRENT_THEME;


        String[] themes = {"Default", "Night"};
        int current = 0;

        switch (prevTheme) {
            case APPSTATE.THEME_DEFAULT:
                current = 0;
                break;
            case APPSTATE.THEME_BLACK:
                current = 1;
                break;

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View titleView = getLayoutInflater().inflate(R.layout.dialog_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.dialogTitle);
        title.setText("Themes");
        ImageView icon = (ImageView) titleView.findViewById(R.id.dialogIcon);
        icon.setImageResource(R.drawable.ic_theme);

        builder.setCustomTitle(titleView);

        builder.setSingleChoiceItems(themes, current, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {

                    case 0:
                        APPSTATE.CURRENT_THEME = APPSTATE.THEME_DEFAULT;
                        break;
                    case 1:
                        APPSTATE.CURRENT_THEME = APPSTATE.THEME_BLACK;
                        break;


                }

            }
        })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getBaseContext().getSharedPreferences("progress", Context.MODE_PRIVATE)
                                .edit().putInt("app_theme", APPSTATE.CURRENT_THEME).apply();
                        RestartActivty();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        APPSTATE.CURRENT_THEME = prevTheme;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        APPSTATE.CURRENT_THEME = prevTheme;
                    }
                });

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CalcAnimation;
        dialog.show();


    }

    public void RestartActivty() {
        APPSTATE.THEME_FLAG = true;
        Intent intent = new Intent(getBaseContext(), ParentCategory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void about() {
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(true);


        View titleView = getLayoutInflater().inflate(R.layout.dialog_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.dialogTitle);
        ImageView icon = (ImageView) titleView.findViewById(R.id.dialogIcon);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_about));
        title.setText("About");
        builder.setCustomTitle(titleView);

        View view = getLayoutInflater().inflate(R.layout.about_content, null);
        LinearLayout like, share, rate, contact, credit;
        like = (LinearLayout) view.findViewById(R.id.like);
        share = (LinearLayout) view.findViewById(R.id.shareapp);
        rate = (LinearLayout) view.findViewById(R.id.rating);
        contact = (LinearLayout) view.findViewById(R.id.contact);
        credit = (LinearLayout) view.findViewById(R.id.credit);

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    getBaseContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);


                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/449918455358346"));

                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Offline-Aptitude-and-GK-App-449918455358346"));
                    startActivity(intent);

                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareApp();


            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStore();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "codit.apps@gmail.com", null));


                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");


                startActivity(Intent.createChooser(intent, "Send feedback"));
            }
        });
        builder.setView(view);

        builder.setPositiveButton("DISMISS", null);

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CalcAnimation;

        dialog.show();

    }

    public void credit() {
        String message = "achartengine - " + "https://github.com/ddanny/achartengine" + "\n\n" + "dinuscxj/CircleProgressBar - https://github.com/dinuscxj/CircleProgressBar\n\n" + "Icons- http://www.flaticon.com/";
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder2.create().show();
    }

    public void openStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));


        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);


        intent.setType("text/plain");

        String text = "Download the free all in one offline guide containing Aptitude , Verbal reasoning , Logical reasoning and GK questions and answers along with detailed explanations,concepts And formulas plus Technical,HR,and many more Interview tips and tricks.\n" + "https://play.google.com/store/apps/details?id=" + getPackageName();

        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_exit, R.anim.trans_bac_enter);
    }
}
