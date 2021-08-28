package app.edu_kg.utils;

public class Functional {
    public static String subjChe2Eng(String ch) {
        switch(ch) {
            case "语文":
                return "chinese";
            case "英文":
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
}
