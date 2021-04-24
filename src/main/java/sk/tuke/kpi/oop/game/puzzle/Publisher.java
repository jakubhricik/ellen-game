package sk.tuke.kpi.oop.game.puzzle;


public interface Publisher<A extends Subscriber> {
    void subscribe(A subscriber);
    void unsubscribe(A subscriber);
    void notifySubscribers();
    void mainBusinessLogic();
}
