package ru.aleksandr.dictionaryspring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;
import ru.aleksandr.dictionaryspring.models.SpanishRuDictionaryWord;
import ru.aleksandr.dictionaryspring.utils.SpanishRuDictValidator;

import java.io.*;
import java.util.List;
import java.util.Properties;

@Repository
@Slf4j
public class SpanishRuRepositoryImpl implements SpanishRuRepository{
    private final Properties prop;
    private final String FILE_NAME = "src/main/resources/static/dictionary2.properties";
    private static SpanishRuDictValidator spanishRuDictValidator;

    @Autowired
    public SpanishRuRepositoryImpl(SpanishRuDictValidator spanishRuDictValidator) {
        SpanishRuRepositoryImpl.spanishRuDictValidator = spanishRuDictValidator;
        this.prop = new Properties();

        try(InputStream in = new FileInputStream(FILE_NAME)) {
            prop.load(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file");
            //чтобы программа не вылетала с ошибкой можно сделать просто логгирование +
            //сообщением пользователю что возникла проблема
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong");
        }
    }


    public List<String> getAll() {
        PrintWriter ps = new PrintWriter(System.out);
        prop.list(ps);
        ps.flush();
        return null;
    }

    public String getByKey(String s) {
        return prop.getProperty(s, "Key not found, try again");
    }

    public boolean save(String s) {
        String[] valueToSave = s.trim().split(" - ", 2);

        SpanishRuDictionaryWord word = new SpanishRuDictionaryWord();
        word.setSpanishWord(valueToSave[0]);
        word.setRuWord(valueToSave[1]);

        validate(word);

        prop.setProperty(word.getSpanishWord(), word.getRuWord());
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to save");
        }
        return true;
    }

    public boolean update(String s) {
        //в задании нет указаний, сделаю позже
        return false;
    }

    public boolean deleteByKey(String s) {
        prop.remove(s);
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to delete");
        }
        return true;
    }

    private static void validate(SpanishRuDictionaryWord spanishRuDictionaryWord) {
        DataBinder dataBinder = new DataBinder(spanishRuDictionaryWord);
        dataBinder.addValidators(spanishRuDictValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            System.out.println(dataBinder.getBindingResult().getAllErrors());
            log.warn("{} is not valid data to add", spanishRuDictionaryWord.getSpanishWord());
            //throw new RuntimeException("Validation Error"); - заменил на логгер
        }
    }

}
