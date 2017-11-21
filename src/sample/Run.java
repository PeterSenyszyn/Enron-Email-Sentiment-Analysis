//Written by Peter Senyszyn
//2017

package sample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Run
{
    private ArrayList<Email> _emails = new ArrayList<>() ;

    private int counter = 0 ;

    public Run()
    {
        _parseEmails( "src/sample/final-enron-email-dataset" ) ;
        _sortChronologically() ;
    }

    public void displayData( Stage stage )
    {
        final CategoryAxis xAxis = new CategoryAxis() ;
        final NumberAxis yAxis = new NumberAxis() ;

        xAxis.setLabel( "Date" ) ;
        yAxis.setLabel( "Avg Sentiment Score" ) ;

        final LineChart<String, Number> lineChart = new LineChart<String, Number>( xAxis, yAxis ) ;

        lineChart.setTitle( "Avg Sentiment Score Of Select Enron Emails Over Time" ) ;

        XYChart.Series series = new XYChart.Series() ;
        series.setName( "Test" ) ;

        for ( Email e : _emails )
        {
            series.getData().add( new XYChart.Data( e.getFormattedDate(), e.getAvgSentimentScore() ) ) ;
        }

        //series.getData().add( new XYChart.Data( "1/2/2011", 0.5 ) ) ;

        Scene scene = new Scene( lineChart, 1024, 768 ) ;

        lineChart.getData().add( series ) ;

        stage.setScene( scene ) ;
        stage.show() ;
    }

    //Recursively searches through directory to find all associated email files
    private void _fetchEmailFiles( String path, ArrayList<File> files )
    {
        File directory = new File( path ) ;

        File[] fList = directory.listFiles() ;

        for ( File file : fList )
        {
            if ( file.isFile() )
            {
                if ( !file.getName().equals( ".DS_Store" ) )
                {
                    counter++ ;
                    System.out.println( counter ) ;
                    files.add(file);
                }
            }

            else if ( file.isDirectory() )
            {
                _fetchEmailFiles( file.getAbsolutePath(), files ) ;
            }
        }
    }

    //Sort the emails chronologically to be displayed in the line chart
    private void _sortChronologically()
    {
        Collections.sort( _emails ) ;

        for ( Email e : _emails )
            System.out.println( e.getFormattedDate() ) ;
    }

    private void _parseEmails( String path )
    {
        ArrayList<File> files = new ArrayList<>() ;

        _fetchEmailFiles( path, files ) ;

        for ( File file : files )
        {
            try
            {
                //Stuff list with each index representing a line of the email
                List<String> lines = Files.readAllLines( Paths.get(file.getPath() ), StandardCharsets.ISO_8859_1 ) ;

                Email email = new Email( new ArrayList<>( lines ) ) ;

                _emails.add( email ) ;
            }
            catch ( IOException e )
            {
                e.printStackTrace() ;
            }
        }
    }
}
