package ru.aleksandr.dictionaryspring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import ru.aleksandr.dictionaryspring.models.SpanishRuDictionaryWord;
import ru.aleksandr.dictionaryspring.utils.SpanishRuDictValidator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Repository
@Slf4j
public class SpanishRuRepositoryImpl implements SpanishRuRepository, Cacheable {
    private final Properties properties;
    private final String FILE_NAME = "src/main/resources/static/dictionary2.properties";
    private SpanishRuDictValidator spanishRuDictValidator;
    private Map<String, String> cacheMap = new HashMap<>();

    @Autowired
    public SpanishRuRepositoryImpl(SpanishRuDictValidator spanishRuDictValidator) {
        this.spanishRuDictValidator = spanishRuDictValidator;
        this.properties = new Properties();

        try(InputStream in = new FileInputStream(FILE_NAME)) {
            properties.load(in);

            cacheMap.putAll((Map) properties);

            properties.clear();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file while loading");
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while loading properties file");
        }
    }


    public Map<String, String> getAll() {
        return cacheMap;
    }

    public String getByKey(String s) {
        if (!cacheMap.containsKey(s))
            return "Key not found, try again";
        return cacheMap.get(s);
    }

    public boolean save(String s) {
        String[] valueToSave = s.trim().split(" - ", 2);

        SpanishRuDictionaryWord word = new SpanishRuDictionaryWord();
        word.setSpanishWord(valueToSave[0]);
        word.setRuWord(valueToSave[1]);

        DataBinder dataBinder = new DataBinder(word);
        dataBinder.addValidators(spanishRuDictValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            for (ObjectError str : dataBinder.getBindingResult().getAllErrors()) {
                System.out.println(str.getDefaultMessage());
            }
            System.out.println();
            log.warn("{} is not valid data to add,\nValidation message: {}\n",
                    s, dataBinder.getBindingResult().getAllErrors());
            return false;
        }

        cacheMap.put(word.getSpanishWord(), word.getRuWord());
        return true;
    }

    public boolean update(String s) {
        //в задании нет указаний, сделаю по надобности
        return false;
    }

    public boolean deleteByKey(String s) {
        if (cacheMap.containsKey(s)) {
            cacheMap.remove(s);
            log.info("{} delete", s);
            return true;
        } else {
            return false;
        }
    }

    public void saveCacheToMemory() {
        try(OutputStream out = new FileOutputStream(FILE_NAME)) {
            properties.putAll(cacheMap);
            properties.store(out, null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file while saving");
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while saving properties file");
        }
    }
}
