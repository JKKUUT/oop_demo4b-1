import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Luokkaa käytetään verkkokaupan tietojen tallentamiseen
 * ja lataamiseen tietovarastosta.
 *
 * @author Erkki
 */
public class VerkkokauppaIO {

    public static void main(String[] args) {
        // Tähän voi kirjoittaa koodia, jolla testata
        // kirjoitus- ja lukumetodien toimintaa helposti
        Tuote tamppooni = new Tuote("tamppooni", 10, 2);
        Tuote tampax = new Tuote("tampax", 2, 1);
        ArrayList<Tuote> to = new ArrayList<>();
        to.add(tamppooni);
        to.add(tampax);
        kirjoitaTuotteet(to, "tuotteet.csv");

        Asiakas atte = new Asiakas("12", "atte", 0);
        Asiakas janne = new Asiakas("2", "janne", 0);
        ArrayList<Asiakas> asia = new ArrayList<>();
        asia.add(atte);
        asia.add(janne);
        kirjoitaAsiakkaat(asia, "asiakkaat.csv");

        Tuote t = new Tuote("t", 2, 2);
        ArrayList<Tuote> tuot = new ArrayList<>();
        tuot.add(t);
        kirjoitaTuotteet(tuot, "tuotteet.csv");
    }

    private static final String EROTIN = ";";

    public static void kirjoitaTiedosto(String tiedostonNimi,
            String sisalto) {
        try (PrintWriter tiedosto = new PrintWriter(tiedostonNimi)) {
            tiedosto.write(sisalto);
        } catch (FileNotFoundException e) {
            System.out.println("Tapahtui virhe: " + e);
        }
    }

    /**
     * Lukee annetun nimisen tiedoston sisällön ja palauttaa sen listassa.
     * Jokainen listan alkio vastaa yhtä tiedoston riviä
     *
     * @param tiedostonNimi luettavan tiedoston nimi
     * @return tiedoston sisällön listana
     */
    public static ArrayList<String> lueTiedosto(String tiedostonNimi) {
        ArrayList<String> data = new ArrayList<>();
        try (Scanner lukija = new Scanner(new File(tiedostonNimi))) {
            while (lukija.hasNextLine()) {
                data.add(lukija.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Tapahtui virhe: " + e);
        }
        return data;
    }

    /**
     * Kirjoittaa asiakaslistan annetun nimiseen tiedostoon.
     *
     * @param asiakasLista  lista kirjoitettavista asiakkaista.
     * @param tiedostonNimi kirjoitettavan tiedoston nimi
     */
    public static void kirjoitaAsiakkaat(ArrayList<Asiakas> asiakasLista,
            String tiedostonNimi) {
        String data = "";
        for (Asiakas asiakas : asiakasLista) {
            data += asiakas.getData(VerkkokauppaIO.EROTIN);
            data += "\n";
        }
        // Poistetaan viimeinen "turha" rivinvaihto
        if (data.length() > 0) {
            data = data.substring(0, data.length() - 1);
        }
        kirjoitaTiedosto(tiedostonNimi, data);
    }

    /**
     * Palauttaa uuden asiakasolion annetun datarivin perusteella.
     * Rivillä tulee olla asiakasnumero, nimi ja tehdyt ostot tässä
     * järjestyksessä erotettuna merkillä
     * <code>VerkkokauppaIO.EROTIN</code>
     *
     * @param data datarivi, josta tiedot parsitaan
     * @return uuden Asiakas-olion dataan perustuen
     */
    public static Asiakas parsiAsiakas(String data) {
        String[] tiedot = data.split(VerkkokauppaIO.EROTIN);
        // Tässä vaiheessa tulee tietää tietojen järjestys
        String asNro = tiedot[0];
        String nimi = tiedot[1];
        double ostot = Double.parseDouble(tiedot[2]);

        return new Asiakas(asNro, nimi, ostot);
    }

    /**
     * Metodi lukee asiakkaat annetun nimisestä tiedostosta ja
     * palauttaa sisällön mukaisen listan Asiakas-olioita.
     *
     * @param tiedostonNimi luettavan tiedoston nimi
     * @return tiedostosta luetut asiakasoliot listana
     */
    public static ArrayList<Asiakas> lueAsiakkaat(String tiedostonNimi) {
        ArrayList<Asiakas> asiakkaat = new ArrayList<>();
        ArrayList<String> data = lueTiedosto(tiedostonNimi);
        for (String adata : data) {
            Asiakas as = parsiAsiakas(adata);
            asiakkaat.add(as);
        }
        return asiakkaat;
    }

    /**
     * Kirjoittaa tuotelistan annetun nimiseen tiedostoon.
     *
     * @param tuotelista    lista tuotteista
     * @param tiedostonNimi kirjoitettavan tiedoston nimi
     */
    public static void kirjoitaTuotteet(ArrayList<Tuote> tuotelista, String tiedostonNimi) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(tiedostonNimi))) {
            oos.writeObject(tuotelista);
        } catch (IOException e) {
            System.out.println("Tapahtui virhe: tuotteiden kirjoittamisessa " + e);
        }
    }

    /**
     * Lukee tuotelistan tiedostosta
     *
     * @param tiedostonNimi tiedoston nimi
     * @return listan tuotteita
     */
    public static ArrayList<Tuote> lueTuotteet(String tiedostonNimi) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedostonNimi))) {
            ArrayList<Tuote> tlista = (ArrayList<Tuote>) ois.readObject();
            return tlista;
        } catch (IOException e) {
            System.out.println("Tapahtui virhe: tuotteissa " + e);
        } catch (ClassNotFoundException e) {
            // Tämä virhe tulee, jos luettu tieto ei ole yhteensopiva
            // sen luokan kanssa, jonka tyyppiseksi se yritetään muuntaa
            System.out.println("Tapahtui virhe: tuotteissa 2 " + e);
        }
        return null;
    }

    /**
     * metodi tallentaa myyjät tiedostoon
     * Tämä toimii lähes täydellisesti samalla tavaoin kuin vastaaa metodi
     * asiakkaille
     */
    public static void kirjoitaMyyjat(ArrayList<Myyja> myyjalista, String tiedostonNimi) {
        String datamy = "";
        for (int i = 0; i < myyjalista.size(); i++) {
            datamy += myyjalista.get(i).getNimi() + EROTIN;
            datamy += myyjalista.get(i).getTunniste() + EROTIN;
            datamy += Double.toString(myyjalista.get(i).getProvisiot());
            datamy += "\n";
        }
        kirjoitaTiedosto(tiedostonNimi, datamy);
    }

    // parsii myyjät samoin kuin vastaava metodi asiakkaille
    public static Myyja parsiMyyja(String data) {
        String[] tiedot = data.split(VerkkokauppaIO.EROTIN);

        String nimi = tiedot[0];
        String tunniste = tiedot[1];
        double provisiot = Double.parseDouble(tiedot[2]);
        return new Myyja(tunniste, nimi, provisiot);
    }

    // taas vastaava metodi kuin asiakkaille
    // tässä tulee huomata, että jos lista "myyjat" tulostetaan ja halutaan luettava
    // muoto,
    // niin listaa pitää kutsua metodien getNimi(), getTunniste() ja getProvisio
    // avulla.
    public static ArrayList<Myyja> lueMyyjat(String tiedostonNimi) {
        ArrayList<Myyja> myyjat = new ArrayList<>();
        ArrayList<String> data = lueTiedosto(tiedostonNimi);
        for (String adata : data) {
            Myyja as = parsiMyyja(adata);
            myyjat.add(as);
        }
        return myyjat;
    }

    // metodi kirjoittaa ostotapahtumat tiedostoon
    public static void kirjoitaOstotapahtumat(ArrayList<Ostotapahtuma> ostotapahtumalista, String tiedostonNimi) {
        String dataost = "";
        for (int i = 0; i < ostotapahtumalista.size(); i++) {
            dataost += ostotapahtumalista.get(i).getAsiakas().getAsiakasNumero() + "," +
                    ostotapahtumalista.get(i).getAsiakas().getNimi() + ","
                    + ostotapahtumalista.get(i).getAsiakas().getOstojaTehty() + ",";

            dataost += ostotapahtumalista.get(i).getMyyja().getTunniste() + "," +
                    ostotapahtumalista.get(i).getMyyja().getNimi() + ","
                    + ostotapahtumalista.get(i).getMyyja().getProvisiot() + ",";

            dataost += ostotapahtumalista.get(i).getTuote().getNimi() + ","
                    + ostotapahtumalista.get(i).getTuote().getHinta() + "," +
                    ostotapahtumalista.get(i).getTuote().getSaldo() + ",";
            dataost += ostotapahtumalista.get(i).getMaara() + EROTIN;
            dataost += "\n";

        }
        kirjoitaTiedosto(tiedostonNimi, dataost);
    }
    // Ostotapahtumien lukumetodi

    public static ArrayList<Ostotapahtuma> lueTapahtumat(String tiedostonNimi) {
        ArrayList<String> lista = lueTiedosto(tiedostonNimi);
        Asiakas as;
        Myyja my;
        Tuote tu;
        int ma;
        Ostotapahtuma osto;
        double dobl;
        double dobl2;
        int integeri;
        ArrayList<Ostotapahtuma> ostelut = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            ;
            dobl = Double.parseDouble(lista.get(i).split(",")[7]);
            dobl2 = Double.parseDouble(lista.get(i).split(",")[8]);
            integeri = (int) dobl;
            as = new Asiakas(lista.get(i).split(",")[0], lista.get(i).split(",")[1],
                    Double.parseDouble(lista.get(i).split(",")[2]));
            my = new Myyja(lista.get(i).split(",")[3], lista.get(i).split(",")[4],
                    Double.parseDouble(lista.get(i).split(",")[5]));
            tu = new Tuote(lista.get(i).split(",")[6], integeri,
                    Double.parseDouble(lista.get(i).split(",")[7]));
            dobl2 = Double.parseDouble(lista.get(i).split(",")[8]);
            ma = (int) Double.parseDouble(lista.get(i).split(",")[8]);
            osto = new Ostotapahtuma(as, my, tu, ma);
            ostelut.add(osto);

        }
        return ostelut;
    }

}
