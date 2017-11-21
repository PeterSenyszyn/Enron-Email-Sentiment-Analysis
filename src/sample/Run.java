//Written by Peter Senyszyn
//2017

package sample;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Run
{
    private ArrayList<Email> _emails = new ArrayList<>() ;

    private int counter = 0 ;

    public Run()
    {
        parseEmails( "src/sample/final-enron-email-dataset");
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
                    counter++ ;
                    System.out.println( counter ) ;
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
