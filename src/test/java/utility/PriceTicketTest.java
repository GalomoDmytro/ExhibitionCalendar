package utility;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class PriceTicketTest {


    @Test
    public void getBigDecimalPriceValBadTest() {
        String badPrice = "abc";
        PriceTicket priceTicket = new PriceTicket();

        BigDecimal result = priceTicket.getBigDecimalPriceVal(badPrice);

        assertTrue(result.getClass().equals(BigDecimal.class));
        assertEquals(0, result.compareTo(new BigDecimal(BigInteger.ZERO)));
    }

    @Test
    public void getBigDecimalPriceValTest() {
        String price = "100";
        PriceTicket priceTicket = new PriceTicket();

        BigDecimal result = priceTicket.getBigDecimalPriceVal(price);
        System.out.println(result);
        assertTrue(result.getClass().equals(BigDecimal.class));
        assertEquals(0, result.compareTo(new BigDecimal(100)));
    }

    @Test
    public void calculateSumTicketsPriceTest() {
        String price = "110";
        PriceTicket priceTicket = new PriceTicket();

        BigDecimal bd = priceTicket.getBigDecimalPriceVal(price);
        BigDecimal resultOne = priceTicket.calculateSumTicketsPrice(bd, 1);
        BigDecimal resultTwo = priceTicket.calculateSumTicketsPrice(bd, 6);

        BigDecimal expectOne = priceTicket.getBigDecimalPriceVal("1,1");
        BigDecimal expectTwo = priceTicket.getBigDecimalPriceVal("6.6");

        System.out.println(resultOne);
        System.out.println(expectOne);
        System.out.println(resultTwo);

        assertTrue(resultOne.getClass().equals(BigDecimal.class));
        assertTrue(resultTwo.getClass().equals(BigDecimal.class));
        assertEquals(0, resultOne.compareTo(expectOne));
        assertEquals(0, resultTwo.compareTo(expectTwo));
    }
}