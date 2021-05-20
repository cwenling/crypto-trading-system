package spark.logic.schedule;

public class ScheduledEvent {

    private final String tag;

    public ScheduledEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
