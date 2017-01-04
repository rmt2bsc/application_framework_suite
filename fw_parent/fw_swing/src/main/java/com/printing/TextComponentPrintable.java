package com.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;

public class TextComponentPrintable implements Printable {
    JTextComponent comp;

    public TextComponentPrintable(String textData) {
        this.comp = new JEditorPane();
        this.comp.setText(textData);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex != 0) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        Rectangle componentBounds = comp.getBounds(null);
        g2.translate(-componentBounds.x, -componentBounds.y);
        g2.scale(1, 1);
        boolean wasBuffered = comp.isDoubleBuffered();
        comp.paint(g2);
        comp.setDoubleBuffered(wasBuffered);
        g2.drawString(comp.getText(), componentBounds.x, componentBounds.y);
        return PAGE_EXISTS;
    }

}
