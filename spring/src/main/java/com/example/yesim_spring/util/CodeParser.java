package com.example.yesim_spring.util;

import java.util.ArrayList;
import java.util.List;

public class CodeParser {

    static public List<String> Parsing(String code){

        List<String> list = new ArrayList<>();

        // Company
        list.add(code.substring(0, 1));

//        // Category
//        list.add(code.substring(2, 3));

        // Item
        list.add(code.substring(2, 4));

        // Container
        list.add(code.substring(5, code.length() - 1));

        return list;
    }

    static public String MakeCode(String companyCode,
                           String itemCode,
                           String containerCode){

        return companyCode + itemCode + containerCode;
    }
}
