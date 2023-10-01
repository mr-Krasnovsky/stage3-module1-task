package com.mjc.school;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final NewsService newsService;
    private final Scanner scanner;

    public Main() {
        this.newsService = new NewsService(new FileNewsRepository());
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.start();
    }

    public void start() throws IOException {
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
                    deleteNews();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Command not found.");
            }
        }
    }

    private void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    private void deleteNews() {
    }

    private void updateNews() {
    }

    private void createNews() {
    }

    private void getNewsById() {
        System.out.println("Operation: Get news by id.");
        System.out.println("Enter news id:");
        int newsId = scanner.nextInt();
        scanner.nextLine();
        NewsDTO news = newsService.getNewsById(newsId);
        if (news == null) {
            System.out.println("ERROR_CODE: 000001 ERROR_MESSAGE: News with id " + newsId + " does not exist.");
        } else {
            printNews(news);
        }

    }

    private void getAllNews() throws IOException {
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