package ru.aleksandr.dictionaryspring.repositories;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;
import ru.aleksandr.dictionaryspring.utils.EngRuDictValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class EngRuRepositoryImpl implements EngRuRepository {
    private final Properties prop;
    private final String FILE_NAME = "src/main/resources/static/dictionary1.properties";
    private InputStream in;
    private final EngRuDictValidator engRuDictValidator;

    @Autowired
    public EngRuRepositoryImpl(EngRuDictValidator engRuDictValidator) {
        this.engRuDictValidator = engRuDictValidator;
        this.prop = new Properties();
        try {
            in = new FileInputStream(FILE_NAME);
            prop.load(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file");
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong");
        }
    }


    public List<String> getAll() {
        List<String> words = new ArrayList<>();
        PrintWriter ps = new PrintWriter(System.out);
        prop.list(ps);
        ps.flush();
        return null;
    }

    public String getByKey(String s) {
        return prop.getProperty(s, "Key not found, try again");
    }

    public void save(String s) {
        String[] valueToSave = s.trim().split(" - ", 2);
        EngRuDictWord word = new EngRuDictWord();
        //какой объект для помещения в него сообщений об ошибке поместить?
        engRuDictValidator.validate(word, null);
        word.setEnglishWord(valueToSave[0]);
        word.setRuWord(valueToSave[1]);
        prop.setProperty(word.getEnglishWord(), word.getRuWord());
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to save");
        }
    }

    public void update(String s) {
        //сделаю позже
    }

    public void deleteByKey(String s) {
        prop.remove(s);
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to delete");
        }
    }

}
