package com.balaji.calculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.balaji.calculator.model.ItemModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PriceDetailFrag extends Fragment implements TextWatcher {

    @BindView(R.id.edt_card_amount)
    EditText edt_card_amount;

    @BindView(R.id.edt_cash_amount)
    EditText edt_cash_amount;

    @BindView(R.id.edt_check_amount)
    EditText edt_check_amount;

    @BindView(R.id.edt_oldgold_amount)
    EditText edt_oldgold_amount;

    @BindView(R.id.edt_oldgold_weight)
    EditText edt_oldgold_weight;

    @BindView(R.id.edt_card_charge)
    EditText edt_card_charge;

    @BindView(R.id.txt_items_total)
    TextView txt_items_total;

    @BindView(R.id.txt_full_total)
    TextView txt_full_total;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    String cust_name="",customare_name="",bill_date="",gst="",finaltotaltext="";
    Double sub_total,finaltotal;
    List<ItemModel>list_item;

    public PriceDetailFrag() {
    }

    public PriceDetailFrag(String customare_name,String cust_name, String bill_date, Double sub_total, String gst, String finaltotaltext, Double finaltotal, List<ItemModel> list_item) {
        this.customare_name = customare_name;
        this.cust_name=cust_name;
        this.bill_date=bill_date;
        this.sub_total=sub_total;
        this.gst=gst;
        this.finaltotaltext=finaltotaltext;
        this.finaltotal=finaltotal;
        this.list_item=list_item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pricedetail_frag, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        txt_items_total.setText(finaltotaltext);

        edt_card_amount.addTextChangedListener(this);
        edt_cash_amount.addTextChangedListener(this);
        edt_check_amount.addTextChangedListener(this);
        edt_oldgold_amount.addTextChangedListener(this);
        edt_oldgold_weight.addTextChangedListener(this);
        edt_card_charge.addTextChangedListener(this);
    }

    @OnClick(R.id.img_share)
    public void onShareClick() {

            try {

                Document document = new Document(PageSize.A4);
                File file_dir = new File(getActivity().getFilesDir() + "/GCalc/pdfs");
                if (!file_dir.exists())
                    file_dir.mkdirs();


                try {
                    File tempFolder = new File(getActivity().getFilesDir() + "/GCalc/pdfs");
                    for (File f : tempFolder.listFiles())
                        f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String file_name = getActivity().getFilesDir() + "/GCalc/pdfs/" + System.currentTimeMillis() + ".pdf";
                PdfWriter.getInstance(document, new FileOutputStream(file_name)); //  Change pdf's name.
                document.open();



                Paragraph paragraph = new Paragraph("Estimate", catFont);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                Paragraph paragraph_sp_name = new Paragraph("Name : "+customare_name, normalFont);
                paragraph_sp_name.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph_sp_name);

                Paragraph paragraph_name = new Paragraph("Sales Person :"+cust_name, normalFont);
                paragraph_name.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraph_name);

                Paragraph paragraph_date = new Paragraph(bill_date, normalFont);
                paragraph_date.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph_date);

                Paragraph blank_line = new Paragraph();
                addEmptyLine(blank_line, 2);
                document.add(blank_line);

                PdfPTable table = new PdfPTable(9);

                // t.setBorderColor(BaseColor.GRAY);
                // t.setPadding(4);
                // t.setSpacing(4);
                // t.setBorderWidth(1);

                PdfPCell c1 = new PdfPCell(new Phrase("Item Name", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Net Wt", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Rate", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Gold Amt", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("L Rate", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("L Amt", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Other 1", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Other 2", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Total", headerFont));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c1.setBorder(Rectangle.NO_BORDER);
                table.addCell(c1);

                table.setHeaderRows(1);

                double tmpNtWt = 0.0,tmpItemPrice =0.0,tmpTotalMaking = 0.0,tmpOther1 = 0.0 ,tmpOther2 = 0.0;

                for (int i = 0; i < list_item.size(); i++)
                {

                    PdfPCell c2 = new PdfPCell(new Phrase(list_item.get(i).item_name,headerFont));
                    c2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    tmpNtWt+= Double.parseDouble(list_item.get(i).net_weight);
                    c2 = new PdfPCell(new Phrase(list_item.get(i).net_weight,headerFont));
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);


                    if (list_item.get(i).selected_carat.contains("18"))
                    {
                        c2 = new PdfPCell(new Phrase(list_item.get(i).carat_18));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c2.setBorder(Rectangle.NO_BORDER);
                        table.addCell(c2);

                    } else if (list_item.get(i).selected_carat.contains("22")){
                        c2 = new PdfPCell(new Phrase(list_item.get(i).carat_22));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c2.setBorder(Rectangle.NO_BORDER);
                        table.addCell(c2);

                    }
                    else
                    {
                        c2 = new PdfPCell(new Phrase(list_item.get(i).silver));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c2.setBorder(Rectangle.NO_BORDER);
                        table.addCell(c2);
                    }

                    tmpItemPrice+=Double.parseDouble(list_item.get(i).item_price);
                    c2 = new PdfPCell(new Phrase(list_item.get(i).item_price));
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    c2 = new PdfPCell(new Phrase(list_item.get(i).making_charge));
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    tmpTotalMaking+=Double.parseDouble(list_item.get(i).making_total);
                    c2 = new PdfPCell(new Phrase(list_item.get(i).making_total));
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    if (list_item.get(i).other1.isEmpty())
                    {
                        c2 = new PdfPCell(new Phrase("-"));
                     }

                    else
                    {
                        c2 = new PdfPCell(new Phrase(list_item.get(i).other1));
                        tmpOther1+=Double.parseDouble(list_item.get(i).other1);
                    }

                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    if (list_item.get(i).other2.isEmpty())
                    {
                        c2 = new PdfPCell(new Phrase("-"));

                    }
                    else
                    {
                        c2 = new PdfPCell(new Phrase(list_item.get(i).other2));
                        tmpOther2+=Double.parseDouble(list_item.get(i).other2);
                    }

                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);

                    c2 = new PdfPCell(new Phrase(list_item.get(i).total));
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c2.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c2);


                }
                //footer
                DecimalFormat df3 =new DecimalFormat("#0.000");
                DecimalFormat df2 =new DecimalFormat("#0.00");
                PdfPCell c3 = new PdfPCell(new Phrase("Total ",headerFont));//Itemname
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase(""+df3.format(tmpNtWt),headerFont));//Nt Amt
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase("-"));//Gold Rate
                c3.setBorder(Rectangle.NO_BORDER);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase(""+df2.format(tmpItemPrice),headerFont));//Amt
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase("-"));//Macking
                c3.setBorder(Rectangle.NO_BORDER);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase(""+df2.format(tmpTotalMaking),headerFont));//total Amt
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase(""+df2.format(tmpOther1),headerFont));//o1
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase(""+df2.format(tmpOther2),headerFont));//o2
                c3.setBorder(Rectangle.TOP);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);
                c3 = new PdfPCell(new Phrase("-"));//total amt
                c3.setBorder(Rectangle.NO_BORDER);
                c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c3);




                table.setTotalWidth(PageSize.A4.getWidth() - 57);
                table.setLockedWidth(true);
                document.add(table);

                //Paragraph blank_line3 = new Paragraph();
                //addEmptyLine(blank_line3, 1);
                //document.add(blank_line3);

                Paragraph para_subtotal = new Paragraph("Sub Total : " + String.format("%.2f", sub_total), headerFont);
                para_subtotal.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_subtotal);

                Paragraph para_gst = new Paragraph(gst, headerFont);
                para_gst.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_gst);

                Paragraph para_total = new Paragraph(finaltotaltext, headerFont);
                para_total.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_total);

                if(!txt_full_total.getText().toString().isEmpty())
                {
                    if(!edt_card_amount.getText().toString().isEmpty())
                    {
                        Paragraph extra = new Paragraph("Card Amount : -"+edt_card_amount.getText().toString().trim(), headerFont);
                        extra.setAlignment(Element.ALIGN_RIGHT);
                        document.add(extra);
                    }

                    if(!edt_card_charge.getText().toString().isEmpty())
                    {
                        Paragraph extra = new Paragraph("Card Charge : +"+edt_card_charge.getText().toString().trim(), headerFont);
                        extra.setAlignment(Element.ALIGN_RIGHT);
                        document.add(extra);
                    }

                    if(!edt_check_amount.getText().toString().isEmpty())
                    {
                        Paragraph extra = new Paragraph("Check Amount : -"+edt_check_amount.getText().toString().trim(), headerFont);
                        extra.setAlignment(Element.ALIGN_RIGHT);
                        document.add(extra);
                    }

                    if(!edt_cash_amount.getText().toString().isEmpty())
                    {
                        Paragraph extra = new Paragraph("Cash Amount : -"+edt_cash_amount.getText().toString().trim(), headerFont);
                        extra.setAlignment(Element.ALIGN_RIGHT);
                        document.add(extra);
                    }

//                    if(!edt_oldgold_weight.getText().toString().isEmpty())
//                    {
//                        Paragraph extra = new Paragraph("Old gold weight : "+edt_oldgold_weight.getText().toString().trim(), headerFont);
//                        extra.setAlignment(Element.ALIGN_RIGHT);
//                        document.add(extra);
//                    }

                    if(!edt_oldgold_amount.getText().toString().isEmpty())
                    {
                        Paragraph extra = new Paragraph("Old gold amount("+edt_oldgold_weight.getText().toString().trim()+ "): -"+edt_oldgold_amount.getText().toString().trim(), headerFont);
                        extra.setAlignment(Element.ALIGN_RIGHT);
                        document.add(extra);
                    }

                    Paragraph extra = new Paragraph(txt_full_total.getText().toString().trim(), headerFont);
                    extra.setAlignment(Element.ALIGN_RIGHT);
                    document.add(extra);
                }

                document.close();

                File file_pdf = new File(file_name);

                if (file_pdf.exists()) {
                    Uri uri = FileProvider.getUriForFile(getActivity(), "com.balaji.calculator", file_pdf);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(share, "Share"));
                }
                getFragmentManager().popBackStack();
//                Fragment fragment=getFragmentManager().findFragmentByTag("HomeFragment");
//                if(fragment!=null)
//                {
//                    HomeFragment homeFragment=(HomeFragment)fragment;
//                    homeFragment.onDeleteAll();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
         Double full_total=0.0;
         Double card=0.0,cash=0.0,check=0.0,old=0.0,charge=0.0;

         if(!edt_card_amount.getText().toString().isEmpty())
            card=Double.valueOf(edt_card_amount.getText().toString().trim());

        if(!edt_cash_amount.getText().toString().isEmpty())
            cash=Double.valueOf(edt_cash_amount.getText().toString().trim());

        if(!edt_check_amount.getText().toString().isEmpty())
            check=Double.valueOf(edt_check_amount.getText().toString().trim());

        if(!edt_oldgold_amount.getText().toString().isEmpty())
            old=Double.valueOf(edt_oldgold_amount.getText().toString().trim());

        if(!edt_card_charge.getText().toString().isEmpty())
            charge=Double.valueOf(edt_card_charge.getText().toString().trim());

        full_total=(finaltotal-card-cash-check-old)+charge;
        if(full_total!=0)
        txt_full_total.setText("Final total : "+String.format("%.2f", full_total));
        else
        txt_full_total.setText("");

    }
}
