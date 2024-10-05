package com.example.dmr3a_novelas.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NovelDatabase {

    private static NovelDatabase instance;
    private List<Novel> novels;
    private List<Review> reviews;

    private NovelDatabase() {
        novels = new ArrayList<>();
        reviews = new ArrayList<>();
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

    public List<Novel> getFavoriteNovels() {
        return novels.stream()
                .filter(novel -> novel.getIsFavorite())
                .collect(Collectors.toList());
    }

    public void toggleFavorite(Novel novel) {
        novel.setFavorite(!novel.getIsFavorite()); // Especificar la clase Novel
    }


    public void addReview(Novel novel, String reviewText , String usuario) {
        reviews.add(new Review(novels.indexOf(novel) + 1, reviewText, usuario)); // Usar el índice de la novela como ID
    }

    public List<Review> getReviewsForNovel(Novel novel) { // Cambiar el tipo de retorno a List<Review>
        int novelId = novels.indexOf(novel) + 1; // Obtener el ID de la novela
        return reviews.stream()
                .filter(review -> review.getNovelId() == novelId)
                .collect(Collectors.toList()); // Devolver la lista de objetos Review
    }
}
