package VaxTodo.Views.Interface.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class ExampleMaskedTextField extends TextField {
    /**
     * position in the mask will allow only numbers to be entered 
     */
    public static final char MASK_DIGIT = 'D';

    /**
     * position in the mask will allow you to enter letters and numbers 
     */
    public static final char MASK_DIG_OR_CHAR = 'W';

    /**
     * position in the mask will only allow letters
     */
    public static final char MASK_CHARACTER = 'A';


    public static final char WHAT_MASK_CHAR = '#';
    public static final char WHAT_MASK_NO_CHAR = '-';


    public static final char PLACEHOLDER_CHAR_DEFAULT = '_';


    private List<Position> objectMask = new ArrayList<>();

    /**
     * plain text without masking
     */
    private StringProperty plainText;

    public final String getPlainText() {
        return plainTextProperty().get();
    }

    public final void setPlainText(String value) {
        plainTextProperty().set(value);
        updateShowingField();
    }

    public final StringProperty plainTextProperty() {
        if (plainText == null)
            plainText = new SimpleStringProperty(this, "plainText", "");
        return plainText;
    }


    /**
     * this is the mask itself visible in the input field
     */
    private StringProperty mask;

    public final String getMask() {
        return maskProperty().get();
    }

    public final void setMask(String value) {
        maskProperty().set(value);
        rebuildObjectMask();
        updateShowingField();
    }

    public final StringProperty maskProperty() {
        if (mask == null)
            mask = new SimpleStringProperty(this, "mask");

        return mask;
    }


    /**
     * if the mask should display symbols that are reserved for the mask, then an additional hint is set where the mask symbol is, and where is just a symbol
     */
    private StringProperty whatMask;

    public final String getWhatMask() {
        return whatMaskProperty().get();
    }

    public final void setWhatMask(String value) {
        whatMaskProperty().set(value);
        rebuildObjectMask();
        updateShowingField();
    }

    public final StringProperty whatMaskProperty() {
        if (whatMask == null) {
            whatMask = new SimpleStringProperty(this, "whatMask");
        }
        return whatMask;
    }


    /**
     * these are placeholders
     */
    private StringProperty placeholder;

    public final String getPlaceholder() {
        return placeholderProperty().get();
    }

    public final void setPlaceholder(String value) {
        placeholderProperty().set(value);
        rebuildObjectMask();
        updateShowingField();
    }

    public final StringProperty placeholderProperty() {
        if (placeholder == null)
            placeholder = new SimpleStringProperty(this, "placeholder");
        return placeholder;
    }


    private class Position {
        public char mask;
        public char whatMask;
        public char placeholder;

        public Position(char mask, char whatMask, char placeholder) {
            this.mask = mask;
            this.placeholder = placeholder;
            this.whatMask = whatMask;
        }

        public boolean isPlainCharacter()
        {
            return whatMask == WHAT_MASK_CHAR;
        }

        public boolean isCorrect(char c)
        {
            switch (mask)
            {
                case MASK_DIGIT:
                    return Character.isDigit(c);
                case MASK_CHARACTER:
                    return Character.isLetter(c);
                case MASK_DIG_OR_CHAR:
                    return Character.isLetter(c) || Character.isDigit(c);
            }
            return false;
        }
    }


    /**
     * generates a list of Position objects for each mask symbol
     */
    private void rebuildObjectMask() {
        objectMask = new ArrayList<>();

        for (int i = 0; i < getMask().length(); i++) {
            char m = getMask().charAt(i);
            char w = WHAT_MASK_CHAR;
            char p = PLACEHOLDER_CHAR_DEFAULT;

            if (getWhatMask() != null && i < getWhatMask().length()) {
                // mask symbol is specifically indicated whether it is or not
                if (getWhatMask().charAt(i) != WHAT_MASK_CHAR) {
                    w = WHAT_MASK_NO_CHAR;
                }
            }
            else
            {
                // since it is not indicated what the symbol is - we understand it on our own
                // and if the character is not among the characters of the mask, then it is considered a simple literal
                if (m != MASK_CHARACTER && m != MASK_DIG_OR_CHAR && m != MASK_DIGIT)
                    w = WHAT_MASK_NO_CHAR;

            }

            if (getPlaceholder() != null && i < getPlaceholder().length())
                p = getPlaceholder().charAt(i);

            objectMask.add(new Position(m, w, p));
        }
    }


    /**
     * the function imposes just plainText on the given mask,
     * adjusts the carriage position
     */
    private void updateShowingField()
    {
        int counterPlainCharInMask = 0;
        int lastPositionPlainCharInMask = 0;
        int firstPlaceholderInMask = -1;
        String textMask = "";
        String textPlain = getPlainText();
        for (int i = 0; i < objectMask.size(); i++) {
            Position p = objectMask.get(i);
            if (p.isPlainCharacter()) {
                if (textPlain.length() > counterPlainCharInMask) {

                    char c = textPlain.charAt(counterPlainCharInMask);
                    while (!p.isCorrect(c))
                    {
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
                    textMask += p.placeholder;
                    if (firstPlaceholderInMask == -1)
                        firstPlaceholderInMask = i;
                }

                counterPlainCharInMask++;

            } else {
                textMask += p.mask;
            }
        }

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



    private int interpretMaskPositionInPlainPosition(int posMask)
    {
        int posPlain = 0;

        for (int i = 0; i < objectMask.size() && i < posMask; i++) {
            Position p = objectMask.get(i);
            if (p.isPlainCharacter())
                posPlain++;
        }

        return posPlain;
    }


    @Override
    public void replaceText(int start, int end, String text) {


        int plainStart = interpretMaskPositionInPlainPosition(start);
        int plainEnd = interpretMaskPositionInPlainPosition(end);

        String plainText1 = "";
        if (getPlainText().length() > plainStart)
            plainText1 = getPlainText().substring(0, plainStart);
        else
            plainText1 = getPlainText();


        String plainText2 = "";
        if (getPlainText().length() > plainEnd)
            plainText2 = getPlainText().substring(plainEnd);
        else
            plainText2 = "";


        setPlainText(plainText1 + text + plainText2);

    }
}
