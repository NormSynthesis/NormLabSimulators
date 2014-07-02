package es.csic.iiia.normlab.onlinecomm.graphics;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
/**
 * 
 * Class to make a Line Chart Graphic with the complaint and the ticks of the simulation.
 * 
 * @author Iosu Mendizabal
 *
 */
public class CreateGraphics extends JFrame { 
    private static Color COLOR_FONDO_GRAFICA = Color.white;
    private XYSeriesCollection dataset = new XYSeriesCollection();
    
    /**
     * Constructor of the CreateGraphics
     * 
     * @param title
     * 			Name of the chart.
     */
	public CreateGraphics(String title){
		super(title);  
	}
	
	/**
	 * Method to create the chart and make it visible for the user.
	 */
	public void createChart(){
		final JFreeChart chart = ChartFactory.createXYLineChart("Complaints in ticks", "Ticks", "Complaints", 
	                dataset,
	                PlotOrientation.VERTICAL, 
	                true, // uso de leyenda
	                true, // uso de tooltips  
	                false // uso de urls
	                );
	
		
	    // Graphic background color
	    chart.setBackgroundPaint(COLOR_FONDO_GRAFICA);
	    
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
		plot.setRenderer(renderer);
	    
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	    setBounds(200,50,1500,300);
	    setVisible(true);
	}
	
	/**
	 * Setter to refresh the data set from the XYSeriesCollection.
	 * 
	 * @param dataset
	 * 			XYSeriesCollection, class where the x and y values are saved.
	 */
    public void setDataset(XYSeriesCollection dataset){
    	this.dataset = dataset;
    }

}
