package com.example.demo.Services;

import com.example.demo.Entities.ToDoListEntity;
import com.example.demo.Models.ToDoList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoListService {
    private final List<ToDoList> lists = new ArrayList<>();

    public long createList(ToDoList toDoListDto) {
        ToDoListEntity toDoListEntity = new ToDoListEntity();
        toDoListEntity.setName(toDoListDto.getName());
        lists.add(toDoListDto);
        return toDoListEntity.getId();
    }

    public List<ToDoList> getLists(){
        return lists;
    }
    public ToDoList getListById(Long id){
        ToDoList toDoList = new ToDoList();
        for (int i=0; i<lists.size(); i++){
            if (lists.get(i).getId()==id){
                toDoList.setId(lists.get(i).getId());
                toDoList.setName(lists.get(i).getName());
            }
        }
        return toDoList;
    }
}
