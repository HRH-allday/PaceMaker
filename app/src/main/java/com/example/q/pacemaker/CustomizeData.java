package com.example.q.pacemaker;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class CustomizeData {

    public int cardtype;
    public ArrayList<TodoListData> todoListDatas;


    public CustomizeData(int cardtype, ArrayList<TodoListData> todoListDatas){
        this.cardtype = cardtype;
        this.todoListDatas = todoListDatas;
    }
}
