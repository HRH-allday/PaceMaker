package com.example.q.pacemaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

;

/**
 * Created by q on 2017-01-14.
 */

public class RoutineFragment extends Fragment {

    private RecyclerView backgroundList;
    private RecyclerView.Adapter adapter;
    ArrayList<TodoListData> todoListDatas;


    public RoutineFragment(){

    }

    public void setTodoList(ArrayList<TodoListData> todoListDatas){
        this.todoListDatas = todoListDatas;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_fragment, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        backgroundList = (RecyclerView) view.findViewById(R.id.routine_recyclerview);
        backgroundList.setLayoutManager(mLayoutManager);

        adapter = new TodoListAdapter(todoListDatas);

        backgroundList.setAdapter(adapter);

        return view;
    }
}
