package com.example.q.pacemaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

;

/**
 * Created by q on 2017-01-14.
 */

public class RoutineFragment extends Fragment {

    private EditText routineEdit;
    private Button routineButton;

    private RecyclerView backgroundList;
    private RecyclerView.Adapter adapter;
    ArrayList<TodoListData> todoListDatas;

    int day;
    int from;


    public RoutineFragment(){

    }

    public void setTodoList(ArrayList<TodoListData> todoListDatas){
        this.todoListDatas = todoListDatas;

    }

    public void addRoutine(TodoListData routine){
        Log.i("hi", routine.text);
        ((TodoListAdapter) adapter).addItem(routine, 0);
    }

    public void setDay(int d){
        this.day = d;
    }

    public void setFrom(int f){
        this.from = f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_fragment, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        routineEdit = (EditText) view.findViewById(R.id.routine_edit);
        routineButton = (Button) view.findViewById(R.id.routine_button);

        routineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = routineEdit.getText().toString().trim();
                if(todo.equals("")) return;
                else if(from == 1){
                    routineEdit.setText("");
                    TodoListData rt = new TodoListData(todo, "#ff1616");
                    addRoutine(rt);

                }
                else{
                    Log.i("why",todo);
                    routineEdit.setText("");
                    TodoListData rt = new TodoListData(todo, "#ff1616");
                    addRoutine(rt);
                    switch (day){
                        case 0:
                            GoalRegisterActivity.mMondayList.put(todo);
                            break;
                        case 1:
                            GoalRegisterActivity.mTuesdayList.put(todo);
                            break;
                        case 2:
                            GoalRegisterActivity.mWednesdayList.put(todo);
                            break;
                        case 3:
                            GoalRegisterActivity.mThursdayList.put(todo);
                            break;
                        case 4:
                            GoalRegisterActivity.mFridayList.put(todo);
                            break;
                        case 5:
                            GoalRegisterActivity.mSatdayList.put(todo);
                            break;
                        case 6:
                            GoalRegisterActivity.mSundayList.put(todo);
                            break;
                    }
                }
                return;
            }
        });

        backgroundList = (RecyclerView) view.findViewById(R.id.routine_recyclerview);
        backgroundList.setLayoutManager(mLayoutManager);

        adapter = new TodoListAdapter(todoListDatas);

        backgroundList.setAdapter(adapter);

        return view;
    }
}
