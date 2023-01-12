package sample;

/**
 * Initializes the values for the quickChart.io external api to create the chart
 * @author Kavishka Timashan
 */
public class QuickChart{
    static String quickChartUrl;
    static String chartType="doughnut";
    static String labelType="doughnutlabel";
    static String responseColor="rgba(153,102,255,1)";

    public static String[] borderColors= {"rgba(255,99,132,1)","rgba(54,162,235,1)","rgba(255,206,86,1)","rgba(75,192,192,1)","rgba(153,102,255,1)"};
    public static String[] backgroundColors= {"rgba(255,99,132,0.2)","rgba(54,162,235,0.2)","rgba(255,206,86,0.2)","rgba(75,192,192,0.2)","rgba(153,102,255,0.2)"};
    static String borderColorUrl;
    static String backgroundColorUrl;

    /**
     * Constructor
     * @param dataLabels
     * @param dataValues
     * @param responseNo
     */
    public QuickChart(StringBuilder dataLabels, StringBuilder dataValues, int responseNo) {

        if(chartType.equals("doughnut")){
            labelType="doughnutlabel";
        }else {
            labelType="pie";
        }
        borderColorUrl=",borderColor:['"+borderColors[0]+"','"+borderColors[1]+"','"+borderColors[2]+"','"+borderColors[3]+"','"+borderColors[4]+"'],";
        backgroundColorUrl=",backgroundColor:['"+backgroundColors[0]+"','"+backgroundColors[1]+"','"+backgroundColors[2]+"','"+backgroundColors[3]+"','"+backgroundColors[4]+"']";

        quickChartUrl = "https://quickchart.io/chart?c={type:'"+chartType+"',data:{labels:["+dataLabels+"],datasets:[{data:["+dataValues+"]"+backgroundColorUrl+borderColorUrl+"}]},options:{plugins:{"+labelType+":{labels:[{text:'"+responseNo+"',font:{size:20},color:'"+responseColor+"'},{text:'Responses',color:'"+responseColor+"'}]}}}}";
    }

    /**
     * Returns the chart Type (String)
     * @return chartType
     */
    public static String getChartType() {
        return chartType;
    }

    /**
     * Sets the quickChart to the default values
     * Used by defaultColors (in the SettingsControl class)
     */
    public static void defaultColors(){
        borderColors= new String[]{"rgba(255,99,132,1)", "rgba(54,162,235,1)", "rgba(255,206,86,1)", "rgba(75,192,192,1)", "rgba(153,102,255,1)"};
        backgroundColors = new String[]{"rgba(255,99,132,0.2)","rgba(54,162,235,0.2)","rgba(255,206,86,0.2)","rgba(75,192,192,0.2)","rgba(153,102,255,0.2)"};
    }
}
