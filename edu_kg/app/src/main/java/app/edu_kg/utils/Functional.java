package app.edu_kg.utils;

public class Functional {
    public static String subjChe2Eng(String ch) {
        switch(ch) {
            case "英语":
                return "english";
            case "数学":
                return "math";
            case "物理":
                return "physics";
            case "化学":
                return "chemistry";
            case "生物":
                return "biology";
            case "地理":
                return "geo";
            case "历史":
                return "history";
            case "政治":
                return "politics";
            default:
                return "chinese";
        }
    }
    public static String subjEng2Che(String ch) {
        switch(ch) {
            case "english":
                return "英语";
            case "math":
                return "数学";
            case "physics":
                return "物理";
            case "chemistry":
                return "化学";
            case "biology":
                return "生物";
            case "geo":
                return "地理";
            case "history":
                return "历史";
            case "politics":
                return "政治";
            default:
                return "语文";
        }
    }
}
