package ru.aleksandr.dictionaryspring.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleksandr.dictionaryspring.repositories.SpanishRuRepositoryImpl;

import java.util.Map;

@Component
public class SpanishRuView {
    private final SpanishRuRepositoryImpl spanishRuRepository;

    @Autowired
    public SpanishRuView(SpanishRuRepositoryImpl spanishRuRepository) {
        this.spanishRuRepository = spanishRuRepository;
    }

    public void show() {
        for (Map.Entry<String, String> map : spanishRuRepository.getAll().entrySet()) {
            System.out.println(map.getKey() + " - " + map.getValue());
        }
        System.out.println();
    }

    public void showByWord(String word) {
        System.out.println(word + " - " + spanishRuRepository.getByKey(word) + "\n");
    }

    public void deleteByWord(String wordTodelete) {
        if (spanishRuRepository.deleteByKey(wordTodelete)) {
            System.out.println("Слово " + wordTodelete + " успешно удалено!" + "\n");
        } else {
            System.out.println("Слово " + wordTodelete + " не было удалено" + "\n");
        }
    }

    public void addWord(String wordToAdd) {
        if (spanishRuRepository.save(wordToAdd)) {
            System.out.println("Слово " + wordToAdd + " успешно добавлено!" + "\n");
        } else {
            System.out.println("Слово " + wordToAdd + " не было добавлено!" + "\n");
        }
    }

    public void updateWord(String wordToUpdate) {
        if (spanishRuRepository.update(wordToUpdate)) {
            System.out.println("Слово " + wordToUpdate + " успешно обновлено!" + "\n");
        } else {
            System.out.println("Слово " + wordToUpdate + " не было обновлено!" + "\n");
        }
    }

    public void exitService() {
        spanishRuRepository.saveCacheToMemory();
    }
}
