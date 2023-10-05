package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.NewsModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileNews implements News {
    private final DataSource dataSource;

    public FileNews(DataSource dataSource) {

        this.dataSource = dataSource;
    }


    @Override
    public List<NewsModel> getAllNews() {
        List<NewsModel> allNews = new ArrayList<>();
        try {
            List<String> lines = dataSource.readAllLines();
            for (String str : lines) {
                if (!str.equals("")) {
                    allNews.add(makeNews(str));
                }
            }
        } catch (IOException e) {
            System.out.println("The news file is corrupted or not found");
        }

        return allNews;
    }

    @Override
    public NewsModel readById(Long id) {
        List<NewsModel> allNews = getAllNews();

        for (NewsModel newsModel : allNews) {
            if (newsModel.getId().equals(id)) {
                return newsModel;
            }
        }
        return null;
    }

    @Override
    public NewsModel createNews(NewsModel newsModel) {
        String newsString = makeNewsString(newsModel);
        try {
            dataSource.createLines(newsString);
            return newsModel;
        } catch (IOException e) {
            System.out.println("Failed to save news: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean deleteNewsById(Long newsId) {
        try {
            List<String> lines = dataSource.readAllLines();
            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();
                if (!line.equals("")) {
                    NewsModel newsModel = makeNews(line);
                    if (newsModel.getId().equals(newsId)) {
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
    public NewsModel updateNews(NewsModel existingNewsModel) {
        List<NewsModel> allNews = getAllNews();

        for (int i = 0; i < allNews.size(); i++) {
            if (allNews.get(i).getId().equals(existingNewsModel.getId())) {
                allNews.set(i, existingNewsModel);
                writeNewsToFile(allNews);
                return existingNewsModel;
            }
        }
        return null;
}

    private void writeNewsToFile (List<NewsModel> allNews) {
        List<String> newsStrings = new ArrayList<>();
        for (NewsModel newsModel : allNews){
            newsStrings.add(makeNewsString(newsModel));
        }
        try {
            dataSource.writeNewsToFile(newsStrings);
        } catch (IOException e) {
            System.out.println("Failed to save news: " + e.getMessage());
        }
    }

    private String makeNewsString(NewsModel newsModel) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return String.format("\n%d;%s;%s;%s;%s;%d",
                newsModel.getId(),
                newsModel.getTitle(),
                newsModel.getContent(),
                newsModel.getCreateDate().format(formatter),
                newsModel.getLastUpdateDate().format(formatter),
                newsModel.getAuthorId());
    }

    public NewsModel makeNews(String str) {
        NewsModel newsModel = null;
        String[] newsStr = str.split(";");
        if (newsStr.length == 6) {
            newsModel = new NewsModel(Long.parseLong(newsStr[0]),
                    newsStr[1],
                    newsStr[2],
                    timeFormatter(newsStr[3]),
                    timeFormatter(newsStr[4]),
                    Long.parseLong(newsStr[5]));
        }
        return newsModel;
    }

    public static LocalDateTime timeFormatter(String date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

}
