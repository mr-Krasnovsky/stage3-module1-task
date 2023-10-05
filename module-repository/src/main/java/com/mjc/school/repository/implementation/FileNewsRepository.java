package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.News;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileNewsRepository implements NewsRepository {
    private final DataSource dataSource;

    public FileNewsRepository(DataSource dataSource) {

        this.dataSource = dataSource;
    }


    @Override
    public List<News> getAllNews() {
        List<News> allNews = new ArrayList<>();
        try {
            List<String> lines = dataSource.readAllLines();
            for (String str : lines) {
                if (!str.equals("")) {
                    allNews.add(createNews(str));
                }
            }
        } catch (IOException e) {
            System.out.println("The news file is corrupted or not found");
        }

        return allNews;
    }

    @Override
    public News readById(Long id) {
        List<News> allNews = getAllNews();

        for (News news : allNews) {
            if (news.getId().equals(id)) {
                return news;
            }
        }
        return null;
    }

    @Override
    public News createNews(News news) {
        String newsString = makeNewsString(news);
        try {
            dataSource.createLines(newsString);
            return news;
        } catch (IOException e) {
            System.out.println("Failed to save news: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean deleteNewsById(News removeNews) {
        try {
            List<String> lines = dataSource.readAllLines();
            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();
                if (!line.equals("")) {
                    News news = createNews(line);
                    if (news.getId().equals(removeNews.getId())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            dataSource.writeAfterDelete(lines);
            return true;
        } catch (IOException e) {
            System.out.println("Filed to remove news" + e.getMessage());
            return false;
        }
    }

    @Override
    public News updateNews(News existingNews) {
        List<News> allNews = getAllNews();

        for (int i = 0; i < allNews.size(); i++) {
            if (allNews.get(i).getId().equals(existingNews.getId())) {
                allNews.set(i, existingNews);
                writeNewsToFile(allNews);
                return existingNews;
            }
        }
        return null;
}

    private void writeNewsToFile (List<News> allNews) {
        List<String> newsStrings = new ArrayList<>();
        for (News news: allNews){
            newsStrings.add(makeNewsString(news));
        }
        try {
            dataSource.writeNewsToFile(newsStrings);
        } catch (IOException e) {
            System.out.println("Failed to save news: " + e.getMessage());
        }
    }

    private String makeNewsString(News news) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return String.format("\n%d;%s;%s;%s;%s;%d",
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getCreateDate().format(formatter),
                news.getLastUpdateDate().format(formatter),
                news.getAuthorId());
    }

    public News createNews(String str) {
        News news = null;
        String[] newsStr = str.split(";");
        if (newsStr.length == 6) {
            news = new News(Long.parseLong(newsStr[0]),
                    newsStr[1],
                    newsStr[2],
                    timeFormatter(newsStr[3]),
                    timeFormatter(newsStr[4]),
                    Long.parseLong(newsStr[5]));
        }
        return news;
    }

    public static LocalDateTime timeFormatter(String date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

}
