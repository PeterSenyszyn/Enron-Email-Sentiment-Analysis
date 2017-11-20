//Written by Peter Senyszyn
//2017

package sample;

public class Email
{
    //Unfiltered email buffer
    private String _rawEmailStr ;

    //The average sentiment score from all considered words in the email
    private double _sentimentScore ;

    public Email( String rawEmailString )
    {
        _rawEmailStr = rawEmailString ;

        _calculateSentimentScore() ;
    }

    private void _filterEmail()
    {
        System.out.println( _rawEmailStr ) ;
    }

    private void _calculateSentimentScore()
    {
        //First, we need to trim the raw email buffer to grab ONLY the contents of the subject
        //and the actual email's contents
        _filterEmail() ;
    }

    public double getSentimentScore()
    { return _sentimentScore ; }
}
