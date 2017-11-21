//Written by Peter Senyszyn
//2017

package sample ;

import javafx.fxml.FXML ;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
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
        _root = FXMLLoader.load( getClass().getResource("run.fxml" ) ) ;

        _currentStage = ( Stage )_runSentimentAnalysis.getScene().getWindow() ;

        _currentStage.setScene( new Scene( _root, 1024/2, 768/2 ) ) ;
        _currentStage.show() ;

        Run run = new Run() ;
    }
}
