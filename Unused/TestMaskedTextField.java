package VaxTodo.Views.Interface.Models;

import java.util.List;

import javax.swing.text.Position;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class TestMaskedTextField extends TextField {
    private static final char charNumbers = '0', charLettersNumbers = 'A', charLetters = 'S', charMask = '#';

    private List<Position> arrMaskPosition;

    // plain text without masking
    private StringProperty plainText;
    public final String getPlainText() {
        return plainTextProperty().get();
    }
    public final void setPlainText(String value) {
        // System.out.println(value);

        plainTextProperty().set(value);
        updateShowingField();
    }
    public final StringProperty plainTextProperty() {
        if (plainText == null)
            plainText = new SimpleStringProperty(this, "plainText", "");
        return plainText;
    }

    private class MaskCharPosition {
        private char mask;
        private char whatMask;
        // private char placeholder;

        public MaskCharPosition(char mask, char whatMask/*, char placeholder*/) {
            this.mask = mask;
            // this.placeholder = placeholder;
            this.whatMask = whatMask;
        }
        
        public char getCharMask() {
            return this.mask;
        }
        public char getCharWhatMask() {
            return this.whatMask;
        }

        public void setCharMask(char mask) {
            this.mask = mask;
        }
        public void setCharWhatMask(char whatMask) {
            this.whatMask = whatMask;
        }

        public boolean isPlainCharacter() {
            return whatMask == charMask; //WHAT_MASK_CHAR;
        }

        public boolean isCorrect(char c) {
            switch (mask) {
                case charNumbers:
                    return Character.isDigit(c);

                case charLetters:
                    return Character.isLetter(c);
                    
                case charLettersNumbers:
                    return Character.isLetter(c) || Character.isDigit(c);
            }
            
            return false;
        }
    }

    // function imposes just plainText on the given mask,
    // adjusts the carriage position
    private void updateShowingField() {
        setText(getPlainText());
    }

    @Override
    public void replaceText(int intIndexStart, int intIndexEnd, String strText) {
        // int intPlainTextIndexStart = interpretMaskPositionInPlainPosition(intIndexStart), 
        //     intPlainTextIndexEnd = interpretMaskPositionInPlainPosition(intIndexEnd);

        // String strPlainTextStart = "", strPlainTextEnd = "";

        // if (getPlainText().length() > intPlainTextIndexStart)
        //     strPlainTextStart = getPlainText().substring(0, intPlainTextIndexStart);
        // else
        //     strPlainTextStart = getPlainText();

        // if (getPlainText().length() > intPlainTextIndexEnd)
        //     strPlainTextEnd = getPlainText().substring(intPlainTextIndexEnd);
        // else
        //     strPlainTextEnd = "";

        // setPlainText(strPlainTextStart + strText + strPlainTextEnd);
        // // setPlainText(strText);

        setPlainText(strText);

        // if (strText.matches("[0-9]*"))
        //     super.replaceText(intIndexStart, intIndexEnd, strText);
    }

    // @Override
    // public void replaceSelection(String strText) {
    //     if (strText.matches("[0-9]*")) 
    //         super.replaceSelection(strText);
    // }
}
