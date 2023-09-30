package com.mjc.school;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileNewsRepository implements NewsRepository{
    private static final String news = "module-repository/src/main/resources/content.txt";

    @Override
    public List<News> getAllNews() throws IOException {
        List<News> allNews = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(news));
            for (String str: lines){
                allNews.add(createNews(str));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return allNews;
    }

    public News createNews(String str){
        News news = null;
        String[] newsStr = str.split(";");
        if(newsStr.length == 6){
            news = new News(Long.parseLong(newsStr[0]),
                    newsStr[1],
                    newsStr[2],
                    timeFormatter(newsStr[3]),
                    timeFormatter(newsStr[4]),
                    Long.parseLong(newsStr[5]));
        }
        return news;
    }

    public static LocalDateTime timeFormatter(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
