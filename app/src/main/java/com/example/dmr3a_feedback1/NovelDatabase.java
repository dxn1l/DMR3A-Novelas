package com.example.dmr3a_feedback1;

import java.util.ArrayList;
import java.util.List;

public class NovelDatabase {

    private static NovelDatabase instance;
    private List<Novel> novels;

    private NovelDatabase() {
        novels = new ArrayList<>();
    }

    public static NovelDatabase getInstance() {
        if (instance == null) {
            instance = new NovelDatabase();
        }
        return instance;
    }

    public List<Novel> getAllNovels() {
        return novels;
    }

    public void addNovel(Novel novel) {
        novels.add(novel);
    }

    public void removeNovel(Novel novel) {
        novels.remove(novel);
    }
}
