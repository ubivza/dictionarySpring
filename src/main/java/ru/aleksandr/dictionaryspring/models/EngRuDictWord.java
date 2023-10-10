package ru.aleksandr.dictionaryspring.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EngRuDictWord {
    @Pattern(regexp = "[0-9]{5}", message = "Word must be 5 characters long and should contain only numbers")
    @NotBlank
    private String englishWord;
    @NotBlank
    private String ruWord;
}
