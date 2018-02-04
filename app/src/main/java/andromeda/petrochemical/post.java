package andromeda.petrochemical;

public class post {

    private String title;
    private String description;
    private String image;
    private String source;
    private String read;
    private String youtubed;


    public post(){
    }

    public post(String title, String description, String image, String source,String read,String youtubed) {

        this.title = title;
        this.description = description;
        this.image = image;
        this.source = source;
        this.read = read;
        this.youtubed = youtubed;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getYoutubed() {
        return youtubed;
    }

    public void setYoutubed(String youtubed) {
        this.youtubed = youtubed;
    }
}

