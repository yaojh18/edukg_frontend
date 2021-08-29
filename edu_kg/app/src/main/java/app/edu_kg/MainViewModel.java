package app.edu_kg;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import app.edu_kg.utils.Constant;
import app.edu_kg.utils.adapter.*;

public class MainViewModel extends ViewModel {
    public MessageListAdapter helperMessageAdapter;
    public SubjectGridAdapter helperSubjectAdapter;
    public List<SubjectGridAdapter.Subject> helperSubjectList;

    public ItemListAdapter homeListAdapter;
    public List<ItemListAdapter.ItemMessage> homeList;
    public SubjectGridAdapter homeSubjectAdapter;
    public List<SubjectGridAdapter.Subject> homeSubjectList;
    public EnumSet<Constant.SUBJECT_NAME> presentSet;
    public List<SubjectGridAdapter.Subject> presentSubjectList;
    public SubjectGridAdapter presentSubjectAdapter;

    public String username;
    public String token;
    public int helperSubjectSelected;
    public int homeSubjectSelected;


    public MainViewModel(){
        helperMessageAdapter = new MessageListAdapter();
        helperMessageAdapter.addRobotMessage("选择上方的学科，可以让我可以更有针对性地回答哦。");

        username = "未登录";
        token = "";

        helperSubjectSelected = 100;
        helperSubjectList = new ArrayList<>();
        helperSubjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.CHINESE));
        helperSubjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.MATH));
        helperSubjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.ENGLISH));
        helperSubjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.PHYSICS));
        helperSubjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.UNFOLD));

        homeList = new ArrayList<>();
        homeSubjectSelected = 0;
        homeSubjectList = new ArrayList<>();
        homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.CHINESE));
        homeSubjectList.get(0).isSelected = true;
        homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.MATH));
        homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.ENGLISH));
        homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.PHYSICS));
        homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.UNFOLD));

        presentSet = EnumSet.allOf(Constant.SUBJECT_NAME.class);
        presentSet.remove(Constant.SUBJECT_NAME.FOLD);
        presentSet.remove(Constant.SUBJECT_NAME.UNFOLD);
        presentSubjectList = new ArrayList<>();
        for (Constant.SUBJECT_NAME name : presentSet)
            presentSubjectList.add(Constant.manageSubjectMap.get(name));

    }
}
