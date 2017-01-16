package com.example.q.pacemaker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by q on 2017-01-14.
 */

public class RoutineAdapter extends FragmentStatePagerAdapter {
    private RoutineFragment mondayFragment;
    private RoutineFragment tuesdayFragment;
    private RoutineFragment wednesdayFragment;
    private RoutineFragment thursdayFragment;
    private RoutineFragment fridayFragment;
    private RoutineFragment saturdayFragment;
    private RoutineFragment sundayFragment;

    int from;


    public RoutineAdapter(FragmentManager fm, ArrayList<TodoListData> mon, ArrayList<TodoListData> tue, ArrayList<TodoListData> wed, ArrayList<TodoListData> thur, ArrayList<TodoListData> fri, ArrayList<TodoListData> sat,ArrayList<TodoListData> sun, int from){
        super(fm);
        mondayFragment = new RoutineFragment();
        mondayFragment.setTodoList(mon);
        mondayFragment.setDay(0);
        mondayFragment.setFrom(from);
        tuesdayFragment = new RoutineFragment();
        tuesdayFragment.setTodoList(tue);
        tuesdayFragment.setDay(1);
        tuesdayFragment.setFrom(from);
        wednesdayFragment = new RoutineFragment();
        wednesdayFragment.setTodoList(wed);
        wednesdayFragment.setDay(2);
        wednesdayFragment.setFrom(from);
        thursdayFragment = new RoutineFragment();
        thursdayFragment.setTodoList(thur);
        thursdayFragment.setDay(3);
        thursdayFragment.setFrom(from);
        fridayFragment = new RoutineFragment();
        fridayFragment.setTodoList(fri);
        fridayFragment.setDay(4);
        fridayFragment.setFrom(from);
        saturdayFragment = new RoutineFragment();
        saturdayFragment.setTodoList(sat);
        saturdayFragment.setDay(5);
        saturdayFragment.setFrom(from);
        sundayFragment = new RoutineFragment();
        sundayFragment.setTodoList(sun);
        sundayFragment.setDay(6);
        sundayFragment.setFrom(from);
    }


    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return mondayFragment;
            case 1 :
                return tuesdayFragment;
            case 2 :
                return wednesdayFragment;
            case 3 :
                return thursdayFragment;
            case 4 :
                return fridayFragment;
            case 5 :
                return saturdayFragment;
            case 6 :
                return sundayFragment;
            default :
                return mondayFragment;
        }
    }

    @Override
    public int getCount() { return 7; }
}
