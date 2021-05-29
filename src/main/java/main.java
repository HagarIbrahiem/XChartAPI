
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

 public class main {
    public static void main(String[] args) {
        List <TitanicPassenger> PasngrList = new ArrayList<TitanicPassenger>();
        PasngrList = getPassengersFromJsonFile();
        graphPassAges(PasngrList);
        graphPassClass(PasngrList);
        PasngrListgraphPassSurvived (PasngrList);
        graphPassSurvivedGender (PasngrList);
        
        
    }
    
    public static List <TitanicPassenger> getPassengersFromJsonFile() {
        List<TitanicPassenger> allPassengers = new ArrayList<TitanicPassenger> ();
        ObjectMapper objectMapper = new ObjectMapper ();
        objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream input = new FileInputStream ("titanic_csv.json")) {
            //Read JSON file
            allPassengers = objectMapper.readValue (input, new TypeReference<List<TitanicPassenger>> () {
            });
            System.out.println("Done .......");
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } 
        return allPassengers;
    }
    
    public static void graphPassAges(List <TitanicPassenger> PasngrList){
    List<Float> pAgeslst = PasngrList.stream().map(TitanicPassenger :: getAge).limit(8).collect(Collectors.toList());
    List<String> pNameslst = PasngrList.stream().map(TitanicPassenger :: getName).limit(8).collect(Collectors.toList());
    
    CategoryChart chart = new CategoryChartBuilder().width(500).height(500).title("Passengers Names & Ages").
                          xAxisTitle("Name").yAxisTitle("Age").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setStacked(true);
    
    chart.addSeries("Series Name : Passengers Names & Ages", pNameslst, pAgeslst);
    
    new SwingWrapper(chart).displayChart();

    }
    public static void graphPassClass(List <TitanicPassenger> PasngrList){
   
    Map<String , Long > result = PasngrList.stream().
                     collect(Collectors.groupingBy(TitanicPassenger :: getPclass , Collectors.counting()));
    PieChart chart = new PieChartBuilder().width(500).height(500).title(  "Passengers Classes").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setHasAnnotations(true);
    
    chart.addSeries("First Class", result.get("1"));
    chart.addSeries("Second Class", result.get("2"));
    chart.addSeries("Third Class", result.get("3"));
    
    new SwingWrapper(chart).displayChart();
    }

    public static void PasngrListgraphPassSurvived(List <TitanicPassenger> PasngrList){
          Map<String , Long> result = PasngrList.stream().collect(Collectors.groupingBy(TitanicPassenger :: getSurvived , Collectors.counting())    );
          
          //Create Chart
          PieChart chart = new PieChartBuilder().width(800).height(600).build();
          Color[] slicecolors = new Color [] {new Color (180,60,50) ,new Color (130,105,120)};
          chart.getStyler().setSeriesColors(slicecolors);
          chart.addSeries("Non-Survived", result.get("0"));
          chart.addSeries("Survived", result.get("1"));
          
          new SwingWrapper(chart).displayChart();
          
     }
     
    public static void graphPassSurvivedGender(List <TitanicPassenger> PasngrList){
          Map<String , Long> result = PasngrList.stream().filter(t -> t.getSurvived().equals("1"))
                                     .collect(Collectors.groupingBy(TitanicPassenger :: getSex , Collectors.counting()));
          
          //Create Chart
          PieChart chart = new PieChartBuilder().width(800).height(600).build();
         Color[] slicecolors = new Color [] {new Color (180,60,50) ,new Color (130,105,120)};
         chart.getStyler().setSeriesColors(slicecolors);
         chart.addSeries("Females Susrvived", result.get("female"));
         chart.addSeries("Male Susrvived", result.get("male"));
          new SwingWrapper(chart).displayChart();
     }
}
