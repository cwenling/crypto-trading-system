package spark.logic.schedule;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import spark.logic.message.EventManager;

public class ScheduledJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        EventManager eM = (EventManager) dataMap.get("eventManager");
        String tag = dataMap.getString("tag");
    }
}
