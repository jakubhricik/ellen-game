package sk.tuke.kpi.oop.game.puzzle.lasers;

public enum Order {
    FIRST(1),
    SECOND(2),
    THIRD(3);
    //if you want to use more lasers than three than this enum will have more params

    private final int orderNum;

    Order(int orderNum){
        this.orderNum = orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }
}
