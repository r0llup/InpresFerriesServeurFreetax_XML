/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inpresferriesserveurfreetax_xml;

import helpers.MD5Helper;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import types.DayYearQte;

/**
 *
 * @author Sh1fT
 */

public final class Protocole {
    protected Serveur_IFreetax parent;

    /**
     * Creates new instance Protocole
     * @param parent
     */
    public Protocole(Serveur_IFreetax parent) {
        this.setParent(parent);
    }

    public Serveur_IFreetax getParent() {
        return parent;
    }

    protected void setParent(Serveur_IFreetax parent) {
        this.parent = parent;
    }

    /**
     * Démarrage de l'application : un comptable se faire reconnaître
     * @param name
     * @param password
     * @return 
     */
    public boolean login(String name, String password) {
        try {
            CallableStatement psLogin = this.getParent().getMysqlConnection()
                .prepareCall(this.getParent().getLoginQuery());
            psLogin.setString(1, name);
            psLogin.setString(2, MD5Helper.getEncodedPassword(password));
            return psLogin.executeQuery().first();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
        return false;
    }

    /**
     * Calcul de la moyenne, du mode, de l'écart-type
     * @param category
     * @param week
     * @param month
     * @return 
     */
    public String getStatisticsDesc(String category, String week, String month) {
        try {
            Double sum = this.getStatisticsDescSum(category);
            if (week.compareTo("unknown") != 0) {
                Double sumWeek = this.getStatisticsDescSumWeek(category, week);
                Double avgWeek = this.getStatisticsDescAvgWeek(category, week);
                Integer modWeek = this.getStatisticsDescModWeek(category, week);
                Double stdWeek = this.getStdDev(
                    this.getStatisticsDescValWeek(category, week), avgWeek);
                return new StringBuilder(sum + ";" + sumWeek + ";" + avgWeek +
                    ";" + modWeek + ";" + stdWeek).toString();
            } else {
                Double sumMonth = this.getStatisticsDescSumMonth(category, month);
                Double avgMonth = this.getStatisticsDescAvgMonth(category, month);
                Integer modMonth = this.getStatisticsDescModMonth(category, month);
                Double stdMonth = this.getStdDev(
                    this.getStatisticsDescValMonth(category, month), avgMonth);
                return new StringBuilder(sum + ";" + sumMonth + ";" + avgMonth +
                    ";" + modMonth + ";" + stdMonth).toString();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
        return null;
    }

    /**
     * Calcul de la somme des commandes selon une catégorie d'article
     * @param category
     * @return
     * @throws SQLException 
     */
    protected Double getStatisticsDescSum(String category) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescSumQuery());
        psStatisticsDesc.setString(1, category);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getDouble(1);
        return null;
    }

    /**
     * Calcul de la somme selon l'article et la semaine
     * @param category
     * @param week
     * @return
     * @throws SQLException 
     */
    protected Double getStatisticsDescSumWeek(String category, String week) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescSumWeekQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, week);
        psStatisticsDesc.setString(3, week);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getDouble(1);
        return null;
    }

    /**
     * Calcul de la somme selon l'article et le mois
     * @param category
     * @param month
     * @return
     * @throws SQLException 
     */
    protected Double getStatisticsDescSumMonth(String category, String month) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescSumMonthQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, month);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getDouble(1);
        return null;
    }

    /**
     * Calcul de la moyenne selon l'article et la semaine
     * @param category
     * @param week
     * @return
     * @throws SQLException 
     */
    protected Double getStatisticsDescAvgWeek(String category, String week) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescAvgWeekQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, week);
        psStatisticsDesc.setString(3, week);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getDouble(1);
        return null;
    }

    /**
     * Calcul de la moyenne selon l'article et le mois
     * @param category
     * @param month
     * @return
     * @throws SQLException 
     */
    protected Double getStatisticsDescAvgMonth(String category, String month) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescAvgMonthQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, month);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getDouble(1);
        return null;
    }

    /**
     * Calcul du mode selon l'article et la semaine
     * @param category
     * @param week
     * @return
     * @throws SQLException 
     */
    protected Integer getStatisticsDescModWeek(String category, String week) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescModWeekQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, week);
        psStatisticsDesc.setString(3, week);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getInt(1);
        return null;
    }

    /**
     * Calcul du mode selon l'article et le mois
     * @param category
     * @param month
     * @return
     * @throws SQLException 
     */
    protected Integer getStatisticsDescModMonth(String category, String month) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescModMonthQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, month);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first())
            return rs.getInt(1);
        return null;
    }

    /**
     * Récupération des jours et des quantitées selon l'article et la semaine
     * @param category
     * @param week
     * @return
     * @throws SQLException 
     */
    protected List<DayYearQte> getStatisticsDescValWeek(String category, String week) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescValWeekQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, week);
        psStatisticsDesc.setString(3, week);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first()) {
            List<DayYearQte> valWeek = new LinkedList<DayYearQte>();
            while (rs.next())
                valWeek.add(new DayYearQte(rs.getInt(1), null, rs.getDouble(2)));
            return valWeek;
        }
        return null;
    }

    /**
     * Calcul du mode selon l'article et le mois
     * @param category
     * @param month
     * @return
     * @throws SQLException 
     */
    protected List<DayYearQte> getStatisticsDescValMonth(String category, String month) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescValMonthQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, month);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first()) {
            List<DayYearQte> valMonth = new LinkedList<DayYearQte>();
            while (rs.next())
                valMonth.add(new DayYearQte(rs.getInt(1), null, rs.getDouble(2)));
            return valMonth;
        }
        return null;
    }

    /**
     * Calcul de la corrélation selon l'article et la semaine
     * @param category
     * @param week
     * @return
     * @throws SQLException 
     */
    protected List<DayYearQte> getStatisticsDescCorWeek(String category, String week) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescCorWeekQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, week);
        psStatisticsDesc.setString(3, week);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first()) {
            List<DayYearQte> corWeek = new LinkedList<DayYearQte>();
            while (rs.next())
                corWeek.add(new DayYearQte(null, rs.getInt(2), rs.getDouble(3)));
            return corWeek;
        }
        return null;
    }

    /**
     * Calcul de la corrélation selon l'article et le mois
     * @param category
     * @param month
     * @return
     * @throws SQLException 
     */
    protected List<DayYearQte> getStatisticsDescCorMonth(String category, String month) throws SQLException {
        CallableStatement psStatisticsDesc = this.getParent().getMysqlConnection()
            .prepareCall(this.getParent().getStatisticsDescCorMonthQuery());
        psStatisticsDesc.setString(1, category);
        psStatisticsDesc.setString(2, month);
        ResultSet rs = psStatisticsDesc.executeQuery();
        if (rs.first()) {
            List<DayYearQte> corMonth = new LinkedList<DayYearQte>();
            while (rs.next())
                corMonth.add(new DayYearQte(null, rs.getInt(2), rs.getDouble(3)));
            return corMonth;
        }
        return null;
    }

    /**
     * Calcul de l'écart-type
     * @param vals
     * @param avg
     * @return 
     */
    protected Double getStdDev(List<DayYearQte> vals, Double avg) {
        Double pow = 0.0;
        if (vals != null) {
            for (DayYearQte val : vals)
                pow += Math.pow(val.getQuantity() - avg, 2);
            
        }
        return Math.sqrt(pow);
    }

    /**
     * Calcul d'un graphique 1D
     * @param category
     * @param week
     * @param month
     * @param type
     * @return 
     */
    public JFreeChart getGraph1D(String category, String week, String month,
        String type) {
        try {
            JFreeChart jfc = null;
            if (type.compareTo("SECT") == 0) {
                if (week.compareTo("unknown") != 0)
                    jfc = ChartFactory.createPieChart("Quantitées selon les jours",
                        this.feedPieDataset(this.getStatisticsDescValWeek(category, week)),
                        true, true, true);
                else
                    jfc = ChartFactory.createPieChart("Quantitées selon les jours",
                        this.feedPieDataset(this.getStatisticsDescValMonth(category, month)),
                        true, true, true);
            } else if (type.compareTo("HIST") == 0) {
                if (week.compareTo("unknown") != 0)
                    jfc = ChartFactory.createBarChart("Quantitées selon les jours",
                        "Jours", "Quantitées", this.feedCategoryDataset(
                        this.getStatisticsDescValWeek(category, week), category),
                        PlotOrientation.VERTICAL, true, true, true);
                else
                    jfc = ChartFactory.createBarChart("Quantitées selon les jours",
                        "Jours", "Quantitées", this.feedCategoryDataset(
                        this.getStatisticsDescValMonth(category, month), category),
                        PlotOrientation.VERTICAL, true, true, true);
            } else if (type.compareTo("CHRO") == 0) {
                if (week.compareTo("unknown") != 0)
                    jfc = ChartFactory.createTimeSeriesChart("Quantitées selon les jours",
                        "Jours", "Quantitées", this.feedXYDataset(
                        this.getStatisticsDescValWeek(category, week), category,
                        month), true, true, true);
                else
                    jfc = ChartFactory.createTimeSeriesChart("Quantitées selon les jours",
                        "Jours", "Quantitées", this.feedXYDataset(
                        this.getStatisticsDescValMonth(category, month), category,
                        month), true, true, true);
            }
            return jfc;
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
        return null;
    }

    /**
     * Calcul d'un graphique 2D
     * @param category
     * @param week
     * @param month
     * @param type
     * @return 
     */
    public JFreeChart getGraph2D(String category, String week, String month,
        String type) {
        try {
            JFreeChart jfc = null;
            if (type.compareTo("CHRO") == 0) {
                if (week.compareTo("unknown") != 0)
                    jfc = ChartFactory.createScatterPlot("Ventes selon les âges",
                        "Âges", "Quantitées", this.feedXYDataset2(
                        this.getStatisticsDescCorWeek(category, week), category),
                        PlotOrientation.VERTICAL, true, true, true);
                else
                    jfc = ChartFactory.createScatterPlot("Ventes selon les âges",
                        "Âges", "Quantitées", this.feedXYDataset2(
                        this.getStatisticsDescCorMonth(category, month), category),
                        PlotOrientation.VERTICAL, true, true, true);
            }
            return jfc;
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }
        return null;
    }

    /**
     * Create and add values to the dataset
     * @param vals
     * @return 
     */
    protected DefaultPieDataset feedPieDataset(List<DayYearQte> vals) {
        if (vals != null) {
            DefaultPieDataset dpd = new DefaultPieDataset();
            for (DayYearQte val : vals) {
                dpd.setValue(new StringBuilder().append(val.getDay()).
                    append(" (").append(val.getQuantity()).append(")").toString(),
                    val.getQuantity());
            }
            return dpd;
        }
        return null;
    }

    /**
     * Create and add values to the dataset
     * @param vals
     * @param category
     * @return 
     */
    protected DefaultCategoryDataset feedCategoryDataset(List<DayYearQte> vals,
        String category) {
        if (vals != null) {
            DefaultCategoryDataset dcd = new DefaultCategoryDataset();
            for (DayYearQte val : vals)
                dcd.addValue(val.getQuantity(), val.getDay(), category);
            return dcd;
        }
        return null;
    }

    /**
     * Create and add values to the dataset
     * @param vals
     * @param category
     * @param month
     * @return 
     */
    protected XYDataset feedXYDataset(List<DayYearQte> vals, String category,
        String month) {
        if (vals != null) {
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            TimeSeries ts = new TimeSeries(category);
            Integer month_;
            if (month.compareTo("unknown") == 0)
                month_ = Calendar.getInstance().get(Calendar.MONTH);
            else
                month_ = Integer.parseInt(month);
            for (DayYearQte val : vals)
                ts.add(new Day(val.getDay(), month_,
                    Calendar.getInstance().get(Calendar.YEAR)), val.getQuantity());
            dataset.addSeries(ts);
            return dataset;
        }
        return null;
    }

    /**
     * Create and add values to the dataset
     * @param vals
     * @param category
     * @return 
     */
    protected XYDataset feedXYDataset2(List<DayYearQte> vals,
        String category) {
        if (vals != null) {
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries xs = new XYSeries(category);
            for (DayYearQte val : vals)
                xs.add(Calendar.getInstance().get(Calendar.YEAR) - val.getYear(),
                    val.getQuantity());
            dataset.addSeries(xs);
            return dataset;
        }
        return null;
    }
}