// src/hospital/PhoneNumberDocumentFilter.java
package hospital;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class PhoneNumberDocumentFilter extends DocumentFilter {
    private final int maxLength;

    public PhoneNumberDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        Document doc = fb.getDocument();
        String current = doc.getText(0, doc.getLength());
        StringBuilder sb = new StringBuilder(current);
        sb.insert(offset, string);
        if (isValid(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        String current = doc.getText(0, doc.getLength());
        StringBuilder sb = new StringBuilder(current);
        sb.replace(offset, offset + length, text == null ? "" : text);
        if (isValid(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    private boolean isValid(String value) {
        if (value == null) return true;
        if (value.length() > maxLength) return false;
        return value.matches("\\d*");
    }
}
