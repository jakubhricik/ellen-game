package sk.tuke.kpi.oop.game.items.termilans;

import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.messages.Messenger;
import sk.tuke.kpi.oop.game.messages.Sender;

public class HintTerminal extends Terminal {

    private final String hint;
    private final Sender actor;

    /**
     * ths terminal will send message (hint) to player
     * @param hint message that will be showed on overlay
     */
    public HintTerminal(String hint){
        super();
        this.hint = hint;
        actor = this;
    }

    public HintTerminal(String hint, Sender sender){
        super();
        this.hint = hint;
        this.actor = sender;
    }

    @Override
    public void useWith(Ripley actor) {
        //do nothing only print hint
        sentMessage();
    }

    @Override
    public void sentMessage() {
        Messenger<Sender> messenger = new Messenger<>(hint);
        messenger.scheduleFor(actor);
        messenger.printMessage(1);
    }
}
