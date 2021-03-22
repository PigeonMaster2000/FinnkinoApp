package com.example.ekaappi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/*
    Kysymykset:
    Olisiko tämän voinut tehdä siten, että 'theatreInfo' sekä 'Theatres' luokkien ei olisi tarvinnut molempien olla
    omissa tiedostoissaan?

    Miten olisin voinut tehdä toteutuksen käyttämättä 'spinnerList' listaani? olisinki voinut ottaa teattereiden
    nimet vain 'TheatresList' listasta suoraan?

    Onko mahdollista säilyttää tekstiä muuten kuin laittamalla stringgiin tai tiedostoon tallentamalla?
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textBox;
    Theatres theatreList;
    Library library;
    Spinner spin;
    ArrayList<String> spinnerList;
    //'currentday' kertoo sen päivän minkä kanssa töyskennellään
    String printString, currentday, url, movieName =null;
    Calendar date, finalTime, firstTime;
    DocumentBuilder builder;
    int theatreId;

    EditText firstTimeInputBox, lastTimeInputBox, currentTimeInputBox, moveInputBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theatreId = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        library = new Library();
        theatreList = new Theatres();

        //spinnner juttuja
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //tehdään päivä
        date = Calendar.getInstance();


        //textView
        textBox = (TextView)findViewById(R.id.contentText);

        //text input
        firstTimeInputBox =  (EditText) findViewById(R.id.firstTime);
        lastTimeInputBox =  (EditText) findViewById(R.id.lastTime);
        currentTimeInputBox = (EditText) findViewById(R.id.curentTime);
        moveInputBox = (EditText) findViewById(R.id.movie);
    }

    public void readXML(View v){
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();

            // Käytän hetkellisesti tätä nodelistaa ja muutan sielä olevat elemntit arraylistiin
            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            //En tiedä miten voisin käyttää vain nimielementtejä 'theatreList.list' listasta, joten teen siksi uuden listan josta luon spinnerin
            spinnerList = new ArrayList<String>();
            for(int i = 0; i<nList.getLength(); i++){
                Node node = nList.item(i);
                //tämä printti on debuggausta varten
                System.out.println("Current Element: "+ node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    theatreList.addElement(element.getElementsByTagName("Name").item(0).getTextContent(),
                            Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent()));

                    spinnerList.add(element.getElementsByTagName("Name").item(0).getTextContent());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#########moi###########");
            setSpinner();
        }

    }

    private void setSpinner(){
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, spinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    private void updateText(int id){
        int amountOfCycles = 9, tempId;
        Calendar tempDate = Calendar.getInstance();
        String tempDateString;
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'");
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        currentday = formatter.format(date.getTime());

        if ((id == 0)&&(movieName == null)){
            //eli jos ei ole valittu teatteria, eikä ole annettu elokuvaa
            return;
        }
        printString = "Here is a list of all the movies I found!\n\n";
        if (movieName == null) {
            //on valittu teatteri, muttei elokuvaa
            System.out.println("TEATTERI VALITTU, MUTTA EI ELOKUVAA");
            url = ("https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + currentday);
            amountOfCycles = 1;
        }
        System.out.println("MOVIE NAME: "+movieName);
        for (int j = 0; j < amountOfCycles; j++) {
            if (amountOfCycles == 9){
                //eli jos ei katsota vain yhden teatterin näytöksiä, vaan kaikkien
                if (j == 0){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1014 + "&dt=" + currentday);
                }
                if (j == 1){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1015 + "&dt=" + currentday);
                }
                if (j == 2){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1016 + "&dt=" + currentday);
                }
                if (j == 3){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1017 + "&dt=" + currentday);
                }
                if (j == 4){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1041 + "&dt=" + currentday);
                }
                if (j == 5){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1018 + "&dt=" + currentday);
                }
                if (j == 6){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1019 + "&dt=" + currentday);
                }
                if (j == 7){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1021 + "&dt=" + currentday);
                }
                if (j == 8){
                    url = ("https://www.finnkino.fi/xml/Schedule/?area=" + 1022 + "&dt=" + currentday);
                }
                System.out.println("Iso loop!");
            }
            try {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(url);
                doc.getDocumentElement().normalize();
                // Käytän hetkellisesti tätä nodelistaa
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

                for (int i = 0; i < nList.getLength(); i++) {
                    System.out.print(" - ");
                    Node node = nList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        //'printString' ei ole tietorakenne eikä säilö dataa, vaan on se teksti mitä tulostetaan näytölle
                        // Tässä tapauksessa se kertoo kaikki elokuvat jne
                        tempDateString = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();

                        tempDate.setTime(formatter2.parse(tempDateString));

                        if ((firstTime == null) && (finalTime == null)) {
                            if ((amountOfCycles == 1) ||
                                    (amountOfCycles == 9) && (element.getElementsByTagName("Title").item(0).getTextContent().equals(movieName))) {
                                //jos ei ole valittuu kellonaika parametreja
                                //JA jos nimi filtteri on käytössä niin nimet täsmäävät
                                printString += element.getElementsByTagName("dttmShowStart").item(0).getTextContent() + " - ";
                                printString += element.getElementsByTagName("Title").item(0).getTextContent() + " - ";
                                printString += element.getElementsByTagName("Theatre").item(0).getTextContent() + "\n\n";
                            }
                        } else {
                            if ((tempDate.compareTo(firstTime) > 0) && (tempDate.compareTo(finalTime) < 0)) {
                                if (((amountOfCycles == 1) ||
                                        (amountOfCycles == 9) && (element.getElementsByTagName("Title").item(0).getTextContent().equals(movieName)))) {
                                    //jos on valittu kellonaika parametrita
                                    // JA jos nimi filtteri on käytössä niin nimet täsmäävät
                                    printString += element.getElementsByTagName("dttmShowStart").item(0).getTextContent() + " - ";
                                    printString += element.getElementsByTagName("Title").item(0).getTextContent() + " - ";
                                    printString += element.getElementsByTagName("Theatre").item(0).getTextContent() + "\n\n";
                                }
                            }
                        }
                    }
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Tässä on url: "+url);
        textBox.setText(printString);
    }

    public void updateParameters(View v){
        String tempNow = currentTimeInputBox.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        System.out.println("TÄSSÄ ON TEMPNOW _>"+tempNow);
        String tempLast = lastTimeInputBox.getText().toString();
        movieName =  moveInputBox.getText().toString();
        if ((movieName.length() == 0)){
            movieName = null;
        }

        if (tempLast == "after this"){
            tempLast = null;
        }
        String tempFirst = firstTimeInputBox.getText().toString();
        if (tempFirst == "before this"){
            tempFirst = null;
        }

        date = Calendar.getInstance();
        finalTime = Calendar.getInstance();
        firstTime = Calendar.getInstance();

        try {
            date.setTime(sdf.parse(tempNow));
        } catch (ParseException e) {
            date = Calendar.getInstance();
            tempNow = sdf.format(date.getTime());
            e.printStackTrace();
        }
        try {
            finalTime.setTime(sdf2.parse(tempNow+" "+tempLast));
        } catch (ParseException e) {
            finalTime = null;
            e.printStackTrace();
        }
        try {
            firstTime.setTime(sdf2.parse(tempNow+" "+tempFirst));
        } catch (ParseException e) {
            firstTime = null;
            e.printStackTrace();
        }
        updateText(theatreId);
        System.out.println("Tässä on hieman arvoja:");
        System.out.println("First ->"+firstTime+"  Last ->"+finalTime+"movie ->"+movieName+"date ->"+tempNow);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int tempPos = position;
        if (position != 0){
            theatreId = theatreList.list.get(tempPos).id;
            updateText(theatreId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}