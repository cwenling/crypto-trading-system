package spark.logic.source;

import com.google.gson.Gson;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import spark.logic.message.EventManager;
import spark.logic.schedule.ScheduledJob;

public class ScheduleManager {

    private final Scheduler scheduler;
    private final EventManager eventManager;

    public ScheduleManager(EventManager eM) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        this.eventManager = eM;
    }

    private Trigger buildTrigger(int intervalInMilliseconds) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(intervalInMilliseconds)
                    .repeatForever())
                .build();
        return trigger;
    }

    private JobDetail buildJob(String tag) {
        JobDetail job = JobBuilder.newJob(ScheduledJob.class).build();
        job.getJobDataMap().put("tag", tag);
        //job.getJobDataMap().put("eventManager", new Gson().toJson(eventManager));
        job.getJobDataMap().put("eventManager", eventManager);
        return job;
    }

    public void periodicCallback(int intervalInMilliseconds, String tag) throws SchedulerException {
        Trigger trigger = buildTrigger(intervalInMilliseconds);
        JobDetail job = buildJob(tag);
        this.scheduler.scheduleJob(job, trigger);
        this.scheduler.start();
    }
}
