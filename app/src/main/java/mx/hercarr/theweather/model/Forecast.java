package mx.hercarr.theweather.model;

public class Forecast {

    private Current current;
    private Hour[] hours;
    private Day[] days;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hour[] getHours() {
        return hours;
    }

    public void setHours(Hour[] hours) {
        this.hours = hours;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }

}