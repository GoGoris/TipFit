package be.kdg.healthtips.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mathi on 6/02/2015.
 */
public class Tip {

    private int nummer;
    private String titel;
    private String beschrijving;

    public Tip()
    {

    }

    public Tip(int nummer, String titel, String beschrijving) {
        this.nummer = nummer;
        this.titel = titel;
        this.beschrijving = beschrijving;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    @Override
    public String toString() {
        return this.titel;
    }



}
