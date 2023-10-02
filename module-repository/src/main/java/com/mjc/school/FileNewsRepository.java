package com.mjc.school;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileNewsRepository implements NewsRepository {
    private static final String newsFile = "module-repository/src/main/resources/content.txt";

    @Override
    public List<News> getAllNews() {
        List<News> allNews = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(newsFile));
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
    public News getNewsById(Long id) {
        List<News> allNews = getAllNews();

        for (News news : allNews) {
            if (news.getId() == id) {
                return news;
            }
        }
        return null;
    }

    @Override
    public void saveNews(News news) {
        String newsString = createNewsSrting(news);
        try {
            Files.write(Path.of(newsFile), newsString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Failed to save news: " + e.getMessage());
        }
    }

    private String createNewsSrting(News news) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static void writeNews(List<News> news) {

    }
}
