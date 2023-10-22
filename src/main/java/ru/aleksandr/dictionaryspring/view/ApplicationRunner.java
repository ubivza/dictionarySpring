package ru.aleksandr.dictionaryspring.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Slf4j
public class ApplicationRunner {
    private static boolean abort = true;
    private EngRuView engRuView;
    private SpanishRuView spanishRuView;

    @Autowired
    public ApplicationRunner(EngRuView engRuView, SpanishRuView spanishRuView) {
        this.engRuView = engRuView;
        this.spanishRuView = spanishRuView;
    }

    public void runApp() {
        log.info("Application starting...");
        Scanner scanner = new Scanner(System.in);
        while (abort) {
            System.out.println("Добро пожаловать в словарьленд! Выберите действие:" + "\n");
            System.out.println("1. Открыть Англо-Русский словарь");
            System.out.println("2. Открыть Испано-Русский словарь");
            System.out.println("3. Найти слово в Англо-Русском словаре");
            System.out.println("4. Найти слово в Испано-Русском словаре");
            System.out.println("5. Удалить слово в Англо-Русском словаре");
            System.out.println("6. Удалить слово в Испано-Русском словаре");
            System.out.println("7. Добавить слово в Англо-Русский словарь");
            System.out.println("8. Добавить слово в Испано-Русский словарь");
            System.out.println("9. Выключить приложение");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    engRuView.show();
                    break;
                case 2:
                    spanishRuView.show();
                    break;
                case 3:
                    System.out.println("Введите слово которое хотите найти:");
                    String wordToFind = scanner.nextLine();
                    engRuView.showByWord(wordToFind);
                    break;
                case 4:
                    System.out.println("Введите слово которое хотите найти:");
                    String wordToFind2 = scanner.nextLine();
                    spanishRuView.showByWord(wordToFind2);
                    break;
                case 5:
                    System.out.println("Введите слово которое хотите удалить:");
                    String wordTodelete = scanner.nextLine();
                    engRuView.deleteByWord(wordTodelete);
                    break;
                case 6:
                    System.out.println("Введите слово которое хотите удалить:");
                    String wordTodelete2 = scanner.nextLine();
                    spanishRuView.deleteByWord(wordTodelete2);
                    break;
                case 7:
                    System.out.println("Введите слово, которое хотите добавить и его перевод через дефис:");
                    System.out.println("Формат : 5 в длину и содержит только цифры");
                    String wordToAdd = scanner.nextLine();
                    engRuView.addWord(wordToAdd);
                    break;
                case 8:
                    System.out.println("Введите слово, которое хотите добавить и его перевод через дефис:");
                    System.out.println("Формат : 4 в длину и содержит только латинские буквы");
                    String wordToAdd2 = scanner.nextLine();
                    spanishRuView.addWord(wordToAdd2);
                    break;
                case 9:
                    log.info("Application aborted...");
                    abort = false;
                    engRuView.exitService();
                    spanishRuView.exitService();
                    break;
                default:
                    System.out.println("Нет такой команды, попробуйте еще раз");
                    break;
            }
        }
        scanner.close();
    }
}
