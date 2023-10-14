package ru.aleksandr.dictionaryspring.utils;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.aleksandr.dictionaryspring.models.SpanishRuDictionaryWord;

@Service
public class SpanishRuDictValidator implements Validator {
    private static final String PATTERN = "[A-Z, a-z]{4}";
    private static final String SPN_FIELD_NAME = "spanishWord";
    private static final String RU_FIELD_NAME = "ruWord";
    private static final String ERROR_MESSAGE_BLANK = "Word shouldn't be empty";
    private static final String ERROR_CHARACTERS_LONG = "Word must be 4 characters long and should contain only latin letters";
    @Override
    public boolean supports(Class<?> clazz) {
        return SpanishRuDictionaryWord.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpanishRuDictionaryWord spanishRuDictionaryWord = (SpanishRuDictionaryWord) target;
        if (spanishRuDictionaryWord.getSpanishWord().isBlank()) {
            errors.rejectValue(SPN_FIELD_NAME, "", ERROR_MESSAGE_BLANK);
        }
        if (spanishRuDictionaryWord.getRuWord().isBlank()) {
            errors.rejectValue(RU_FIELD_NAME, "", ERROR_MESSAGE_BLANK);
        }
        if (!spanishRuDictionaryWord.getSpanishWord().matches(PATTERN)) {
            errors.rejectValue(SPN_FIELD_NAME, "", ERROR_CHARACTERS_LONG);
        }
    }
}
