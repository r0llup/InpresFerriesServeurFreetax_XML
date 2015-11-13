/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package types;

/**
 *
 * @author Sh1fT
 */

public class DayYearQte {
    protected Integer day;
    protected Integer year;
    protected Double quantity;

    public DayYearQte(Integer day, Integer year, Double quantity) {
        this.day = day;
        this.year = year;
        this.quantity = quantity;
    }

    public DayYearQte() {
        this.day = null;
        this.year = null;
        this.quantity = null;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}