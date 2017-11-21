//Written by Peter Senyszyn
//2017

package sample;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Email
{
    //Unfiltered email container
    private ArrayList<String> _rawEmail = new ArrayList<>() ;

    //Filtered email container
    private ArrayList<String> _filteredEmail = new ArrayList<>() ;

    //The average sentiment score from all considered words in the email
    private double _sentimentScore = 0.0 ;

    //If there are anomalies in the email, we'll go ahead and mark it for deletion from the main list
    private boolean _markedForDelete = false ;

    public Email( ArrayList<String> rawEmail )
    {
        _rawEmail = rawEmail ;

        _calculateSentimentScore() ;
    }

    private void _filterEmail()
    {
        //For ease of parsing, just skip over emails that have multiple subjects
        int subjectOccurences = 0 ;
        int xFileNameOccurences = 0 ;

        for ( int i = 0 ; i < _rawEmail.size() ; i++ )//( String s : _rawEmail )
        {
            String s = _rawEmail.get( i ) ;

            //Could use StringTokenizer, but comparing substrings is more efficient considering this will run on 300k+ emails on each line
            //If "Subject:" then we want to trim everything before the actual subject text
            //If "X-FileName:" then we know everything afterwards should be actual email contents
            if ( s.length() >= 8 && s.substring( 0, 8 ).equals( "Subject:" ) )
            {
                subjectOccurences++ ;

                //We don't want to deal with emails that have multiple subjects
                if ( subjectOccurences >= 2 )
                {
                    _markedForDelete = true ;
                }

                else
                {
                    String filteredSubject = s.replace( "Subject: ", "" ) ;

                    _filteredEmail.add( filteredSubject ) ;
                }
            }

            else if ( s.length() >= 11 && s.substring( 0, 11 ).equals( "X-FileName:" ) )
            {
                xFileNameOccurences++ ;

                if ( xFileNameOccurences >= 2 )
                {
                    _markedForDelete = true ;
                }

                else
                {
                    if ( i + 1 < _rawEmail.size() )
                    {
                        for ( int j = i + 1 ; j < _rawEmail.size() ; j++ )
                        {

                        }
                    }
                }
            }
        }
    }

    private void _calculateSentimentScore()
    {
        //First, we need to trim the raw email buffer to grab ONLY the contents of the subject
        //and the actual email's contents
        _filterEmail() ;
    }

    public double getSentimentScore()
    { return _sentimentScore ; }

    public boolean markedForDelete()
    { return _markedForDelete ; }
}
