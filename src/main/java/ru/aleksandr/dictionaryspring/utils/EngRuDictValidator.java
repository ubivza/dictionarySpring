package ru.aleksandr.dictionaryspring.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;

@Component
public class EngRuDictValidator implements Validator {
    private static String pattern = "[0-9]{5}";
    @Override
    public boolean supports(Class<?> clazz) {
        return EngRuDictWord.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EngRuDictWord engRuDictWord = (EngRuDictWord) target;
        if (engRuDictWord.getEnglishWord().isBlank()) {
            errors.rejectValue("englishWord", "", "Word shouldn't be empty");
        }
        if (engRuDictWord.getRuWord().isBlank()) {
            errors.rejectValue("ruWord", "", "Translate shouldn't be empty");
        }
        if (!engRuDictWord.getEnglishWord().matches(pattern)) {
            errors.rejectValue("englishWord", "", "Word must be 5 characters long and should contain only numbers");
        }
    }
}
