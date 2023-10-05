package com.mjc.school;

import com.mjc.school.repository.implementation.DataSource;
import com.mjc.school.repository.implementation.FileAuthorRepository;
import com.mjc.school.repository.implementation.FileDataSource;
import com.mjc.school.repository.implementation.FileNewsRepository;
import com.mjc.school.сustomExceptions.InputValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final NewsService newsService;
    private final Scanner scanner;

    public Main() {
        this.newsService = new NewsService(new FileNewsRepository(new FileDataSource()),
                new FileAuthorRepository(new FileDataSource()));
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        int choice = -1;
        while (choice != 0) {
            System.out.println(
                    "Enter the number of operation:\n" +
                            "1 - Get all news.\n" +
                            "2 - Get news by id.\n" +
                            "3 - Create news.\n" +
                            "4 - Update news.\n" +
                            "5 - Remove news by id.\n" +
                            "0 - Exit.");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        getAllNews();
                        break;
                    case 2:
                        getNewsById();
                        break;
                    case 3:
                        createNews();
                        break;
                    case 4:
                        updateNews();
                        break;
                    case 5:
                        removeNewsById();
                        break;
                    case 0:
                        exit();
                        break;
                    default:
                        System.out.println("Command not found.");
                }
            } else {
                System.out.println("Command not found.");
                scanner.nextLine();
            }
        }
    }

    private void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    private void removeNewsById() {

        System.out.println("Operation: Remove news by id.");
        System.out.println("Enter news id:");

        if (scanner.hasNextLong()) {
            Long newsId = scanner.nextLong();
            scanner.nextLine();
            try {
                NewsDTO news = new NewsDTO();
                news.setId(newsId);
                Boolean removed = newsService.deleteNewsById(news);
                if (removed) {
                    System.out.println(removed);
                } else {
                    throw new InputValidationException(
                            Constants.ERROR_CODE_PREFIX +
                                    Constants.ERROR_NULL_NEWS_OBJECT +
                                    Constants.ERROR_MESSAGE_PREFIX +
                            "Failed to remove news.");
                }
            } catch (InputValidationException | IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_INVALID_CONTENT +
                            Constants.ERROR_MESSAGE_PREFIX +
                            "News Id should be number.");
            scanner.nextLine();
        }
    }

    private void updateNews() {
        String title = null;
        String content = null;
        long authorId = 0;

        boolean validInput = false;
        System.out.println("Operation: Update news.");
        System.out.println("Enter news id:");
        if (scanner.hasNextLong()) {
            Long newsId = scanner.nextLong();
            scanner.nextLine();

            while (!validInput) {
                System.out.println("Enter news title:");
                title = scanner.nextLine();

                System.out.println("Enter news content:");
                content = scanner.nextLine();

                System.out.println("Enter author id:");
                if (scanner.hasNextLong()) {
                    authorId = scanner.nextLong();
                    validInput = true;
                } else {
                    System.out.println(Constants.ERROR_CODE_PREFIX +
                                    Constants.ERROR_INVALID_CONTENT +
                                    Constants.ERROR_MESSAGE_PREFIX +
                                    "Author Id should be number");
                    scanner.nextLine();
                }
            }
            try {
                NewsDTO news = new NewsDTO();
                news.setId(newsId);
                news.setTitle(title);
                news.setContent(content);
                news.setAuthorId(authorId);

                Long updateNews = newsService.updateNews(news);
                if (updateNews != null) {
                    printNews(newsService.getNewsById(updateNews));
                }
            } catch (InputValidationException e) {
                System.out.println(e.getMessage());
                start();
            }
        } else {
            System.out.println(Constants.ERROR_CODE_PREFIX +
                    Constants.ERROR_INVALID_CONTENT +
                    Constants.ERROR_MESSAGE_PREFIX +
                    "News Id should be number.");
            scanner.nextLine();
        }
    }

    private void createNews() {
        String title = null;
        String content = null;
        long authorId = 0;

        boolean validInput = false;

        while (!validInput) {
            System.out.println("Operation: Create news.");
            System.out.println("Enter news title:");
            title = scanner.nextLine();

            System.out.println("Enter news content:");
            content = scanner.nextLine();

            System.out.println("Enter author id:");
            if (scanner.hasNextLong()) {
                authorId = scanner.nextLong();
                validInput = true;
            } else {
                System.out.println(
                        Constants.ERROR_CODE_PREFIX +
                                Constants.ERROR_INVALID_CONTENT +
                                Constants.ERROR_MESSAGE_PREFIX +
                                "Author Id should be number");
                scanner.nextLine();
            }
        }
        try {

            NewsDTO newNews = new NewsDTO();
            newNews.setTitle(title);
            newNews.setContent(content);
            newNews.setAuthorId(authorId);

            Long createdNewsId = newsService.createNews(newNews);
            if (createdNewsId != null) {
                printNews(newsService.getNewsById(createdNewsId));
            }
        } catch (InputValidationException e) {
            System.out.println(e.getMessage());
            start();
        }
    }


    private void getNewsById() {
        System.out.println("Operation: Get news by id.");
        System.out.println("Enter news id:");

        if (scanner.hasNextLong()) {
            Long newsId = scanner.nextLong();
            scanner.nextLine();
            try {
                NewsDTO news = newsService.getNewsById(newsId);
                printNews(news);
            } catch (InputValidationException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        } else {
            System.out.println(
                    Constants.ERROR_CODE_PREFIX +
                    Constants.ERROR_INVALID_CONTENT +
                    Constants.ERROR_MESSAGE_PREFIX +
                    "News Id should be number.");
            scanner.nextLine();
        }
    }

    private void getAllNews() {
        List<NewsDTO> newsList = newsService.getAllNews();
        if (newsList.isEmpty()) {
            System.out.println("No news");
        } else {
            for (NewsDTO news : newsList) {
                printNews(news);
            }
        }
    }

    public static void printNews(NewsDTO news) {
        System.out.print("NewsDtoResponse [");
        System.out.print("id=" + news.getId());
        System.out.print(", title=" + news.getTitle());
        System.out.print(", content=" + news.getContent());
        System.out.print(", createDate=" + news.getCreateDate());
        System.out.print(", lastUpdateDate=" + news.getLastUpdateDate());
        System.out.println(", authorId=" + news.getAuthorId() + "]");
    }
}