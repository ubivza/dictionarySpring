package ru.aleksandr.dictionaryspring.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleksandr.dictionaryspring.repositories.SpanishRuRepositoryImpl;

@Component
public class SpanishRuView {
    private final SpanishRuRepositoryImpl spanishRuRepository;

    @Autowired
    public SpanishRuView(SpanishRuRepositoryImpl spanishRuRepository) {
        this.spanishRuRepository = spanishRuRepository;
    }

    public void show() {
        spanishRuRepository.getAll();
    }

    public void showByWord(String word) {
        System.out.println(spanishRuRepository.getByKey(word) + "\n");
    }

    public void deleteByWord(String wordTodelete) {
        spanishRuRepository.deleteByKey(wordTodelete);
        System.out.println("Слово " + wordTodelete + " успешно удалено!" + "\n");
    }

    public void addWord(String wordToAdd) {
        spanishRuRepository.save(wordToAdd);
        System.out.println("Слово " + wordToAdd + " успешно добавлено!" + "\n");
    }
}
