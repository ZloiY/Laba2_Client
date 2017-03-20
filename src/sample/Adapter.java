package sample;

import sample.thrift.PatternGroup;

/**
 * Created by ZloiY on 3/20/2017.
 */
public class Adapter {
    public static String fromEnumToStringPatternGroup(PatternGroup patternGroup){
        if (patternGroup != null) {
            switch (patternGroup.getValue()) {
                case 1:
                    return "MV-паттерны";
                case 2:
                    return "Структурные паттерны";
                case 3:
                    return "Порождающие паттерны";
                case 4:
                    return "Поведенчиские паттерны";
                default: return null;
            }
        }else return "Все паттерны";
    }

    public static String fromEnumToStringPatternGroup(int key){
        switch (key) {
            case 1:
               return "MV-паттерны";
            case 2:
               return "Структурные паттерны";
            case 3:
               return "Порождающие паттерны";
            case 4:
               return "Поведенчиские паттерны";
            default: return  "Все паттерны";}
    }
}
