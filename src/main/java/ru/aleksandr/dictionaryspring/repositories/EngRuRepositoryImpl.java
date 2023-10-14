package ru.aleksandr.dictionaryspring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;
import ru.aleksandr.dictionaryspring.utils.EngRuDictValidator;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Repository
@Slf4j
public class EngRuRepositoryImpl implements EngRuRepository {
    private static boolean isChanged = false;
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
        for (Map.Entry<Object, Object> o : prop.entrySet()) {
            System.out.println(o.getKey() + " - " + o.getValue());
        }
        /*if (isChanged) {
            PrintWriter ps = new PrintWriter(System.out);
            prop.list(ps);
            ps.flush();
        } else {
            for (Map.Entry<Object, Object> o : prop.entrySet()) {
                System.out.println(o.getKey() + " - " + o.getValue());
            }
        }*/
        return null;
    }

    public String getByKey(String s) {
        return prop.getProperty(s, "Key not found, try again");
    }

    public boolean save(String s) {
        String[] valueToSave = s.trim().split(" - ", 2);
        EngRuDictWord word = new EngRuDictWord();
        word.setEnglishWord(valueToSave[0]);
        word.setRuWord(valueToSave[1]);
        DataBinder dataBinder = new DataBinder(word);
        dataBinder.addValidators(engRuDictValidator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            System.out.println(dataBinder.getBindingResult().getAllErrors());
            log.warn("{} is not valid string", s);
            return false;
            //throw new RuntimeException("Validation Error");
        }
        prop.setProperty(word.getEnglishWord(), word.getRuWord());
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
            isChanged = true;
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to save");
        }
        return true;
    }

    public void update(String s) {
        //сделаю позже
    }

    public boolean deleteByKey(String s) {
        prop.remove(s);
        try {
            prop.store(new FileOutputStream(FILE_NAME), null);
            isChanged = true;
        } catch (IOException e) {
            throw new RuntimeException("No such properties file found to delete");
        }
        return true;
    }

}
