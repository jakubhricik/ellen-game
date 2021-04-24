package sk.tuke.kpi.oop.game.weapons;

public class Gun extends Firearm{

    public Gun(int startingAmmo, int maxAmmo) {
        super(startingAmmo, maxAmmo);
    }

    public Gun(int startingAndMaxAmmo) {
        super(startingAndMaxAmmo);
    }

    @Override
    protected Fireable createBullet() {
        return new Bullet();
    }
}
