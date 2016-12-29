package com.printing;

import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.Serializable;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.swing.text.JTextComponent;

import com.SystemException;
import com.util.RMT2File;

/**
 * This is a utility for printing the contents of the Swing components
 * 
 * @author roy.terrell
 *
 */
public class PrintUtility {

    private static final String PRINT_DIALOG_FILENAME = "print_prefs.ser";

    private static final String PRINTER_NAME_FILENAME = "printer.ser";

    /**
     * Sends the contents of a text document to the default printer.
     * <p>
     * The contents of a text document can be printed in its entirety or as a
     * range of pages specified by the user.
     * 
     * @param comp
     *            The target text component. This component is used to obtain
     *            it's implementation of the {@link Printable} iterface, which
     *            in turn is called by the printng system to render a page.
     * @param pages
     *            A String array separating the contents of <i>comp</i> into
     *            pages. Each element of the array represents a page of the
     *            document where element zero equals page #1, element one equal
     *            page #2, and so on.
     * @param attrs
     *            A Set of print attributes used to govern the print job.
     * @throws ComponentPrintingException
     *             Error occured printing a given page.
     */
    public static void print(JTextComponent comp, String[] pages,
            PrintRequestAttributeSet attrs) throws ComponentPrintingException {
        // Determine Print range, if any
        int printRangeLower = 1;
        int printRangeHigher = pages.length;
        Attribute attr = attrs.get(PageRanges.class);
        if (attr != null) {
            if (attr instanceof PageRanges) {
                PageRanges pr = (PageRanges) attr;
                int[][] range = pr.getMembers();
                printRangeLower = range[0][0];
                printRangeHigher = range[0][1];
                attrs.remove(attr);
            }
        }

        // Get user's previous print service (printer) selection
        PrintService printer = PrintUtility.getPreviousPrinter();

        // Create a job for each page that is to be printed
        Printable printable = null;
        try {
            for (int page = (printRangeLower - 1); page < printRangeHigher; page++) {
                PrinterJob pjob = PrinterJob.getPrinterJob();

                // Assing user's previous printer selection, if available.
                if (printer != null) {
                    pjob.setPrintService(printer);
                }
                PageFormat pf = pjob.defaultPage();
                Paper paper = new Paper();
                double margin = 0;
                paper.setImageableArea(margin, margin, paper.getWidth()
                        - margin * 2, paper.getHeight() - margin * 2);
                pf.setPaper(paper);
                comp.setText(pages[page]);
                printable = comp.getPrintable(null, null);
                pjob.setPrintable(printable, pf);
                pjob.print(attrs);
            }
        } catch (PrinterException e) {
            String msg = "Error occurred printing component "
                    + printable.getClass().getName();
            throw new ComponentPrintingException(msg, e);
        }
    }

    public static PrintRequestAttributeSet getUserPrintAttributes(
            boolean portrait, Insets insets) throws ComponentPrintingException {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet attrs = null;
        String serialObjFilename = RMT2File
                .getSerialLocation(PRINT_DIALOG_FILENAME);

        // Get previous printer selections
        if (RMT2File.verifyFile(serialObjFilename) == 1) {
            try {
                attrs = (HashPrintRequestAttributeSet) RMT2File
                        .deSerializeObject(PRINT_DIALOG_FILENAME);
                PrintService ps = PrintUtility.getPreviousPrinter();
                if (ps != null) {
                    try {
                        pjob.setPrintService(ps);
                    } catch (PrinterException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SystemException e) {
                attrs = new HashPrintRequestAttributeSet();
            }
        }
        else {
            pjob = PrinterJob.getPrinterJob();
            // create an attribute set to store attributes from the print dialog
            attrs = new HashPrintRequestAttributeSet();

            // Set default margins for portait orientation
            if (insets == null) {
                insets = new Insets(13, 13, 13, 13);
            }
            float leftMargin = insets.left;
            float rightMargin = insets.right;
            float topMargin = insets.top;
            float bottomMargin = insets.bottom;

            attrs.add(new Copies(1));
            if (portrait) {
                attrs.add(OrientationRequested.PORTRAIT);
            }
            else {
                attrs.add(OrientationRequested.LANDSCAPE);
                leftMargin = insets.top;
                rightMargin = insets.bottom;
                topMargin = insets.right + 12;
                bottomMargin = insets.left + 12;
            }

            attrs.add(MediaSizeName.ISO_A4);
            MediaSize mediaSize = MediaSize.ISO.A4;
            float mediaWidth = mediaSize.getX(Size2DSyntax.MM);
            float mediaHeight = mediaSize.getY(Size2DSyntax.MM);
            float width = (mediaWidth - leftMargin - rightMargin);
            float height = (mediaHeight - topMargin - bottomMargin);
            MediaPrintableArea mpa = new MediaPrintableArea(leftMargin,
                    topMargin, width, height, Size2DSyntax.MM);
            attrs.add(mpa);
        }

        try {
            boolean okPrint = pjob.printDialog(attrs);
            if (okPrint) {
                // Before returning, persist user's printer attribute selections
                RMT2File.serializeObject((Serializable) attrs,
                        PRINT_DIALOG_FILENAME);
                PrintService ps = pjob.getPrintService();
                RMT2File.serializeObject(ps.getName(), PRINTER_NAME_FILENAME);
                return attrs;
            }
            return null;
        } catch (Exception e) {
            throw new ComponentPrintingException(e);
        }
    }

    private static final PrintService getPreviousPrinter() {
        String prevPrinterName = null;
        try {
            prevPrinterName = (String) RMT2File
                    .deSerializeObject(PRINTER_NAME_FILENAME);
        } catch (SystemException e) {
            return null;
        }
        PrintService srvc[] = PrinterJob.lookupPrintServices();
        for (int ndx = 0; ndx < srvc.length; ndx++) {
            String name = srvc[ndx].getName();
            System.out.println(name);
            if (name.equals(prevPrinterName)) {
                return srvc[ndx];
            }
        }
        return null;
    }

} // end class

