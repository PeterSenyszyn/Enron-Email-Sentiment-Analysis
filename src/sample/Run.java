//Written by Peter Senyszyn
//2017

package sample;

import java.io.IOException;
import java.util.ArrayList;

public class Run
{
    private SentiWord _sentiWord ;
    private ArrayList<Email> _emails = new ArrayList<>() ;

    public Run()
    {
        try
        {
            _sentiWord = new SentiWord( "src/sample/sentiwordnet.txt" );

            
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        /*Run run = new Run() ;

        MaxentTagger tagger = new MaxentTagger( "src/sample/english-bidirectional-distsim.tagger" ) ;

        String sample = "This is sample text." ;

        String tagged = tagger.tagString( sample ) ;

        String[] splitStr = tagged.split( "_" ) ;

        System.out.println( tagged );
        System.out.println( splitStr[0] ) ;
        System.out.println( splitStr[1] );*/
    }
}
