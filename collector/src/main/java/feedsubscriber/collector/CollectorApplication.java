package feedsubscriber.collector;

import feedsubscriber.collector.jobs.CollectorJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Spring Boot application for the collector module.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpellCheckingInspection"})
@SpringBootApplication(scanBasePackages = {"feedsubscriber.common", "feedsubscriber.database",
    "feedsubscriber.collector"})
@EnableScheduling
@EnableMongoRepositories(basePackages = {"feedsubscriber.database"})
public class CollectorApplication {
  @Autowired
  private Scheduler scheduler;

  public static void main(String[] args) {
    SpringApplication.run(CollectorApplication.class, args);
  }

  /**
   * Schedules the collector job to run periodically.
   *
   * @throws SchedulerException If there is an issue with the scheduler.
   */
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
