//Written by Peter Senyszyn
//2017

package sample;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Run
{
    private SentiWord _sentiWord ;
    private ArrayList<Email> _emails = new ArrayList<>() ;

    public Run()
    {
        try
        {
            _sentiWord = new SentiWord( "src/sample/sentiwordnet.txt" );

            
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    //Recursively searches through directory to find all associated email files
    private void fetchEmailFiles( String path, ArrayList<File> files )
    {
        File directory = new File( path ) ;

        File[] fList = directory.listFiles() ;

        for ( File file : fList )
        {
            if ( file.isFile() )
            {
                if ( !file.getName().equals( ".DS_Store" ) )
                {
                    files.add(file);
                }
            }

            else if ( file.isDirectory() )
            {
                fetchEmailFiles( file.getAbsolutePath(), files ) ;
            }
        }
    }

    public void parseEmails( String path )
    {
        ArrayList<File> files = new ArrayList<>() ;

        fetchEmailFiles( path, files ) ;

        int i = 0 ;

        for ( File file : files )
        {
            try
            {
                //Stuff list with each index representing a line of the email
                List<String> lines = Files.readAllLines( Paths.get(file.getPath() ), StandardCharsets.ISO_8859_1 ) ;

                Email email = new Email( new ArrayList<>( lines ) ) ;

                _emails.add( email ) ;

                //i++ ;

                //System.out.println( i ) ;
            }
            catch ( IOException e )
            {
                e.printStackTrace() ;
            }

            //System.out.println( file.getAbsolutePath() ) ;
        }

        //System.out.println( _emails.size() ) ;
    }

    public static void main( String[] args )
    {
        Run run = new Run() ;

        //run.parseEmails( "/Users/petersenyszyn/Downloads/enron-email-dataset" ) ;
        run.parseEmails( "/Users/petersenyszyn/Downloads/allen-p/poop" ) ;

        /*MaxentTagger tagger = new MaxentTagger( "src/sample/english-bidirectional-distsim.tagger" ) ;

        String sample = "This is sample text 3827." ;

        String tagged = tagger.tagString( sample ) ;

        StringTokenizer st = new StringTokenizer( tagged, " " ) ;

        while ( st.hasMoreTokens() )
        {
            String[] splitStr = st.nextToken().split( "_" ) ;

            for ( int i = 0 ; i < splitStr.length ; i++ )
                System.out.println( splitStr[i] ) ;
        }*/

        //String[] splitStr = tagged.split( "_" ) ;

        //System.out.println( tagged );

    }
}
