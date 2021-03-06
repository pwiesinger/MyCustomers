package at.lab73.paulwiesinger.mycustomers;

public class Customer {

    private int id;
    private String firstname;
    private String lastname;

    public Customer(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Customer(String csvLine) {
        String[] arr = csvLine.split(",");
        this.firstname = arr[1];
        this.lastname = arr[2];
        this.id = Integer.parseInt(arr[0]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return firstname + ", " + lastname;
    }
}
