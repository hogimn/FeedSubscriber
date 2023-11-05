package feedsubscriber.collector.jobs;

import feedsubscriber.common.db.endpoint.EndpointService;
import feedsubscriber.common.db.rss.RSSItem;
import feedsubscriber.common.db.rss.RSSService;
import feedsubscriber.common.serialization.RSS;
import feedsubscriber.common.utils.WebUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class CollectorJob implements Job {
    @Autowired
    EndpointService endpointService;

    @Autowired
    RSSService rssService;

    @Autowired
    RestTemplate restTemplate;

    private static final Logger logger =
            LoggerFactory.getLogger(CollectorJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("CollectorJob started");

        List<String> urls = endpointService
                .findAllUrls();

        urls.forEach(url -> {
            logger.info("Processing URL: {}", url);

            RSS rssFeeds = WebUtils.makeGetRequestList(
                    restTemplate,
                    url,
                    RSS.class);

            List<RSSItem> rssItems = rssFeeds
                    .getChannel()
                    .getItem()
                    .stream()
                    .map(RSSItem::new)
                    .toList();

            List<RSSItem> notSavedRssItems = new ArrayList<>();
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
