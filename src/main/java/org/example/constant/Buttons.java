package org.example.constant;

import java.util.List;

public class Buttons {

    public List<List<String>> YesNoButtons(){
        List<String> row = List.of("Yes", "No");
        List<List<String>> buttons = List.of(row);
        return buttons;
    }

    public List<List<String>> ChangeButtons(){
        List<String> row1 = List.of("brand",   "model");
        List<String> row2 = List.of("startYear", "endYear");
        List<String> row3 = List.of("startPrice","endPrice");
        List<List<String>> buttons = List.of(row1, row2, row3);
        return buttons;
    }
}
