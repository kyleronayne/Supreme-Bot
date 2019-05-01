public class Profile {

    private int searchDelay;
    private String category;
    private String keyword;
    private String style;
    private String size;
    private String name;
    private String shippingAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String email;
    private String phoneNumber;

    public Profile() {

        // *Input as seconds*
        searchDelay = 2;

        // *Category is case sensitive*
        category = "Jackets";
        keyword = "leather";
        style = "burgundy";
        size = "medium";
        name = "John Doe";
        shippingAddress = "8815 71st Ave. NW";
        city = "Gig Harbor";
        zipCode = "98332";

        // *State is in abbreviated format*
        state = "WA";

        // *Country is in abbreviated format*
        country = "USA";
        email = "john.doe99@gmail.com";

        // *Phone number is formatted without spaces or special characters*
        phoneNumber = "2532251818";
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


    public String getName() {
        return name;
    }


    public String getShippingAddress() {
        return shippingAddress;
    }


    public String getCity() {
        return city;
    }


    public String getState() {
        return state;
    }


    public String getZipCode() {
        return zipCode;
    }


    public String getCountry() {
        return country;
    }


    public String getEmail() {
        return email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
}