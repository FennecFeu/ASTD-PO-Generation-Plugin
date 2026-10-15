package com.udes.model.il.predicates;

import java.io.Serializable;
import java.util.ResourceBundle;

import com.udes.model.il.statements.Statement;
import com.udes.translator.ILTranslator;

public abstract class Predicate implements Serializable {
    /*
     * @brief Generates code in the target language
     * @param The statement
     * @param The type of event
     * @param The language
     * @param The resource bundle
     * @param The string builder
     */
    public abstract String generateCode(Statement stmt, String eventType, ILTranslator.Lang lang, ResourceBundle bundle);
}
