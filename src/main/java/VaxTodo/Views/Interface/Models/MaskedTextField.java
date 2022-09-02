package VaxTodo.Views.Interface.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

/** Sub class of TextField that uses masking techniques to prevent user entering wrongfull data (example: only let user enter numbers, etc.)
 * Is sub class of TextField
 * @author Mohamed
 * @author Christian
 * @author Louis
 * @since v1.0.0
 */
public class MaskedTextField extends TextField {
    private static final char charNumbers = '0', charLettersNumbers = 'A', charLetters = 'S', charPostalCode = 'P', charEmail = 'E', charToMask = '#', charNoAss = 'M';

    private List<CharFromInputField> arrInputCharacters = new ArrayList<>();

    // plain text without masking
    private StringProperty plainText;
    public final String getPlainText() {
        return plainTextProperty().get();
    }
    public final void setPlainText(String value) {
        plainTextProperty().set(value);
        updateInputField();
    }
    public final StringProperty plainTextProperty() {
        if (plainText == null)
            plainText = new SimpleStringProperty(this, "plainText", "");

        return plainText;
    }

    // this is the mask itself visible in the input field
    private StringProperty mask;
    public final String getMask() {
        // System.out.println(maskProperty().get());
        return maskProperty().get();
    }
    public final void setMask(String value) {
        maskProperty().set(value);
        rebuildObjectMask();
        updateInputField();
    }
    public final StringProperty maskProperty() {
        if (mask == null)
            mask = new SimpleStringProperty(this, "mask");

        return mask;
    }

    //! // if the mask should display symbols that are reserved for the mask, 
    // // then an additional hint is set where the mask symbol is, 
    // // and where is just a symbol
    // private StringProperty whatMask;
    // public final String getWhatMask() {
    //     // System.out.println(whatMaskProperty().get());
    //     return whatMaskProperty().get();
    // }
    // public final void setWhatMask(String value) {
    //     whatMaskProperty().set(value);
    //     rebuildObjectMask();
    //     updateInputField();
    // }
    // public final StringProperty whatMaskProperty() {
    //     if (whatMask == null) {
    //         whatMask = new SimpleStringProperty(this, "whatMask");
    //     }
    //     return whatMask;
    // }

    // // these are placeholders
    // private StringProperty placeholder;
    // public final String getPlaceholder() {
    //     return placeholderProperty().get();
    // }
    // public final void setPlaceholder(String value) {
    //     placeholderProperty().set(value);
    //     rebuildObjectMask();
    //     updateInputField();
    // }
    // public final StringProperty placeholderProperty() {
    //     if (placeholder == null)
    //         placeholder = new SimpleStringProperty(this, "placeholder");
    //     return placeholder;
    // }

    private class CharFromInputField {
        private char charMaskFromFXML;
        private char charInputFieldToMask;

        public CharFromInputField(char charMaskFromFXML, char charInputFieldToMask) {
            this.charMaskFromFXML = charMaskFromFXML;
            this.charInputFieldToMask = charInputFieldToMask;
        }
        
        public char getCharMaskFromFXML() {
            return this.charMaskFromFXML;
        }
        public char getCharInputFieldToMask() {
            return this.charInputFieldToMask;
        }

        public void setCharMaskFromFXML(char charMaskFromFXML) {
            this.charMaskFromFXML = charMaskFromFXML;
        }
        public void setCharInputFieldToMask(char charInputFieldToMask) {
            this.charInputFieldToMask = charInputFieldToMask;
        }

        public boolean isInputFieldMaskable() {
            // System.out.println("\n\n");
            // System.out.println(charInputFieldToMask);
            return charInputFieldToMask == charToMask;
        }

        public boolean isValidCharacterMask(char c) {
            switch (charMaskFromFXML) {
                case charNumbers:
                    return Character.isDigit(c);

                case charLetters:
                    return Character.isLetter(c) || c == ' ' || c == '-' || c == '_';
                    
                case charLettersNumbers:
                    return Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_';

                // only uppercase letters mask is accepted in postal code
                case charPostalCode:
                    return Character.isLetter(c); //Character.isUpperCase(c);

                case charEmail:
                    return Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_' || c == '@' || c == '.'; //|| c == '#';

                case charNoAss:
                    return Character.isLetter(c) || Character.isDigit(c);
            }
            
            return false;
        }

        @Override
        public String toString() {
            return "{" +
                " charMaskFromFXML='" + charMaskFromFXML + "'" +
                ", charInputFieldToMask='" + charInputFieldToMask + "'" +
                "}";
        }
    }

    // generates a list of Position objects for each mask symbol
    private void rebuildObjectMask() {
        arrInputCharacters = new ArrayList<>();

        for (int i = 0; i < getMask().length(); i++) {
            char charMaskFromFXML = getMask().charAt(i), charInputFieldToMask;
            
            // char w = charToMask;
            // char p = PLACEHOLDER_CHAR_DEFAULT;

            //!
            /*if (getWhatMask() != null && i < getWhatMask().length()) {
                // mask symbol is specifically indicated whether it is or not
                if (getWhatMask().charAt(i) != charToMask)
                    w = '-';
            }
            else {
                // since it is not indicated what the symbol is - we understand it on our own
                // and if the character is not among the characters of the mask, then it is considered a simple literal
                if (m != charNumbers && m != charLetters && m != charLettersNumbers)
                    w = '-';
            }*/

            // if (getPlaceholder() != null && i < getPlaceholder().length())
            //     p = getPlaceholder().charAt(i);

            // if (charMaskFromFXML != charNumbers && charMaskFromFXML != charLetters && charMaskFromFXML != charLettersNumbers)
            //     charInputFieldToMask = charMaskFromFXML;
            // else
            //     charInputFieldToMask = charToMask;

            // only mask characters which are defined in FXMl such as [0, A, S]
            if (charMaskFromFXML == charNumbers || charMaskFromFXML == charLetters || charMaskFromFXML == charLettersNumbers || charMaskFromFXML == charPostalCode || charMaskFromFXML == charEmail || charMaskFromFXML == charNoAss)
                charInputFieldToMask = charToMask;
            else
                charInputFieldToMask = charMaskFromFXML;
                    
            arrInputCharacters.add(new CharFromInputField(charMaskFromFXML, charInputFieldToMask));
        }
    }

    // function imposes just plainText on the given mask,
    // adjusts the carriage position
    private void updateInputField() {
        int counterPlainCharInMask = 0,
            lastPositionPlainCharInMask = 0,
            firstPlaceholderInMask = -1;

        String textMask = "",
                textPlain = getPlainText();

        // System.out.println("\n" + textPlain + "\n");
        
        // System.out.println("\n");
        // String s = "";
        // for (CharFromInputField charFromInputField : arrInputCharacters)
        //     s += charFromInputField + "\n";
        // System.out.println(s + "\n\n");

        for (int i = 0; i < arrInputCharacters.size(); i++) {
            CharFromInputField charFromInputField = arrInputCharacters.get(i);

            if (charFromInputField.isInputFieldMaskable()) { // if input character == '#'
                if (textPlain.length() > counterPlainCharInMask) {

                    char c = textPlain.charAt(counterPlainCharInMask);
                    while (!charFromInputField.isValidCharacterMask(c)) {
                        // cut out what did not fit
                        textPlain = textPlain.substring(0, counterPlainCharInMask) + textPlain.substring(counterPlainCharInMask + 1);

                        if (textPlain.length() > counterPlainCharInMask)
                            c = textPlain.charAt(counterPlainCharInMask);
                        else
                            break;
                    }

                    textMask += c;
                    lastPositionPlainCharInMask = i;
                }
                else {
                    // textMask += p.placeholder;
                    if (firstPlaceholderInMask == -1)
                        firstPlaceholderInMask = i;
                }

                counterPlainCharInMask++;
            } 
            else {
                textMask += charFromInputField.getCharMaskFromFXML(); //p.mask;
            }
        }
        // System.out.println("\n\n" + textMask + "\n\n");

        setText(textMask);

        if (firstPlaceholderInMask == -1)
            firstPlaceholderInMask = 0;

        int caretPosition = (textPlain.length() > 0 ? lastPositionPlainCharInMask + 1 : firstPlaceholderInMask);
        selectRange(caretPosition, caretPosition);

        if (textPlain.length() > counterPlainCharInMask)
            textPlain = textPlain.substring(0, counterPlainCharInMask);

        if (!textPlain.equals(getPlainText()))
            setPlainText(textPlain);
    }

    // 
    private int interpretMaskPositionInPlainPosition(int posMask) {
        int posPlain = 0;

        // System.out.println();

        for (int i = 0; i < arrInputCharacters.size() && i < posMask; i++) {
            CharFromInputField p = arrInputCharacters.get(i);

            if (p.isInputFieldMaskable()) {
                posPlain++;
                // System.out.println(p.isInputFieldMaskable());
            }
        }

        // System.out.println("\nPos Plain : " + posPlain + "\n\n");
        return posPlain;
    }

    @Override
    public void replaceText(int intIndexStart, int intIndexEnd, String strText) {
        int intPlainTextIndexStart = interpretMaskPositionInPlainPosition(intIndexStart), 
            intPlainTextIndexEnd = interpretMaskPositionInPlainPosition(intIndexEnd);

        // System.out.println("\nintIndexStart: " + intIndexStart + ", intIndexEnd: " + intIndexEnd);
        // System.out.println("intPlainTextIndexStart: " + intPlainTextIndexStart + ", intPlainTextIndexEnd: " + intPlainTextIndexEnd);

        String strPlainTextStart = "", strPlainTextEnd = "";

        if (getPlainText().length() > intPlainTextIndexStart)
            strPlainTextStart = getPlainText().substring(0, intPlainTextIndexStart);
        else
            strPlainTextStart = getPlainText();

        if (getPlainText().length() > intPlainTextIndexEnd)
            strPlainTextEnd = getPlainText().substring(intPlainTextIndexEnd);
        else
            strPlainTextEnd = "";

        setPlainText(strPlainTextStart + strText + strPlainTextEnd);
        // setPlainText(strText);
    }
}
