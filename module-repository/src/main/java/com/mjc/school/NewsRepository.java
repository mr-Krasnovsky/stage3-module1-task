package com.mjc.school;

import java.io.IOException;

import java.util.List;

public interface NewsRepository {
    List<News> getAllNews() throws IOException;
}
