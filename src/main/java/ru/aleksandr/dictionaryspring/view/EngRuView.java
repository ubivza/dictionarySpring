package ru.aleksandr.dictionaryspring.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleksandr.dictionaryspring.repositories.EngRuRepositoryImpl;

@Component
public class EngRuView {
    private final EngRuRepositoryImpl engRuRepository;

    @Autowired
    public EngRuView(EngRuRepositoryImpl engRuRepository) {
        this.engRuRepository = engRuRepository;
    }

    public void show() {
        engRuRepository.getAll();
        System.out.println();
    }

    public void showByWord(String word) {
        System.out.println(engRuRepository.getByKey(word) + "\n");
    }

    public void deleteByWord(String wordTodelete) {
        if (engRuRepository.deleteByKey(wordTodelete)) {
            System.out.println("Слово " + wordTodelete + " успешно удалено!" + "\n");
        } else {
            System.out.println("Слово " + wordTodelete + " не было добавлено" + "\n");
        }
    }

    public void addWord(String wordToAdd) {
        if (engRuRepository.save(wordToAdd)) {
            System.out.println("Слово " + wordToAdd + " успешно добавлено!" + "\n");
        } else {
            System.out.println("Слово " + wordToAdd + " не было добавлено!" + "\n");
        }
    }
}
