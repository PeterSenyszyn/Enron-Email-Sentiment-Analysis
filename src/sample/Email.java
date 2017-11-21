//Written by Peter Senyszyn
//2017

package sample;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Email implements Comparable<Email>
{
    //Unfiltered email container
    private ArrayList<String> _rawEmail = new ArrayList<>() ;

    //Filtered email container
    private ArrayList<String> _filteredEmail = new ArrayList<>() ;

    //Collection of sentiment scores
    private ArrayList<Double> _sentimentScores = new ArrayList<>() ;

    //The average sentiment score from all considered words in the email
    private double _avgSentimentScore = 0.0 ;

    //If there are anomalies in the email, we'll go ahead and mark it for deletion from the main list
    private boolean _markedForDelete = false ;

    //For later chronologically sorting the data
    private String _dayDate ;
    private String _monthDate ;
    private String _yearDate ;

    private static MaxentTagger _tagger = new MaxentTagger( "src/sample/english-left3words-distsim.tagger" ) ;

    private static SentiWord _sentiWord ;

    @Override
    public int compareTo( Email e )
    {
        DateFormat df = new SimpleDateFormat( "MMM/dd/yyyy" ) ;

        try
        {
            Date d1 = df.parse( getFormattedDate() ) ;
            Date d2 = df.parse( e.getFormattedDate() ) ;

            return d1.compareTo( d2 ) ;
        }

        catch ( ParseException p )
        {
            p.printStackTrace() ;
        }

        return 0 ;
    }

    public Email( ArrayList<String> rawEmail )
    {
        _rawEmail = rawEmail ;

        try
        {
            _sentiWord = new SentiWord( "src/sample/sentiwordnet.txt" ) ;
        }
        catch ( IOException e )
        {
            e.printStackTrace() ;
        }

        _calculateSentimentScore() ;
    }

    private void _filterEmail()
    {
        //For ease of parsing, just skip over emails that have multiple subjects
        int subjectOccurences = 0 ;
        int xFileNameOccurences = 0 ;
        int datesOccurences = 0 ;

        boolean hitXFile = false ;

        //2:46.55 previously
        //After changes:

        for ( String s : _rawEmail )
        {
            //Could use StringTokenizer, but comparing substrings is more efficient considering this will run on 300k+ emails on each line
            //If "Date:" then we want to grab ONLY Day/Month/Year
            //If "Subject:" then we want to trim everything before the actual subject text
            //If "X-FileName:" then we know everything afterwards should be actual email contents
            if ( s.length() >= 5 && s.substring( 0, 5 ).equals( "Date:" ) )
            {
                datesOccurences++ ;

                if ( datesOccurences >= 2 )
                {
                    _markedForDelete = true ;
                }

                else
                {
                    String[] splitStr = s.split( " " ) ;

                    if ( splitStr.length == 8 )
                    {
                        //Second index is the day
                        _dayDate = splitStr[2] ;

                        //Third index is the month
                        _monthDate = splitStr[3] ;

                        //Fourth index is the year
                        _yearDate = splitStr[4] ;
                    }

                    else
                    {
                        _markedForDelete = true ;
                    }
                }
            }

            else if ( s.length() >= 8 && s.substring( 0, 8 ).equals( "Subject:" ) )
            {
                subjectOccurences++ ;

                //We don't want to deal with emails that have multiple subjects
                if ( subjectOccurences >= 2 )
                {
                    _markedForDelete = true ;
                }

                else
                {
                    if ( !_markedForDelete )
                    {
                        String filteredSubject = s.replace( "Subject: ", "" ) ;

                        _filteredEmail.add( filteredSubject ) ;
                    }
                }
            }

            else if ( s.length() >= 11 && s.substring( 0, 11 ).equals( "X-FileName:" ) )
            {
                xFileNameOccurences++ ;

                if ( xFileNameOccurences >= 2 )
                {
                    _markedForDelete = true ;
                    hitXFile = false ;
                }

                else
                {
                    hitXFile = true ;
                }
            }

            //The ranged for loop above is more efficient than via indices because we avoid arraylist .get() calls
            //To accomodate that, we treat "hitXFile" as a trigger that the rest of the email is only actual email content,
            //therefore avoiding indices and .get() calls to arraylists
            if ( hitXFile )
            {
                String regexedLine = s.replaceAll( "[^a-zA-Z0-9 .,]", "" ) ;

                if ( !regexedLine.trim().isEmpty() && !_markedForDelete )
                {
                    _filteredEmail.add( regexedLine.trim() ) ;
                }
            }
        }
    }

    private void _calculateSentimentScore()
    {
        //First, we need to trim the raw email buffer to grab ONLY the contents of the subject
        //and the actual email's contents and date
        _filterEmail() ;

        for ( String s : _filteredEmail )
        {
            String taggedStr = _tagger.tagString( s ) ;

            StringTokenizer st = new StringTokenizer( taggedStr, " _" ) ;

            try
            {
                //This is unsafe in normal conditions, but in this situation there is guaranteed to be at least 2 tokens
                //if st.hasMoreTokens()
                while ( st.hasMoreTokens() )
                {
                    String word = st.nextToken() ;
                    String posStr = st.nextToken() ;

                    double score ;

                    /*
                    Sentiwordnet only has 5 notations, but the Stanford POS returns Penn Treebank which has 36 notations. Therefore, we need to do a
                    bit of translation to map Stanford POS to Sentiwordnet

                    SentiwordNet            -> Stanford equivalents
                    n - NOUN                -> Stanford equivalents: NN, NNS, NNP, NNPS, PRP, PRP$
                    v - VERB                -> Stanford equivalents: VB, VBD, VBG, VBN, VBP, VBZ
                    a - ADJECTIVE           -> Stanford equivalents: JJ, JJR, JJS
                    s - ADJECTIVE SATELLITE -> no real mapping to Stanford POS
                    r - ADVERB              -> Stanford equivalents: RB, RBR, RBS
                     */

                    //Noun
                    if ( Stream.of( "NN", "NNS", "NNP", "NNPS", "PRP", "PRP$" ).anyMatch( posStr::equals ) )
                    {
                        score = _sentiWord.extract( word, "n" ) ;
                    }

                    //Verb
                    else if ( Stream.of( "VB", "VBD", "VBG", "VBN", "VBP", "VBZ" ).anyMatch( posStr::equals ) )
                    {
                        score = _sentiWord.extract( word, "v" ) ;
                    }

                    //Adjective
                    else if ( Stream.of( "JJ", "JJR", "JJS" ).anyMatch( posStr::equals ) )
                    {
                        score = _sentiWord.extract( word, "a" ) ;
                    }

                    //Adverb
                    else if ( Stream.of( "RB", "RBR", "RBS" ).anyMatch( posStr::equals ) )
                    {
                        score = _sentiWord.extract( word, "r" ) ;
                    }

                    else
                    {
                        score = 0.0 ;
                    }

                    _sentimentScores.add( score ) ;
                }
            }
            catch ( NoSuchElementException e )
            {
                e.printStackTrace() ;
            }
        }

        Double summation = 0.0 ;

        for ( Double d : _sentimentScores )
        {
            summation += d ;
        }

        _avgSentimentScore = summation / _sentimentScores.size() ;

        //System.out.println( _avgSentimentScore ) ;
    }

    public Double getAvgSentimentScore()
    { return _avgSentimentScore ; }

    public String getMonthDate()
    { return _monthDate ; }

    public String getDayDate()
    { return _dayDate ; }

    public String getYearDate()
    { return _yearDate ; }

    public String getFormattedDate()
    { return _monthDate + "/" + _dayDate + "/" + _yearDate ; }
}
