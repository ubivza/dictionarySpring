package ru.aleksandr.dictionaryspring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;
import ru.aleksandr.dictionaryspring.utils.EngRuDictValidator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Repository
@Slf4j
public class EngRuRepositoryImpl implements EngRuRepository, Cacheable{
    private final Properties properties;
    private final String FILE_NAME = "src/main/resources/static/dictionary1.properties";
    private final EngRuDictValidator engRuDictValidator;
    private Map<String, String> cacheMap = new HashMap<>();

    @Autowired
    public EngRuRepositoryImpl(EngRuDictValidator engRuDictValidator) {
        this.engRuDictValidator = engRuDictValidator;
        this.properties = new Properties();

        try(InputStream in = new FileInputStream(FILE_NAME)) {
            properties.load(in);

            cacheMap.putAll((Map) properties);
            log.info("CacheMap of first dict was filled with data from properties file");

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

        EngRuDictWord word = new EngRuDictWord();
        word.setEnglishWord(valueToSave[0]);
        word.setRuWord(valueToSave[1]);

        //-------------------------------------------
        DataBinder dataBinder = new DataBinder(word);
        dataBinder.addValidators(engRuDictValidator);
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
        //-------------------------------------------

        //для дубликатов, при сохранении повторного ключа можно просто сделать конкатенацию
        //value через заптую, то есть просто добавить перевод к уже существующему (если это нужно)
        cacheMap.put(word.getEnglishWord(), word.getRuWord());
        return true;
    }

    public boolean update(String s) {
        //в задании нет пункта, реализую если нужно
        return false;
    }

    public boolean deleteByKey(String s) {
        log.info("Trying to delete {}", s);
        if (cacheMap.containsKey(s)) {
            cacheMap.remove(s);
            return true;
        } else {
            return false;
        }
    }

    public void saveCacheToMemory() {
        try(OutputStream out = new FileOutputStream(FILE_NAME)) {
            properties.putAll(cacheMap);
            log.info("CacheMap of first dict is pushing to properties file");
            properties.store(out, null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such properties file while saving");
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while saving properties file");
        }
    }
}