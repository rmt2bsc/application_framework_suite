package com.util;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.api.config.AppPropertyPool;

/**
 * Utility class that provides common functionality for resource
 * allocation/deallocation and general management regarding the entire
 * application.
 * 
 * @author rterrell
 *
 */
public class AppUtil {

    private static Logger logger;

    private static Properties APP_PROPS;

    private static final String APP_ICON_KEY = "appIcon";

    /**
     * The properties file containing the application's configuration.
     */
    public static final String CONFIG_COMMON_FILE = "/application.properties";

    /**
     * The default font size that will be applied to every DataGrid and dialog
     * within the application.
     */
    public static final int DEFAULT_FONT_SIZE = 15;

    /**
     * The property key to locate the value of the application title from within
     * the application configuration.
     */
    public static final String PROP_APP_TITLE = "apptitle";

    /**
     * Creates a AppUtil object.
     */
    public AppUtil() {
        return;
    }

    /**
     * Globally sets the font for the application with values obtained from
     * application.properties.
     */
    public void setDefaultFont() {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                Font f = getDataGridRowFont();
                UIManager.put(key, f);
            }
        }
    }

    /**
     * Uses the specified component, <i>comp</i>, to display a message anchored
     * by its parent to the user.
     * <p>
     * The HTML paragraph tag, &lt;P$gt;, is used to enclose the message text so
     * that the JLabel component is forced to wrap excessively long message
     * text.
     * 
     * @param comp
     *            an instance of {@link JLabel} that is used to display a
     *            message on its parent.
     * @param message
     *            The message to be displayed.
     * @param error
     *            When true, the font color of the message is RED. Otherwise,
     *            the font color is BLACK.
     */
    public static final void showMessage(JLabel comp, String message,
            boolean error) {
        if (comp == null) {
            logger.error("The intended component to display user message is invalid or null");
            return;
        }

        // Use HTML paragraph tags to make text wrap in the JLable component
        message = "<html><p>" + message + "</p></html>";
        comp.setText(message);
        comp.setVisible(true);

        // Set the font
        Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        comp.setFont(f);

        if (error) {
            comp.setForeground(Color.RED);
        }
        else {
            comp.setForeground(Color.BLACK);
        }
    }

    /**
     * Obtains a icon image for the application.
     * 
     * @return {@link ImageIcon}
     */
    public static ImageIcon getAppIcon() {
        URL imgURL;
        String iconPath = AppPropertyPool.getProperty(AppUtil.APP_ICON_KEY);
        if (iconPath == null) {
            logger.warn("Application Icon was not found!");
            return null;
        }
        imgURL = AppUtil.class.getResource(iconPath);
        ImageIcon img = null;
        if (imgURL != null) {
            img = new ImageIcon(imgURL, "Application Icon");
        }
        return img;
    }

    /**
     * Return a Font instance that will be applied to each data row of the
     * DataGrid component.
     * <p>
     * The data used to create the Font instance is obtained from the
     * application.properties file. The keys for the font family, size, and
     * style are <i>ListDataFont</i>, <i>ListDataFontSize</i>, and
     * <i>ListDataFontStyle</i>, respectively.
     * 
     * @return An instance of {@link Font}
     */
    public static Font getDataGridRowFont() {
        String fontName = AppPropertyPool.getProperty("ListDataFont");
        String fontSizeStr = AppPropertyPool.getProperty("ListDataFontSize");
        String fontStyleStr = AppPropertyPool.getProperty("ListDataFontStyle");
        int fontSize;
        int fontStyle;

        if (fontName == null) {
            fontName = Font.MONOSPACED;
        }
        try {
            fontSize = Integer.parseInt(fontSizeStr);
        } catch (NumberFormatException e) {
            fontSize = DEFAULT_FONT_SIZE;
        }

        try {
            fontStyle = Integer.parseInt(fontStyleStr);
        } catch (NumberFormatException e) {
            fontStyle = Font.PLAIN;
        }

        return new Font(fontName, fontStyle, fontSize);
    }

    /**
     * Return a Font instance that will be applied to table header of the
     * DataGrid component.
     * <p>
     * The data used to create the Font instance is obtained from the
     * application.properties file. The keys for the font family, size, and
     * style are <i>ListHeaderFont</i>, <i>ListHeaderFontSize</i>, and
     * <i>ListHeaderFontStyle</i>, respectively.
     * 
     * @return An instance of {@link Font}
     */
    public static Font getDataGridHeaderFont() {
        String fontName = AppPropertyPool.getProperty("ListHeaderFont");
        String fontSizeStr = AppPropertyPool.getProperty("ListHeaderFontSize");
        String fontStyleStr = AppPropertyPool
                .getProperty("ListHeaderFontStyle");
        int fontSize;
        int fontStyle;

        if (fontName == null) {
            fontName = Font.MONOSPACED;
        }
        try {
            fontSize = Integer.parseInt(fontSizeStr);
        } catch (NumberFormatException e) {
            fontSize = DEFAULT_FONT_SIZE;
        }

        try {
            fontStyle = Integer.parseInt(fontStyleStr);
        } catch (NumberFormatException e) {
            fontStyle = Font.BOLD;
        }

        return new Font(fontName, fontStyle, fontSize);
    }
}
