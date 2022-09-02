package VaxTodo.Views.Interface.Models;

import VaxTodo.Configs.Config;
import javafx.scene.control.TextField;

public class TextFieldCodeIdentification extends TextField {
    private int intMaxLength;

    public TextFieldCodeIdentification() {
        super();
        this.intMaxLength = Config.intFormatLengthCodeIdentification;
    }
    
    public void setMaxlength(int intMaxLength) {
        this.intMaxLength = intMaxLength;
    }

    // @Override
    // public void replaceText(int start, int end, String text) {
    //     // Delete or backspace user input.
    //     if (getText() == null || getText().equalsIgnoreCase("")) {
    //         valor = "";
    //     }
    //     if (text.equals("")) {
    //         super.replaceText(start, end, text);
    //     } else{


    //         text = text.replaceAll("[^0-9]", "");
    //         valor += text;

    //         super.replaceText(start, end, text);
    //         if (!valor.equalsIgnoreCase(""))
    //             setText(formata(valor));
    //     }
    // }

    // @Override
    // public void replaceSelection(String text) {
    //     // Delete or backspace user input.
    //     if (text.equals("")) {
    //         super.replaceSelection(text);
    //     } else if (getText().length() < intMaxLength) {
    //         // Add characters, but don't exceed maxlength.
    //         // text = MascaraFinanceira.show(text);
    //         if (text.length() > intMaxLength - getText().length()) {
    //             // text = MascaraFinanceira.show(text);
    //             text = text.substring(0, intMaxLength - getText().length());
    //         }
    //         super.replaceSelection(text);
    //     }
    // }

    // public String getCleanValue(){
    //     String cleanString = getText().replaceAll("[^0-9]", "");
    //     Double cleanNumber = new Double(cleanString);
    //     return String.valueOf(cleanNumber/100);
    // }

    // private String formata(Double valor) {
    //     Locale locale = new Locale("pt", "BR");
    //     NumberFormat nf = NumberFormat.getInstance(locale);
    //     nf.setMaximumFractionDigits(2);
    //     nf.setMinimumFractionDigits(2);

    //     return nf.format(valor);
    // }

    // public String formata(String valor) {
    //     double v = new Double(valor);
    //     return formata(v/100);
    // }
}
