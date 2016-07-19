package itservicesam.android.hcg_kur_manager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

@Entity
public class BodyData {

    @Id
    private Long id;

    @NotNull
    private Date date;
    private double gewicht;

    private int bauchUmfang;

    private int oberschenkelUmfangLinks;

    private int oberschenkelUmfangRechts;

    private int brustUmfang;

    private int poUmfang;

    public BodyData() {
    }

    public BodyData(Long id) {
        this.id = id;
    }

    @Generated(hash = 1802438322)
    public BodyData(Long id, @NotNull Date date, double gewicht, int bauchUmfang,
            int oberschenkelUmfangLinks, int oberschenkelUmfangRechts,
            int brustUmfang, int poUmfang) {
        this.id = id;
        this.date = date;
        this.gewicht = gewicht;
        this.bauchUmfang = bauchUmfang;
        this.oberschenkelUmfangLinks = oberschenkelUmfangLinks;
        this.oberschenkelUmfangRechts = oberschenkelUmfangRechts;
        this.brustUmfang = brustUmfang;
        this.poUmfang = poUmfang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    @NotNull
    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(@NotNull double gewicht) {
        this.gewicht = gewicht;
    }

    public int getBauchUmfang() {
        return bauchUmfang;
    }

    public void setBauchUmfang(int bauchUmfang) {
        this.bauchUmfang = bauchUmfang;
    }

    public int getOberschenkelUmfangLinks() {
        return oberschenkelUmfangLinks;
    }

    public void setOberschenkelUmfangLinks(int oberschenkelUmfangLinks) {
        this.oberschenkelUmfangLinks = oberschenkelUmfangLinks;
    }

    public int getOberschenkelUmfangRechts() {
        return oberschenkelUmfangRechts;
    }

    public void setOberschenkelUmfangRechts(int oberschenkelUmfangRechts) {
        this.oberschenkelUmfangRechts = oberschenkelUmfangRechts;
    }

    public int getBrustUmfang() {
        return brustUmfang;
    }

    public void setBrustUmfang(int brustUmfang) {
        this.brustUmfang = brustUmfang;
    }

    public int getPoUmfang() {
        return poUmfang;
    }

    public void setPoUmfang(int poUmfang) {
        this.poUmfang = poUmfang;
    }
}
