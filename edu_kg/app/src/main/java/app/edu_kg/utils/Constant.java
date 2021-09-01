package app.edu_kg.utils;


import android.graphics.Color;

import java.util.EnumMap;
import app.edu_kg.utils.adapter.SubjectGridAdapter.Subject;

import app.edu_kg.R;

public class Constant {
    public final static int MESSAGE_LIST_RESPONSE = 1001;
    public final static int LOGIN_RESPONSE_SUCCESS = 1002;
    public final static int LOGIN_RESPONSE_FAIL = 1003;
    public final static int MODIFY_RESPONSE_SUCCESS = 1004;
    public final static int MODIFY_RESPONSE_FAIL = 1005;
    public final static int LIST_RESPONSE_SUCCESS = 1006;
    public final static int LIST_RESPONSE_FAIL = 1007;
    public final static int REGISTER_RESPONSE_SUCCESS = 1008;
    public final static int REGISTER_RESPONSE_FAIL = 1009;


    public final static int HOME_ENTITY_RESPONSE_SUCCESS = 2004;
    public final static int HOME_ENTITY_RESPONSE_FAIL = 2005;
    public final static int LINK_INSTANCE_RESPONSE = 2006;
    public final static int INSTANCE_LIST_RESPONSE = 2007;
    public final static int QUESTION_LIST_RESPONSE = 2008;


    public final static int HISTORY_PAGE = 3000;
    public final static int COLLECTION_PAGE = 3001;
    public final static int RECOMMENDATION_PAGE = 3002;
    
    public enum SUBJECT_NAME
    {
        CHINESE, MATH, ENGLISH, PHYSICS, CHEMISTRY,
        BIOLOGY, POLITICS, HISTORY, GEOGRAPHY, FOLD, UNFOLD;
    }

    public final static EnumMap<SUBJECT_NAME, Subject> helperSubjectMap = new EnumMap<SUBJECT_NAME, Subject>(SUBJECT_NAME.class){{
       put(SUBJECT_NAME.CHINESE, new Subject("语文", R.drawable.subject_chinese, SUBJECT_NAME.CHINESE));
       put(SUBJECT_NAME.MATH, new Subject("数学", R.drawable.subject_math, SUBJECT_NAME.MATH));
       put(SUBJECT_NAME.ENGLISH, new Subject("英语", R.drawable.subject_english, SUBJECT_NAME.ENGLISH));
       put(SUBJECT_NAME.PHYSICS, new Subject("物理", R.drawable.subject_physics, SUBJECT_NAME.PHYSICS));
       put(SUBJECT_NAME.CHEMISTRY, new Subject("化学", R.drawable.subject_chemistry, SUBJECT_NAME.CHEMISTRY));
       put(SUBJECT_NAME.BIOLOGY, new Subject("生物", R.drawable.subject_biology, SUBJECT_NAME.BIOLOGY));
       put(SUBJECT_NAME.POLITICS, new Subject("政治", R.drawable.subject_politics, SUBJECT_NAME.POLITICS));
       put(SUBJECT_NAME.HISTORY, new Subject("历史", R.drawable.subject_history, SUBJECT_NAME.HISTORY));
       put(SUBJECT_NAME.GEOGRAPHY, new Subject("地理", R.drawable.subject_geography, SUBJECT_NAME.GEOGRAPHY));
       put(SUBJECT_NAME.FOLD, new Subject("收起", R.drawable.subject_fold, SUBJECT_NAME.FOLD));
       put(SUBJECT_NAME.UNFOLD, new Subject("展开", R.drawable.subject_unfold, SUBJECT_NAME.UNFOLD));
    }};

    public final static EnumMap<SUBJECT_NAME, Subject> homeSubjectMap = new EnumMap<SUBJECT_NAME, Subject>(SUBJECT_NAME.class){{
        put(SUBJECT_NAME.CHINESE, new Subject("语文", R.drawable.subject_chinese, SUBJECT_NAME.CHINESE));
        put(SUBJECT_NAME.MATH, new Subject("数学", R.drawable.subject_math, SUBJECT_NAME.MATH));
        put(SUBJECT_NAME.ENGLISH, new Subject("英语", R.drawable.subject_english, SUBJECT_NAME.ENGLISH));
        put(SUBJECT_NAME.PHYSICS, new Subject("物理", R.drawable.subject_physics, SUBJECT_NAME.PHYSICS));
        put(SUBJECT_NAME.CHEMISTRY, new Subject("化学", R.drawable.subject_chemistry, SUBJECT_NAME.CHEMISTRY));
        put(SUBJECT_NAME.BIOLOGY, new Subject("生物", R.drawable.subject_biology, SUBJECT_NAME.BIOLOGY));
        put(SUBJECT_NAME.POLITICS, new Subject("政治", R.drawable.subject_politics, SUBJECT_NAME.POLITICS));
        put(SUBJECT_NAME.HISTORY, new Subject("历史", R.drawable.subject_history, SUBJECT_NAME.HISTORY));
        put(SUBJECT_NAME.GEOGRAPHY, new Subject("地理", R.drawable.subject_geography, SUBJECT_NAME.GEOGRAPHY));
        put(SUBJECT_NAME.FOLD, new Subject("收起", R.drawable.subject_fold, SUBJECT_NAME.FOLD));
        put(SUBJECT_NAME.UNFOLD, new Subject("展开", R.drawable.subject_unfold, SUBJECT_NAME.UNFOLD));
    }};

    public final static EnumMap<SUBJECT_NAME, Subject> manageSubjectMap = new EnumMap<SUBJECT_NAME, Subject>(SUBJECT_NAME.class){{
        put(SUBJECT_NAME.CHINESE, new Subject("语文", R.drawable.subject_remove, SUBJECT_NAME.CHINESE));
        put(SUBJECT_NAME.MATH, new Subject("数学", R.drawable.subject_remove, SUBJECT_NAME.MATH));
        put(SUBJECT_NAME.ENGLISH, new Subject("英语", R.drawable.subject_remove, SUBJECT_NAME.ENGLISH));
        put(SUBJECT_NAME.PHYSICS, new Subject("物理", R.drawable.subject_remove, SUBJECT_NAME.PHYSICS));
        put(SUBJECT_NAME.CHEMISTRY, new Subject("化学", R.drawable.subject_remove, SUBJECT_NAME.CHEMISTRY));
        put(SUBJECT_NAME.BIOLOGY, new Subject("生物", R.drawable.subject_remove, SUBJECT_NAME.BIOLOGY));
        put(SUBJECT_NAME.POLITICS, new Subject("政治", R.drawable.subject_remove, SUBJECT_NAME.POLITICS));
        put(SUBJECT_NAME.HISTORY, new Subject("历史", R.drawable.subject_remove, SUBJECT_NAME.HISTORY));
        put(SUBJECT_NAME.GEOGRAPHY, new Subject("地理", R.drawable.subject_remove, SUBJECT_NAME.GEOGRAPHY));
    }};

    public final static int[] LINK_INSTANCE_COLOR = {
            Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.DKGRAY
    };

}
