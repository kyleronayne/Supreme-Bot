public class Profile {

    private int searchDelay;
    private String category;
    private String keyword;
    private String style;
    private String size;

    public Profile() {

        // *Input as seconds*
        searchDelay = 2;

        // *Category is case sensitive*
        category = "T-Shirts";
        keyword = "box";
        style = "white";
        size = "Large";
    }


    public int getSearchDelay() {
        return searchDelay * 1000;
    }


    public String getCategory() {
        return category;
    }


    public String getKeyword() {
        return keyword.toLowerCase();
    }


    public String getStyle() {
        return style.toLowerCase();
    }


    public String getSize() {
        return size.toLowerCase();
    }
}