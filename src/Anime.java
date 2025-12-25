public class Anime {
    private String id;
    private String title;
    private String genre;
    private String releaseDate;

    public Anime(String id, String title, String genre, String releaseDate) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
}
