package utility;

import java.math.BigDecimal;

public class PriceTicket {

    public PriceTicket(){}

    public BigDecimal getBigDecimalPriceVal(String priseLine) {
        BigDecimal price = new BigDecimal(priseLine);
//        price = price.divide(BigDecimal.valueOf(100));
        setRound(price);

        return price;
    }

    public BigDecimal calculateSumTicketsPrice(BigDecimal ticketPrice, int quantityTickets) {
        BigDecimal price = ticketPrice.multiply(BigDecimal.valueOf(quantityTickets));
        price = price.divide(BigDecimal.valueOf(100));
        setRound(price);
        return price;
    }

    private void setRound(BigDecimal bigDecimal) {
        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
