//Written by Peter Senyszyn
//2017

package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SentiWord
{
    //Main dictionary representation
    private Map<String, Double> _dictionary ;

    public SentiWord( String path ) throws IOException
    {
        _dictionary = new HashMap<>() ;

        HashMap<String, HashMap<Integer, Double>> tempDictionary =
                new HashMap<>() ;

        BufferedReader bufReader = null ;

        try
        {
            bufReader = new BufferedReader( new FileReader( path ) ) ;

            int lineNum = 0 ;

            String line ;

            while ( ( line = bufReader.readLine() ) != null )
            {
                lineNum++ ;

                //If a comment, skip line
                if ( !line.trim().startsWith( "#" ) )
                {
                    //Use tab separation
                    String[] data = line.split( "\t" ) ;
                    String wordTypeMarker = data[0] ;

                    //Valid line? Otherwise exception
                    if ( data.length != 6 )
                    {
                        throw new IllegalArgumentException( "Incorrect tabulation format in file, line: " + lineNum ) ;
                    }

                    //Calculate synset score as score = Pos - Neg
                    Double synsetScore = Double.parseDouble( data[2] ) - Double.parseDouble( data[3] ) ;

                    //Get all synset terms
                    String[] synTermsSplit = data[4].split( " " ) ;

                    for ( String synTermSplit : synTermsSplit )
                    {
                        String[] termAndRank = synTermSplit.split( "#" ) ;
                        String synTerm = termAndRank[0] + "#" + wordTypeMarker ;

                        int synTermRank = Integer.parseInt( termAndRank[1] ) ;

                        //We now have a map of the type:
                        //term -> { score #1, score #2, ... }

                        //Add map to term if it doesn't have one
                        if ( !tempDictionary.containsKey( synTerm ) )
                        {
                            tempDictionary.put( synTerm, new HashMap<>() ) ;
                        }

                        //Add synset link to synterm
                        tempDictionary.get( synTerm ).put( synTermRank, synsetScore ) ;
                    }
                }
            }

            //Go thru all terms
            for ( Map.Entry<String, HashMap<Integer, Double> > entry : tempDictionary.entrySet() )
            {
                String word = entry.getKey() ;

                Map<Integer, Double> synSetScoreMap = entry.getValue() ;

                //Calc weighted average
                //Score = 1/2 * first, 1/3 * second, 1/4 * third, etc
                //Sum = 1/1 + 1/2 + 1/3 ...
                double score = 0.0 ;
                double sum = 0.0 ;

                for ( Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet() )
                {
                    score += setScore.getValue() / ( double )setScore.getKey() ;
                    sum += 1.0 / ( double )setScore.getKey() ;
                }

                score /= sum ;

                _dictionary.put( word, score ) ;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace() ;
        }
        finally
        {
            if ( bufReader != null )
                bufReader.close() ;
        }
    }

    public double extract( String word, String position )
    {
        return _dictionary.get( word + "#" + position ) ;
    }
}
