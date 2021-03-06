package com.example.q.pacemaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.q.pacemaker.Utilities.SendJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    String cid;


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

    public void setCid(String id){
        this.cid = id;
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
                    try {
                        JSONObject req = new JSONObject();
                        req.put("cid", cid);
                        req.put("new_routine", todo);
                        req.put("day",day);

                        JSONObject res = new SendJSON(App.server_url + App.routing_add_routine, req.toString(), App.JSONcontentsType).execute().get();
                        if (res != null && res.has("result") && res.getString("result").equals("success")) {

                        }
                    }catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

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


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                try {
                    if(from == 1) return;
                    JSONObject req = new JSONObject();
                    req.put("cid", cid);
                    req.put("index", viewHolder.getAdapterPosition());
                    req.put("day", day);

                    JSONObject res = new SendJSON(App.server_url + App.routing_get_remove_clone_routine, req.toString(), App.JSONcontentsType).execute().get();
                    if (res != null && res.has("result") && res.getString("result").equals("success")) {

                    }
                }catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        backgroundList = (RecyclerView) view.findViewById(R.id.routine_recyclerview);
        backgroundList.setLayoutManager(mLayoutManager);

        //if(from != 2) itemTouchHelper.attachToRecyclerView(backgroundList);

        if(from == 2){
            routineEdit.setVisibility(View.GONE);
            routineButton.setVisibility(View.GONE);
        }

        adapter = new TodoListAdapter(todoListDatas);

        backgroundList.setAdapter(adapter);

        return view;
    }
}
