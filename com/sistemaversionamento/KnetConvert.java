package com.sistemaversionamento;


import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.codec.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class KnetConvert
{
  public KnetConvert(int remoteHandle) {}
  
  public static void Stringtopdf(String input, String out)
    throws DocumentException, FileNotFoundException, IOException
  {
    InputStream input2 = new ByteArrayInputStream(input.getBytes("ISO-8859-1"));
    OutputStream out2 = new FileOutputStream(out);
    Tidy tidy = new Tidy();
    
    tidy.setShowWarnings(false);
    org.w3c.dom.Document doc = tidy.parseDOM(input2, null);
    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocument(doc, null);
    renderer.layout();
    renderer.createPDF(out2);
    out2.close();
  }
  
  public static void Urltopdf(String url, String target)
    throws Exception
  {
    Reader htmlreader;
    
    if ((url.startsWith("http://")) || (url.startsWith("https://")))
    {
      URL site = new URL(url);
      htmlreader = new InputStreamReader(site.openStream());
    }
    else
    {
      htmlreader = new InputStreamReader(new FileInputStream(url));
    }
    com.lowagie.text.Document pdfDocument = new com.lowagie.text.Document();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter.getInstance(pdfDocument, baos);
    pdfDocument.open();
    StyleSheet styles = new StyleSheet();
    styles.loadTagStyle("body", "font", "Bitstream Vera Sans");
    
    @SuppressWarnings("rawtypes")
	ArrayList arrayElementList = HTMLWorker.parseToList(htmlreader, styles);
    
    
    for (int i = 0; i < arrayElementList.size(); i++)
    {
      Element e = (Element)arrayElementList.get(i);
      pdfDocument.add(e);
    }
    pdfDocument.close();
    byte[] bs = baos.toByteArray();
    
    String pdfBase64 = Base64.encodeBytes(bs);
    System.out.println(pdfBase64);
    File pdfFile = new File(target);
    FileOutputStream out = new FileOutputStream(pdfFile);
    out.write(bs);
    out.close();
  }
}
