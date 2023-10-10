package ru.aleksandr.dictionaryspring.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpanishRuDictionaryWord {
    @NotBlank
    @Pattern(regexp = "[A-Z, a-z]{4}", message = "Word must be 4 characters long and should contain only latin letters")
    private String spanishWord;
    @NotBlank
    private String ruWord;
}
