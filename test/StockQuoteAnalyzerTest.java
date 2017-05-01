import exceptions.InvalidAnalysisState;
import exceptions.InvalidStockSymbolException;
import exceptions.StockTickerConnectionError;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StockQuoteAnalyzerTest {
    @Mock
    private StockQuoteGeneratorInterface generatorMock;
    @Mock
    private StockTickerAudioInterface audioMock;

    private StockQuoteAnalyzer analyzer;

    @BeforeMethod
    public void setUp() throws Exception {
        generatorMock = mock(StockQuoteGeneratorInterface.class);
        audioMock = mock(StockTickerAudioInterface.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        generatorMock = null;
        audioMock = null;
    }

    @Test(expectedExceptions = InvalidStockSymbolException.class)//assert
    public void constructorShouldThrowExceptionWhenSymbolIsInvalid() throws Exception {
        //arrange
        analyzer = new StockQuoteAnalyzer("ZZZZZZZZZ", generatorMock, audioMock);
    }

    @Test(expectedExceptions = NullPointerException.class)//assert
    public void constructorShouldThrowExceptionWhenStockQuoteSourceIsNull() throws Exception {
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", null, audioMock);
    }

    @Test
    public void constructorShouldCreateNewObjectIfValidParameters() throws Exception {
        //arrange/act
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //assert
        if(analyzer == null){
            fail();
        }
    }
    @Test
    public void getSymbolShouldReturnTheCorrectSymbol() throws Exception {
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        String symbol = analyzer.getSymbol();
        //assert
        assertTrue(symbol.equals("ZUMZ"));
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getPreviousCloseShouldThowExceptionIfCurrentQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        analyzer.getPreviousClose();
    }

    @Test
    public void getPreviousCloseShouldReturnTheCorrectQuote() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 34.20, 24.71, 1.23));
        //act
        analyzer.refresh();

        double previousClose = analyzer.getPreviousClose();
        //assert
        assertEquals(previousClose, 34.20, 0.00001);
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getCurrentPriceShouldThowExceptionIfCurrentQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        analyzer.getCurrentPrice();
    }

    @Test
    public void getCurrentPriceShouldReturnTheCorrectPrice() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 34.20, 24.71, 1.23));
        //act
        analyzer.refresh();

        double currentPrice = analyzer.getCurrentPrice();
        //assert
        assertEquals(currentPrice, 24.71, 0.00001);
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getChangeSinceCloseShouldThowExceptionIfCurrentQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        analyzer.getChangeSinceClose();
    }

    @Test
    public void getChangeSinceCloseShouldReturnTheCorrectPriceChangeSinceClose() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 34.20, 24.71, 1.23));
        //act
        analyzer.refresh();

        double change = analyzer.getChangeSinceClose();
        //assert
        assertEquals(change, 1.23, 0.00001);
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getPercentChangeSinceCloseShouldThowExceptionIfCurrentQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        analyzer.getPercentChangeSinceClose();
    }

    @Test
    public void getPercentChangeSinceCloseShouldReturnTheCorrectPercentChangeSinceClose() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 34.20, 24.71, 1.23));
        //act
        analyzer.refresh();

        double percentChange = analyzer.getPercentChangeSinceClose();
        //assert
        assertEquals(percentChange, 35.96, 0.00001);
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getChangeSinceLastCheckShouldThowExceptionIfCurrentQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        analyzer.getChangeSinceLastCheck();
    }

    @Test(expectedExceptions = InvalidAnalysisState.class)//assert
    public void getChangeSinceLastCheckShouldThowExceptionIfPreviousQuoteIsNull() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 34.20, 24.71, 1.23));
        //act
        analyzer.refresh();
        analyzer.getChangeSinceLastCheck();
    }

    @Test
    public void getChangeSinceLastCheckShouldReturnTheCorrectChangeSinceLastCheck() throws Exception{
        //arrange
        analyzer = new StockQuoteAnalyzer("ZUMZ", generatorMock, audioMock);
        //act
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 0, 24.71, 0));
        analyzer.refresh();
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("ZUMZ", 0, 24.82, 0));
        analyzer.refresh();

        double change = analyzer.getChangeSinceLastCheck();
        //assert
        assertEquals(change, 0.11, 0.00001);
    }

    @Test
    public void refreshMethodTestShouldGetLatestInfoWhenNoConnectionError() throws InvalidStockSymbolException, StockTickerConnectionError
    {
        StockQuoteAnalyzer sampleStockQuote = new StockQuoteAnalyzer("A", generatorMock, audioMock);
        sampleStockQuote.refresh();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void refreshMethodTestShouldThrowExceptionWhenConnectionNotAvailable() throws StockTickerConnectionError, InvalidStockSymbolException
    {
        StockQuoteAnalyzer sampleStockQuote = new StockQuoteAnalyzer("A", null, audioMock);
        sampleStockQuote.refresh();
    }

    @Test
    public void testHappyAudioShouldBeInTheMoneyWhenGreaterThan1PercentChange() throws Exception {
        StockQuoteAnalyzer sampleStockQuote = new StockQuoteAnalyzer("A", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("AMBR", 7.3, 8.38, 5));
        sampleStockQuote.refresh();
        sampleStockQuote.playAppropriateAudio();
        verify(audioMock,times(1)).playHappyMusic();
    }

    @Test
    public void testSadAudioShouldBeGrrrSoundWhenLessThan1PercentChange() throws Exception {
        StockQuoteAnalyzer sampleStockQuote = new StockQuoteAnalyzer("A", generatorMock, audioMock);
        when(generatorMock.getCurrentQuote()).thenReturn(new StockQuote("AMBR", 9.3, 8.31, -0.5));
        sampleStockQuote.refresh();
        sampleStockQuote.playAppropriateAudio();
        verify(audioMock,times(1)).playSadMusic();
    }

    @Test
    public void testErrorAudioShouldPlayErrorMusicWhenChangeIsInvalid() throws InvalidAnalysisState, InvalidStockSymbolException, StockTickerConnectionError {
        StockQuoteAnalyzer sampleStockQuote = new StockQuoteAnalyzer("A", generatorMock, audioMock);
        sampleStockQuote.refresh();
        sampleStockQuote.playAppropriateAudio();
        verify(audioMock,times(1)).playErrorMusic();
    }
}