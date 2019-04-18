package com.example.devsawe.duka.Activities;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.Date;

public class HeaderFooterPageEvent implements PdfPageEvent {
    private String officerInCharge;
    private String receiptNo;
    private Date date;


    public HeaderFooterPageEvent(String officerInCharge, String receiptNo, Date date) {
        this.officerInCharge = officerInCharge;
        this.receiptNo = receiptNo;
        this.date = date;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(" R.No :" + receiptNo + "\n\n"), 30, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("" + date + "\n\n"), 550, 800, 0);

    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(" R.NO :" + receiptNo + "\n\n"), 30, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("" + date + "\n\n"), 550, 800, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Served By : " + officerInCharge), 110, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Sign : _________________       " + document.getPageNumber()), 550, 30, 0);
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Served By : " + officerInCharge), 110, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Sign : _________________       " + document.getPageNumber()), 550, 30, 0);
    }

    @Override
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {

    }

    @Override
    public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {

    }

    @Override
    public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {

    }
}
