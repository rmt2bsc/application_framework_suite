package com.ui.components.text.wordsearch;

import java.awt.Color;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * A simple class that searches for a word in a document and highlights
 * occurrences of that word.
 * 
 * @author rterrell
 *
 */
public class TextCompWordSearcher {

    private JTextComponent comp;

    private Highlighter.HighlightPainter painter;

    /**
     * 
     */
    public TextCompWordSearcher(JTextComponent comp) {
        this.comp = comp;
        this.painter = new BasicTextHighlighter(Color.YELLOW);
    }

    /**
     * Remove any existing highlights for last word
     */
    public void removeHighlights() {
        Highlighter highlighter = comp.getHighlighter();

        // Remove any existing highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof BasicTextHighlighter) {
                highlighter.removeHighlight(h);
            }
        }
        return;
    }

    /**
     * Search for a word and return the offset of the first occurrence.
     * <p>
     * Highlights are added for all occurrences found.
     * 
     * @param word
     *            The search argument
     * @return the index of the first occurence
     */
    public int search(String word) {
        int firstOffset = -1;
        Highlighter highlighter = comp.getHighlighter();

        // Remove any existing highlights for last word
        this.removeHighlights();

        // Validate the search argument
        if (word == null || word.equals("")) {
            return -1;
        }

        // Look for the word we are given - insensitive search
        String content = null;
        try {
            Document d = comp.getDocument();
            content = d.getText(0, d.getLength()).toLowerCase();
        } catch (BadLocationException e) {
            // Cannot happen
            return -1;
        }

        word = word.toLowerCase();
        int lastIndex = 0;
        int wordSize = word.length();

        while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
            int endIndex = lastIndex + wordSize;
            try {
                highlighter.addHighlight(lastIndex, endIndex, painter);
            } catch (BadLocationException e) {
                // Nothing to do
            }
            if (firstOffset == -1) {
                firstOffset = lastIndex;
            }
            lastIndex = endIndex;
        }

        return firstOffset;
    }
}
