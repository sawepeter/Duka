package com.example.devsawe.duka.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.devsawe.duka.Model.CartModel;
import com.example.devsawe.duka.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Receipt {

    File myFile = null;
    String fle;
    private String LOG_TAG = "receiptlog";
    private Context context;

    public Receipt(Context context) {
        this.context = context;
    }

    public File re(ArrayList<CartModel> cartitems, double total, double cashIn, String shopdetails, String attendername, String rn) throws DocumentException, FileNotFoundException{

        File pdffolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS),"DukaReceipts");
        if (!pdffolder.exists()) {
            pdffolder.mkdir();
            Log.i(LOG_TAG, "pdf Directory created");
        }
        Date date = new Date();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdffolder + timestamp +".pdf");
        OutputStream outputStream = new FileOutputStream(myFile);
        BaseColor baseColor = new BaseColor(84,141,212);
        Rectangle pageSize = new Rectangle(PageSize.A5);//determines the size of the receipt
        pageSize.setBackgroundColor(baseColor);
        Document document = new Document(pageSize);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();

        HeaderFooterPageEvent event = new HeaderFooterPageEvent(attendername, rn, date);
        pdfWriter.setPageEvent(event);
        PdfContentByte canvas = pdfWriter.getDirectContentUnder();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.download);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            Image image = null;
            try {
                image = Image.getInstance(stream.toByteArray());
                image.setAbsolutePosition(0,0);
                image.scaleAbsolute(PageSize.A5);
            }catch (BadElementException m){

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            canvas.saveState();
            PdfGState state = new PdfGState();
            state.setFillOpacity(0.1f);

            canvas.addImage(image);
            canvas.restoreState();
        } catch (OutOfMemoryError error){
            error.printStackTrace();
        }

        PdfPTable header = new PdfPTable(3);
        header.setWidths(new int[]{1, 4, 1});
        header.addCell(createTextCell(""));
        header.addCell(createTextCell(shopdetails));
        header.addCell(createTextCell(""));


        PdfPTable RecieptTitle = new PdfPTable(3);
        RecieptTitle.setWidths(new int[]{1, 1, 1});
        RecieptTitle.setSpacingBefore(8);
        RecieptTitle.setSpacingAfter(6);
        RecieptTitle.getDefaultCell().setBorderWidthLeft(0);
        RecieptTitle.getDefaultCell().setBorderWidthRight(0);
        RecieptTitle.addCell("Receipt");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String d = simpleDateFormat.format(date);
        RecieptTitle.addCell("" + d);
        RecieptTitle.setSpacingAfter(8);
        RecieptTitle.addCell("RN : 1267");

        PdfPTable RecieptitemsTitles = new PdfPTable(4);
        RecieptitemsTitles.setWidths(new int[]{2, 2, 2, 2});
        RecieptitemsTitles.addCell(creatTextCellTitles("Item"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Quantity"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Price"));
        RecieptitemsTitles.addCell(creatTextCellTitles("Total"));

        for (int a = 0; a < cartitems.size(); a++) {
            RecieptitemsTitles.addCell(createTextCellcolor(cartitems.get(a).getCartname(), a));

            RecieptitemsTitles.addCell(createTextCellcolor("",a));
            RecieptitemsTitles.addCell(createTextCellcolor("",a));
            RecieptitemsTitles.addCell(createTextCellcolor("",a));
            RecieptitemsTitles.addCell(createTextCellcolor("",a));
            RecieptitemsTitles.addCell(createTextCellcolor("",a));


            RecieptitemsTitles.addCell(createTextCellcolor(cartitems.get(a).getCartquantity(), a));
            RecieptitemsTitles.addCell(createTextCellcolor(cartitems.get(a).getCarttotal(), a));
            //the setter of this price has not yet been set correctly.
            RecieptitemsTitles.addCell(createTextCellcolor(cartitems.get(a).getCartsellingprice(), a));

        }


        PdfPTable totals = new PdfPTable(3);
        totals.setWidths(new int[]{1, 1, 1});
        totals.setSpacingBefore(8);
        totals.setSpacingAfter(6);
        totals.getDefaultCell().setBorderWidthTop(0);
        totals.getDefaultCell().setBorderWidthBottom(0);
        totals.getDefaultCell().setBorderWidthLeft(0);
        totals.getDefaultCell().setBorderWidthRight(0);

        totals.addCell("");
        totals.addCell("Total");
        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + total);

        totals.addCell("");
        totals.addCell("Cash In");
        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + cashIn);

        totals.addCell("");
        totals.addCell("Balance");
        totals.setSpacingAfter(8);
        totals.addCell("Ksh : " + String.valueOf(cashIn - total));


        document.add(header);
        document.add(RecieptTitle);
        document.add(RecieptitemsTitles);
        document.add(totals);
        document.close();

        return myFile;
    }

    public PdfPCell createTextCell(String text){

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(text);
        cell.addElement(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Rectangle.NO_BORDER);

        return cell;
    }

    public PdfPCell creatTextCellTitles(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Paragraph p = new Paragraph();
        p.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD));
        p.add(text);
        cell.addElement(p);

        return cell;
    }

    public PdfPCell createTextCellcolor(String text, int c) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        if (c % 2 == 0) {
            Paragraph p  = new Paragraph();
            p.add(text);
            cell.addElement(p);
        } else {
            Paragraph p = new Paragraph();
            p.add(text);
            cell.addElement(p);
        }
        return cell;
    }



}
