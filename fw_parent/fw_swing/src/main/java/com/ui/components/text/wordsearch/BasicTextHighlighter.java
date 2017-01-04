package com.ui.components.text.wordsearch;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/**
 * A class for highlighting words in a text document by changing the background
 * of the word in a color specified by the user.
 * 
 * @author rterrell
 *
 */
public class BasicTextHighlighter extends DefaultHighlightPainter {

    /**
     * Create a BasicTextHighlighter by specifying the background color.
     * 
     * @param arg0
     */
    public BasicTextHighlighter(Color arg0) {
        super(arg0);
        return;
    }

}
