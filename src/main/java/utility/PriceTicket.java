package utility;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author Dmytro Galomko
 */
public class PriceTicket {

    public PriceTicket() {
    }

    public BigDecimal getBigDecimalPriceVal(String priseLine) {
        priseLine = checkIfNumber(priseLine);

        BigDecimal price = new BigDecimal(priseLine);
        setRound(price);

        return price;
    }

    private String checkIfNumber(String priseLine) {
        if (priseLine == null) {
            return "0";
        }

        String[] digit = priseLine.split("");

        for (String d : digit) {
            if (!StringUtils.isNumeric(d)
                    && !d.equals(".")
                    && !d.equals(",")
            ) {
                return "0";
            }
        }

        if(priseLine.contains(",")) {
            priseLine = priseLine.replaceAll(",", ".");
        }

        return priseLine;
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
