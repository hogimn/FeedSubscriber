package feedsubscriber.collector.jobs;

import feedsubscriber.common.serialization.Rss;
import feedsubscriber.common.utils.WebUtils;
import feedsubscriber.database.endpoint.Endpoint;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RssItem;
import feedsubscriber.database.rss.RssService;
import java.util.ArrayList;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Quartz Job for collecting RSS feed items from configured endpoints.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class CollectorJob implements Job {
  @Autowired
  EndpointService endpointService;

  @Autowired
  RssService rssService;

  @Autowired
  RestTemplate restTemplate;

  private static final Logger logger =
          LoggerFactory.getLogger(CollectorJob.class);

  @Override
  public void execute(JobExecutionContext context) {
    logger.info("CollectorJob started");

    List<Endpoint> endpoints = endpointService
            .findAll();

    endpoints.forEach(endpoint -> {
      logger.info("Processing URL: {}", endpoint.getUrl());

      Rss rssFeeds = WebUtils.makeGetRequestList(
              restTemplate,
              endpoint.getUrl(),
              Rss.class);

      List<RssItem> rssItems = rssFeeds
              .getChannel()
              .getItem()
              .stream()
              .map(item -> {
                RssItem rssItem = new RssItem(item, endpoint.getUsername());
                rssItem.setEndpoint(endpoint);
                return rssItem;
              })
              .toList();

      List<RssItem> notSavedRssItems = new ArrayList<>();
      rssItems.forEach(rss -> {
        if (rssService.findByLink(rss.getLink()) == null) {
          notSavedRssItems.add(rss);
        }
      });

      rssService.saveAll(notSavedRssItems);
    });

    logger.info("CollectorJob finished");
  }
}
