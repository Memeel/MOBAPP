package fr.imt_atlantique.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    private String nom, prenom, ville, date, departement;
    private String[] phone;


    User(Parcel in) {
        this.nom = in.readString();
        this.prenom = in.readString();
        this.ville = in.readString();
        this.date = in.readString();
        this.departement = in.readString();
        this.phone = in.createStringArray();
    }

    User(String nom, String prenom, String ville, String date, String departement, String[] phone) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.date = date;
        this.departement = departement;
        this.phone = phone;
    }

    public String getNom() {return nom;}
    public String getPrenom() {return prenom;}
    public String getVille() {return ville;}
    public String getDate() {return date;}
    public String getDepartement() {return departement;}
    public String[] getPhone() {return phone;}

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(ville);
        dest.writeString(date);
        dest.writeString(departement);
        dest.writeStringArray(phone);
    }
}
