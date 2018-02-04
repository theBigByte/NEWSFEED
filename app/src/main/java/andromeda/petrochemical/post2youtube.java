package andromeda.petrochemical;

public class post2youtube {

    private String youtubed;
    private String share;
    private String title;
    private String source;


     public post2youtube(){

    }


    public post2youtube(String youtubed,String share,String title,String source) {
        this.youtubed = youtubed;
        this.share = share;
        this.title = title;
        this.source = source;
    }


    public String getYoutubed() {
        return youtubed;
    }

    public void setYoutubed(String youtubed) {
        this.youtubed = youtubed;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
