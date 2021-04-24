package sk.tuke.kpi.oop.game.characters;

import java.util.HashSet;
import java.util.Set;

public class Health{

    @FunctionalInterface
    public interface ExhaustionEffect {
        void apply();
    }


    private int currentHealth;
    private final int maxHealth;
    private boolean wasExhausted;
    private final Set<ExhaustionEffect> list = new HashSet<>();

    public Health(int startingHealth, int maxHealth){
        this.currentHealth = startingHealth;
        this.maxHealth = maxHealth;
        wasExhausted = false;
    }

    public Health(int startingAndMaxHealth){
        this.currentHealth = startingAndMaxHealth;
        this.maxHealth = startingAndMaxHealth;
        wasExhausted = false;
    }

    public void onExhaustion(ExhaustionEffect effect){
        list.add(effect);
    }

    public void refill(int amount){
        if(amount > 0){
            this.currentHealth += amount;
            if(this.currentHealth > this.maxHealth){
                this.currentHealth = this.maxHealth;
            }
        }
    }

    public void restore(){
        this.currentHealth = this.maxHealth;
    }

    public void drain(int amount){
        if(amount > 0){
            this.currentHealth -= amount;
            if(this.currentHealth <= 0){
                this.currentHealth = 0;
                this.exhaust();
            }
        }
    }

    public void exhaust(){
        if(!wasExhausted){
            wasExhausted = true;
            this.currentHealth = 0;
            list.forEach(ExhaustionEffect::apply);
        }
    }

    public int getValue(){
        return this.currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
