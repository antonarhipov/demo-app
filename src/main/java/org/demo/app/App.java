package org.demo.app;

import java.util.ArrayList;
import java.util.List;

public class App {
    //Lib lib = new Lib()

    List<Item> items = new ArrayList<>();

    //main app method
    public void run(){
        //lib.foo;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public int itemsCount() {
        return items.size();
    }
}
