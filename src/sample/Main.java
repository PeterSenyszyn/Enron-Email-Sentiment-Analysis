//Written by Peter Senyszyn
//2017

package sample ;

import javafx.application.Application ;
import javafx.fxml.FXMLLoader ;
import javafx.scene.Parent ;
import javafx.scene.Scene ;
import javafx.stage.Stage ;

public class Main extends Application
{
    @Override
    public void start( Stage primaryStage ) throws Exception
    {
        Parent root = FXMLLoader.load( getClass().getResource("main.fxml") ) ;
        primaryStage.setTitle( "Enron Email Analysis - Created by Peter Senyszyn" ) ;
        primaryStage.setScene( new Scene( root, 1024/2, 768/2 ) ) ;
        primaryStage.show() ;
    }

    public static void main( String[] args )
    {
        launch( args ) ;
    }
}
