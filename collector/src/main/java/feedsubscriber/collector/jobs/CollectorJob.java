package feedsubscriber.collector.jobs;

import feedsubscriber.common.serialization.Rss;
import feedsubscriber.common.utils.WebUtils;
import feedsubscriber.database.endpoint.Endpoint;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RssItem;
import feedsubscriber.database.rss.RssService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Quartz Job for collecting RSS feed items from configured endpoints.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
@Slf4j
public class CollectorJob implements Job {
  @Autowired
  EndpointService endpointService;

  @Autowired
  RssService rssService;

  @Autowired
  RestTemplate restTemplate;

  @Override
  public void execute(JobExecutionContext context) {
    log.info("CollectorJob started");

    Set<String> urlVisited = new HashSet<>();

    List<Endpoint> endpoints = endpointService
        .findAll();

    endpoints.forEach(endpoint -> {
      log.info("Processing URL: {}", endpoint.getUrl());

      if (urlVisited.contains(endpoint.getUrl())) {
        return;
      }

      Rss rssFeeds;
      try {
        rssFeeds = WebUtils.makeGetRequestList(
            restTemplate,
            endpoint.getUrl(),
            Rss.class);
      } catch (Exception e) {
        log.error(e.toString());
        return;
      }

      List<RssItem> rssItems = rssFeeds
          .getChannel()
          .getItem()
          .stream()
          .map(item -> new RssItem(item, endpoint.getUsername()))
          .toList();

      List<String> usernames = endpointService.findAllSubscribedUsersByUrl(endpoint.getUrl());

      usernames.forEach(username -> {
        Endpoint endpointRelated = endpointService
            .findByUrlAndUsername(endpoint.getUrl(), username);

        List<RssItem> rssItemsToSave = new ArrayList<>();
        rssItems.forEach(rss -> {
          if (rssService.findByLinkAndUsername(rss.getLink(), username) == null) {
            rss.setEndpoint(endpointRelated);
            rss.setUsername(username);
            rssItemsToSave.add(rss);
          }
        });

        rssService.saveAll(rssItemsToSave);
      });

      urlVisited.add(endpoint.getUrl());
    });

    log.info("CollectorJob finished");
  }
}
