package ru.aleksandr.dictionaryspring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;
import ru.aleksandr.dictionaryspring.utils.EngRuDictValidator;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Repository
@Slf4j
public class EngRuRepositoryImpl implements EngRuRepository {
    private static boolean isChanged = false;
    private final Properties prop;
    private final String FILE_NAME = "src/main/resources/static/dictionary1.properties";
    private final EngRuDictValidator engRuDictValidator;
    private Map<Object, Object> cacheMap = new HashMap<>();

    @Autowired
    public EngRuRepositoryImpl(EngRuDictValidator engRuDictValidator) {
        this.engRuDictValidator = engRuDictValidator;
        this.prop = new Properties();
        try(InputStream in = new FileInputStream(FILE_NAME)) {
            prop.load(in);
            fillCacheMap();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file");
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong");
        }
    }


    public List<String> getAll() {
        if (!isChanged) {
            for (Map.Entry<Object, Object> map : cacheMap.entrySet()) {
                System.out.println(map.getKey() + " - " + map.getValue());
            }
        } else {
            fillCacheMap();
            for (Map.Entry<Object, Object> map : cacheMap.entrySet()) {
                System.out.println(map.getKey() + " - " + map.getValue());
            }
        }
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
            log.warn("{} is not valid data to add", s);
            return false;
            //throw new RuntimeException("Validation Error"); - заменил на логгер
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

    public boolean update(String s) {
        //в задании нет пункта, реализую позже
        return false;
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

    private void fillCacheMap() {
        cacheMap.putAll(prop);
        log.info("Cache was refreshed");
    }

}
