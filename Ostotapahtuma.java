public class Ostotapahtuma {
    private Asiakas asiakas;
    private Myyja myyja;
    private Tuote tuote;
    private int maara;
    private double hinta;

    public Ostotapahtuma(Asiakas asiakas, Myyja myyja,
            Tuote tuote, int maara) {
        this.asiakas = asiakas;
        this.myyja = myyja;
        this.tuote = tuote;
        this.maara = maara;
        hinta = maara * tuote.getHinta();

        // Vähennetään hinnasta vielä alennus
        hinta = hinta - hinta * (asiakas.getAlennusprosentti() / 100);
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public Myyja getMyyja() {
        return myyja;
    }

    public Tuote getTuote() {
        return tuote;
    }

    public int getMaara() {
        return maara;
    }

    public double getHinta() {
        return hinta;
    }

    @Override
    public String toString() {
        return "Asiakas: " + asiakas + " / Myyjä: " + myyja + " / Tuote: " + tuote + " / Määrä: " + maara;
    }
}
