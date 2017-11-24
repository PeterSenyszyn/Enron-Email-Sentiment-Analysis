//Written by Peter Senyszyn
//2017

package sample ;

import javafx.fxml.FXML ;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller
{
    private Parent _root = null ;
    private Stage _currentStage = null ;

    @FXML
    private Button _runSentimentAnalysis ;

    public Controller()
    {

    }

    @FXML
    protected void onRunSentimentAnalysisPressed() throws IOException
    {
        _currentStage = ( Stage )_runSentimentAnalysis.getScene().getWindow() ;

        Run run = new Run() ;

        run.displayData( _currentStage ) ;
    }
}
