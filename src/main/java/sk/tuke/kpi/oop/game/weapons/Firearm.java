package sk.tuke.kpi.oop.game.weapons;

import java.util.ArrayList;
import java.util.List;

public abstract class Firearm {
    private int ammo;
    private final int maxAmmo;
    private List<Fireable> bullets = new ArrayList<>();


    public Firearm(int startingAmmo, int maxAmmo){
        this.ammo = startingAmmo;
        this.maxAmmo = maxAmmo;
    }

    public Firearm(int startingAndMaxAmmo){
        this.ammo = startingAndMaxAmmo;
        this.maxAmmo = startingAndMaxAmmo;
    }


    public void reload(int newAmmo){
        if(newAmmo > 0 && this.ammo < this.maxAmmo){
            this.ammo += newAmmo;
            if(this.ammo >= this.maxAmmo){
                this.ammo = this.maxAmmo;
            }
        }
    }

    public Fireable fire(){
        if(this.getAmmo() > 0){
            this.ammo -= 1;
            Fireable bullet = createBullet();
            bullets.add(bullet);
            return bullet;
        }
        else return null;
    }

    public List<Fireable> getBullets(){
        return new ArrayList<>(bullets);
    }

    protected abstract Fireable createBullet();

    public int getAmmo(){
        return this.ammo;
    }

}
