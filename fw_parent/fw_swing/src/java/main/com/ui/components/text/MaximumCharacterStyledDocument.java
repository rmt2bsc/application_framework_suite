package com.ui.components.text;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * A custom {@link javax.swing.text.Document} implementation that limiits the
 * number of characters that a text component can contain.
 * <p>
 * Serves as an alternative to using {@link javax.swing.text.MaskFormatter} for
 * input character limitation. This class extendes {@link DefaultStyledDocument}
 * in order to accomadate JTextPane related components since JTextPane requires
 * its document to be of that type. This component will automatically work for
 * JTextField and JTextArea componets since they expect the assoicated document
 * to be of type {@link javax.swing.text.Document}.
 * 
 * @author rterrell
 *
 */
public class MaximumCharacterStyledDocument extends DefaultStyledDocument {
    private static final long serialVersionUID = 8071174447953441716L;

    private int maxCharacters;

    /**
     * Create a {@link MaximumCharacterStyledDocument} specifying the maximum
     * number characters the document can contain.
     * 
     * @param maxChars
     *            The maximum characters allowed.
     */
    public MaximumCharacterStyledDocument(int maxChars) {
        maxCharacters = maxChars;
    }

    /**
     * Overridden to limit the document to the maximum allowed characters which
     * is called each time text is inserted into the document.
     * <p>
     * Text insertiion can be the result of the user typeing or pasting text in,
     * or because of a call to <i>setText</i>.
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {

        // This rejects the entire insertion if it would make
        // the contents too long. Another option would be
        // to truncate the inserted string so the contents
        // would be exactly maxCharacters in length.
        if ((getLength() + str.length()) <= maxCharacters)
            super.insertString(offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }
}
