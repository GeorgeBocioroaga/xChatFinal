package ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils;

public class User {

    private String name;
    private String grupa;
    private String an;
    private String image;
    private String thumb_image;
    private String online;
    private Float lastOnline;

    public Float getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Float lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    private String uid;

    public String  getUid(){
        return this.uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public User(){}


    public User(String an,String grupa,String image, String name, String online, String thumb_image, Float lastOnline) {
        setAn(an);
        setGrupa(grupa);
        setName(name);
        setImage(image);
        setThumb_image(thumb_image);
        setOnline(online);
        setLastOnline(lastOnline);


    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
