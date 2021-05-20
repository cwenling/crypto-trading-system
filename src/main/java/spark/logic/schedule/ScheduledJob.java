package spark.logic.schedule;

import com.google.gson.Gson;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import spark.logic.message.EventManager;

public class ScheduledJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String tag = dataMap.getString("tag");
        String eventManagerString = dataMap.getString("eventManager");
        EventManager eM = new Gson().fromJson(eventManagerString, EventManager.class);
        eM.publish(new ScheduledEvent(tag));
    }
}
