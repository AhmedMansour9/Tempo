package bekya.bekyaa;

public class meesage {

    public  meesage (){}
    String Msg,from,to,date;

    public meesage(String msg, String from, String to, String date) {
        Msg = msg;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
