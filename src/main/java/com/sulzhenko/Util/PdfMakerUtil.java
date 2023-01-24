package com.sulzhenko.Util;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.sulzhenko.DTO.ReportDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.sulzhenko.controller.Constants.PAGE;
import static com.sulzhenko.controller.Constants.REPORT;

public class PdfMakerUtil {
    private static final Logger logger = LogManager.getLogger(PdfMakerUtil.class);
    private static final String[] REPORT_CELLS = new String[]{"login", "number.activities", "total.time", "activity.name", "time.amount"};
    private final List<ReportDTO> report;
    private final ResourceBundle resourceBundle;
    public PdfMakerUtil(String locale, List<ReportDTO> report) {
        this.resourceBundle = getBundle(locale);
        this.report = report;
    }

    private Table getReportTable() {
        Table table = new Table(new float[]{4, 12, 6, 6, 6});
        table.setWidth(UnitValue.createPercentValue(100));
        addTableHeader(table);
        addUserTableRows(table);
        return table;
    }
    private void addTableHeader(Table table) {
        Stream.of(REPORT_CELLS)
                .forEach(columnTitle -> {
                    Cell header = new Cell();
                    header.setBackgroundColor(new DeviceRgb(220, 220, 220));
                    header.setBorder(new SolidBorder(1));
                    header.add(new Paragraph(new Text(resourceBundle.getString(columnTitle))));
                    table.addCell(header);
                });
    }
    private void addUserTableRows(Table table) {
        report.forEach(element ->
                {
                    int rawSpan = element.getActivitiesWithTime().size();
                    Cell cell = new Cell(rawSpan, 1).add(new Paragraph(element.getLogin()));
                    table.addCell(cell);
                    cell = new Cell(rawSpan, 1).add(new Paragraph(String.valueOf(element.getNumberOfActivities())));
                    table.addCell(cell);
                    cell = new Cell(rawSpan, 1).add(new Paragraph(String.valueOf(element.getTotalTime())));
                    table.addCell(cell);
                    for(UserActivityDTO raw: element.getActivitiesWithTime()){
                        table.addCell(raw.getActivityName());
                        table.addCell(String.valueOf(raw.getActivityTime()));
                    }
                }
        );
    }
    private static ResourceBundle getBundle(String locale) {
        String resources = "resources";
        if (locale.contains("_")) {
            int index = locale.indexOf("_");
            String lang = locale.substring(0, index);
            String country = locale.substring(index + 1);
            return ResourceBundle.getBundle(resources, new Locale(lang, country));
        } else {
            return ResourceBundle.getBundle(resources, new Locale(locale));
        }
    }
    public void getReportPDF(String name, String page, String currentPage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String file = "C:\\data\\temp\\tasks\\fp\\sulzhenko\\src\\main\\webapp\\" + name + ".pdf";
        PdfDocument pdfDoc;
        try {
            pdfDoc = new PdfDocument(new PdfWriter(file));
        } catch (FileNotFoundException e) {
            logger.fatal(e.getMessage());
            throw new ServiceException(e);
        }
        Document doc = new Document(pdfDoc);
        PdfFont font;
        try {
            font = PdfFontFactory.createFont("C:\\WINDOWS\\Fonts\\ARIAL.TTF");
            doc.setFont(font);
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            throw new ServiceException(e);
        }
        doc.add(new Paragraph(new Text(formatter.format(date))));
        doc.add(new Paragraph(new Text(getHead(page, currentPage)))
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph(new Text("\n")));
        doc.add(getReportTable());
        doc.close();
    }
    private String getHead(String page, String currentPage){
        if(page != null && currentPage != null){
            return resourceBundle.getString(REPORT).toUpperCase() + resourceBundle.getString(PAGE) + currentPage;
        } else if(page != null){
            return resourceBundle.getString(REPORT).toUpperCase() + resourceBundle.getString(PAGE) + 1;
        } else {
            return resourceBundle.getString(REPORT).toUpperCase();
        }
    }
}
