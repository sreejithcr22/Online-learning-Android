package com.codit.interview.aptitude.util;

import com.codit.interview.aptitude.R;

/**
 * Created by Sreejith on 16-Sep-16.
 */
public  class APPSTATE {

    public static boolean GOOGLE_PLAY_REQ_VERSION=true;

    public static boolean SUB_BACK_FLAG=false;
    public static boolean DRAWER_FLAG=false;


    public static int EXPL_COUNT=0;
    public static  final int EXPL_AD_FREQ=2;
    public static final int INTERVIEW_AD_FREQ=1;
    public static int RECYCLER_AD_COUNT=0;
    public static final int INTER_AD_FREQ=3;

    public static boolean PARENT_CATEG_QUANTI=false;
    public static boolean PARENT_CATEG_VERBAL=false;
    public static boolean PARENT_CATEG_LOGICAL=false;
    public static boolean PARENT_CATEG_GK=false;

    public static final int QUANTI_QUE_COUNT=372;
    public static final int LOGICAL_QUE_COUNT=300;
    public static final int VERBAL_QUE_COUNT=228;

    public static final int APTI_QUE_COUNT=QUANTI_QUE_COUNT+LOGICAL_QUE_COUNT+VERBAL_QUE_COUNT;
    public static final int GK_QUE_COUNT=300;

    public static int CURRENT_SELECTED_DRAWER_ITEM=0;
    public static boolean MOCK_TEST_ON=false;

    public static  int CURRENT_THEME=0;

    public static final int THEME_DEFAULT= R.style.AppTheme;
    public static final int THEME_BLACK=R.style.AppThemeBlack;

    public static String CURRENT_QUE_SUB_CATEGORY;
    public static boolean OPEN_FROM_TIMER;
    public static String TIMER_CHANGED_CATEGORY;

    public static String DEFAULT_TIME;

    public static String CURRENT_TABLE;
    public static String CURRENT_ACTIVITY_TITLE;
    public static int CURRENT_MIN;
    public static int CURRENT_SEC;
    public static String CURRENT_CATEGORY;
    public static int adCount=0;

    public static boolean visitFlag=false;
    public static boolean reviewFlag=false;
    public static boolean BACK_FLAG=false;
    public static boolean THEME_FLAG=false;

    public static boolean QUESTION_ACTIVITY_FLAG=false;
    public static boolean MOCK_QUESTION_ACTIVITY_FLAG=false;






    public static String TABLE_APTI_NUMBERS="numbers";
    public static String TABLE_APTI_AGE="age";
    public static String TABLE_HCF_AND_LCM="hcf_and_lcm";
    public static String TABLE_TIME_AND_WORK="time_and_work";
    public static String TABLE_TIME_AND_DISTANCE="time_and_distance";
    public static String TABLE_AVG="avg";
    public static String TABLE_PROFIT="profit";
    public static String TABLE_INTEREST="interest";
    public static String TABLE_PERMUTATION="permutation";
    public static String TABLE_RATIO="ratio";
    public static String TABLE_BOAT="boats";
    public static String TABLE_ALLIGATION="alligation";
    public static String TABLE_PERCENTAGE="percentage";
    public static String TABLE_TRAINS="train";
    public static String TABLE_PIPES="pipes";
    public static String TABLE_PARTNERSHIP="partnership";



    public static String TABLE_BLOOD="blood";
    public static String TABLE_SERIES="series";
    public static String TABLE_SEAT="seat";
    public static String TABLE_ANALOGY="analogy";
    public static String TABLE_CALENDAR="calendar";
    public static String TABLE_ESSENTIAL="essential";
    public static String TABLE_STATEMENT_CONCLUSION="statement_conclusion";
    public static String TABLE_STATEMENT_ASSUMPTION="statement_assumptions";
    public static String TABLE_STATEMENT_ARGUEMENT="statement_arguement";
    public static String TABLE_VERIFICATION="verification";
    public static String TABLE_ARITHMETIC="arithmetic";
    public static String TABLE_DIRECTION="direction";
    public static String TABLE_CLASSIFICATION="classification";
    public static String TABLE_DECODE="decode";



    public static String GK__AWARDS="awards";
    public static String GK_BASIC_GK="basic_gk";
    public static String GK_COMPUTER="computer";
    public static String GK_FAMOUS="famous";
    public static String GK_SCIENCE="general_science";
    public static String GK_INDIAN_HISTORY="indian_history";
    public static String GK_INVENTIONS="inventions";
    public static String GK_SPORTS="sports";
    public static String GK_GEO="world_geo";
    public static String GK_INDIAN_GEO="indian_geo";
    public static String GK_POLITICS="politics";

    public static String ANTONYMS="antonyms";
    public static String CLOSET="closet";
    public static String IDIOMS="idioms";
    public static String PREPOSITIONS="preposition";
    public static String PARA="pharagraph";
    public static String ARRANGE="sentence_arrange";
    public static String COMPLETE="sentence_complete";
    public static String SPELL="spelling";
    public static String SUBSTI="substitute";
    public static String SYNONYM="synonyms";




    public static class AptiParentCategs
    {
        public AptiParentCategs(String category1, String time1) {
            category = category1;
            time = time1;

        }

        public  String category;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String time;


    }

    public static AptiParentCategs[] logical;
    public static AptiParentCategs[] quanti;
    public static AptiParentCategs[] verbal;




}
