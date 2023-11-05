package feedsubscriber.collector;

import feedsubscriber.collector.jobs.CollectorJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpellCheckingInspection"})
@SpringBootApplication(scanBasePackages = {"feedsubscriber.common", "feedsubscriber.collector"})
@EnableScheduling
@EnableMongoRepositories(basePackages = {"feedsubscriber.common", "feedsubscriber.collector"})
public class CollectorApplication {
    @Autowired
    private Scheduler scheduler;

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleCollectorJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder
                .newJob(CollectorJob.class)
                .withIdentity("collectorJob")
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("collectorTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
