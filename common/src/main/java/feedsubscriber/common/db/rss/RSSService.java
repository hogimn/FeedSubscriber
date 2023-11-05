package feedsubscriber.common.db.rss;

import feedsubscriber.common.dto.RSSItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "UnusedReturnValue"})
@Service
public class RSSService {
    @Autowired
    RSSRepository rssRepository;

    public List<RSSItemDTO> findAllAndSortByPubDateDesc() {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        List<RSSItemDTO> rssList = new ArrayList<>(rssRepository.findAll()
                .stream()
                .map(r -> new RSSItemDTO(
                        r.getTitle(),
                        r.getDescription(),
                        r.getPubDate(),
                        r.getLink(),
                        r.getAuthor()))
                .toList());

        rssList.sort((r1, r2) -> {
            Date d1;
            Date d2;
            try {
                d1 = inputDateFormat.parse(r1.getPubDate());
                d2 = inputDateFormat.parse(r2.getPubDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return d2.compareTo(d1);
        });

        return rssList;
    }

    public RSSItem findByLink(String link) {
        return rssRepository.findByLink(link);
    }

    public void saveAll(List<RSSItem> rss) {
        rssRepository.saveAll(rss);
    }
}
