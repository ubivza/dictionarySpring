package ru.aleksandr.dictionaryspring.utils;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.aleksandr.dictionaryspring.models.EngRuDictWord;

@Service
public class EngRuDictValidator implements Validator {
    private static final String PATTERN = "[0-9]{5}";
    private static final String ENG_FIELD_NAME = "englishWord";
    private static final String RU_FIELD_NAME = "ruWord";
    private static final String ERROR_MESSAGE_BLANK = "Word shouldn't be empty";
    private static final String ERROR_CHARACTERS_LONG = "Word must be 5 characters long and should contain only numbers";
    @Override
    public boolean supports(Class<?> clazz) {
        return EngRuDictWord.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EngRuDictWord engRuDictWord = (EngRuDictWord) target;
        if (engRuDictWord.getEnglishWord().isBlank()) {
            errors.rejectValue(ENG_FIELD_NAME, "", ERROR_MESSAGE_BLANK);
        }
        if (engRuDictWord.getRuWord().isBlank()) {
            errors.rejectValue(RU_FIELD_NAME, "", ERROR_MESSAGE_BLANK);
        }
        if (!engRuDictWord.getEnglishWord().matches(PATTERN)) {
            errors.rejectValue(ENG_FIELD_NAME, "", ERROR_CHARACTERS_LONG);
        }
    }
}
