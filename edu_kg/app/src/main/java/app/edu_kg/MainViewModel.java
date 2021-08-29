package app.edu_kg;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.utils.Constant;
import app.edu_kg.utils.adapter.*;

public class MainViewModel extends ViewModel {
    public MessageListAdapter helperMessageAdapter;
    public SubjectGridAdapter helperSubjectAdapter;
    public String username;
    public String token;
    public List<SubjectGridAdapter.Subject> helperSubjectList;
    public int subjectSelected;

    public MainViewModel(){
        helperMessageAdapter = new MessageListAdapter();
        helperMessageAdapter.addRobotMessage("选择上方的学科，可以让我可以更有针对性地回答哦。");
        username = "未登录";
        token = "";
        subjectSelected = 100;
        helperSubjectList = new ArrayList<SubjectGridAdapter.Subject>();
        helperSubjectList.add(Constant.subjectMap.get(Constant.SUBJECT_NAME.CHINESE));
        helperSubjectList.add(Constant.subjectMap.get(Constant.SUBJECT_NAME.MATH));
        helperSubjectList.add(Constant.subjectMap.get(Constant.SUBJECT_NAME.ENGLISH));
        helperSubjectList.add(Constant.subjectMap.get(Constant.SUBJECT_NAME.PHYSICS));
        helperSubjectList.add(Constant.subjectMap.get(Constant.SUBJECT_NAME.UNFOLD));
    }
}
