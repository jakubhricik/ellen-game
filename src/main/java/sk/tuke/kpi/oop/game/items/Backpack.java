package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.*;

public class Backpack implements ActorContainer<Collectible> {

    private final String name;
    private final int capacity;
    private final List<Collectible> list;

    public Backpack(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
        list = new ArrayList<>();
    }


    @Override
    public void add(@NotNull Collectible actor){

        if(getSize() < capacity){
            list.add(actor);
        }else{
            throw new IllegalStateException(getName() + "is full");
        }

    }

    @Override
    public void remove(@NotNull Collectible actor) {
        if(getSize() != 0) {
            list.remove(actor);
        }
    }

    @Override
    public @Nullable Collectible peek() {
        try{
            return getContent().get(getContent().size() - 1);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void shift() {
        if(getSize() > 1){
            Collectible lastItem = peek();
            if (lastItem != null){

                for (int i = getSize() - 1 ; i > 0; i--){
                    list.set(i, list.get(i-1));
                }
                list.set(0, lastItem);
            }
        }
    }

    @NotNull
    @Override
    public Iterator<Collectible> iterator() {
        return getContent().listIterator();
    }

    //getters:

    @Override
    public @NotNull List<Collectible> getContent() {
        return new ArrayList<>(list);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }
}
