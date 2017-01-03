package com.util;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import com.RMT2Base;

/**
 * @author rterrell
 * 
 */
public class RMT2SwingUtil extends RMT2Base {

    /**
     * 
     */
    public RMT2SwingUtil() {
        return;
    }

    /**
     * Creates an instance of {@link Color} from a list of RGB values containted
     * in <i>colors</i>.
     * 
     * @param colors
     *            a String representing the RGB numeric values that will
     *            comprise the color. The list of values should occur in the
     *            order of red, green, and blue and separated by commas.
     * @param defColor
     *            an instance of {@link Color} representing the default color
     *            assinged in the event an error occurs or if <i>colors</i> is
     *            found to be invalid.
     * @return an instance of {@link Color} based on the values containted in
     *         <i>colors</i> or the default known as <i>defColor</i>.
     */
    public static final Color createColorInstance(String colors, Color defColor) {
        // The color configuration input argument must not be null or ""
        if (colors == null || colors.equals("")) {
            return defColor;
        }

        // Convert colors argument into an array of 3 numeric Strings.
        String rgb[];
        int total = 0;
        try {
            rgb = colors.split(",");
            total = rgb.length;
            if (total != 3) {
                return defColor;
            }
        } catch (Exception e) {
            return defColor;
        }

        // Verify that all values are numerics and trim each String value.
        for (int ndx = 0; ndx < total; ndx++) {
            String item = rgb[ndx].trim();
            if (!RMT2Money.isNumeric(item)) {
                return defColor;
            }
            rgb[ndx] = item;
        }

        // Create Color instance
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]),
                Integer.parseInt(rgb[2]));
    }

    /**
     * Creates a JFormattedTextField using a specified mask.
     * 
     * @param mask
     *            a valid mask format.
     * @return an instance of {@link JFormattedTextField} with the specified
     *         mask applied. If the mask is invalid, a JFormattedTextField
     *         without any formatting is returned.
     */
    public static final JFormattedTextField createMaskedTextField(String mask) {
        JFormattedTextField f;
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter(mask);
            f = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            f = new JFormattedTextField();
        }
        return f;
    }

    /**
     * Determines if a text input component is populated and not equal to
     * spaces.
     * 
     * @param comp
     *            the component to be evaluated which is of type
     *            {@link JTextComponent}
     * @return true when <i>comop</i> is populated with data other spaces; false
     *         when the component contains spaces or is null
     */
    public static final boolean isTextFieldPopulated(JTextComponent comp) {
        if (comp == null) {
            return false;
        }
        if (comp.getText().equals(RMT2String.spaces(comp.getText().length()))) {
            return false;
        }
        return true;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * 
     * @param path
     * @return
     */
    public static final ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = RMT2SwingUtil.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
