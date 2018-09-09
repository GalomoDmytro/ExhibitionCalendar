package entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

/**
 * @author Dmytro Galomko
 */
public class Contract {

    private Integer id;
    private Integer exhibitionId;
    private String exhibitionTitle;
    private String exhibitionDescription;
    private Integer exCenterId;
    private String exhibitionCenterTitle;
    private Date dateFrom;
    private Date dateTo;
    private BigDecimal ticketPrice;
    private String workTime;
    private Integer maxTicketPerDay;

    public Contract(){}

    public Integer getId() {
        if(id == null) {
            return 0;
        }
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExhibitionId() {
        if(exhibitionId == null) {
            return 0;
        }
        return exhibitionId;
    }

    public void setExhibitionId(Integer exhibitionId) {
        this.exhibitionId = exhibitionId;
    }

    public String getExhibitionTitle() {
        return exhibitionTitle;
    }

    public String getExhibitionDescription() {
        if(exhibitionDescription == null) {
            return "";
        }
        return exhibitionDescription;
    }

    public void setExhibitionDescription(String exhibitionDescription) {
        this.exhibitionDescription = exhibitionDescription;
    }

    public void setExhibitionTitle(String exhibitionTitle) {
        this.exhibitionTitle = exhibitionTitle;
    }

    public String getExhibitionCenterTitle() {
        if(exhibitionCenterTitle == null) {
            return "";
        }
        return exhibitionCenterTitle;
    }

    public void setExhibitionCenterTitle(String exhibitionCenterTitle) {
        this.exhibitionCenterTitle = exhibitionCenterTitle;
    }

    public Integer getExCenterId() {
        if(exCenterId == null) {
            return 0;
        }
        return exCenterId;
    }

    public void setExCenterId(Integer exCenterId) {
        this.exCenterId = exCenterId;
    }

    public Date getDateFrom() {

        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public Integer getMaxTicketPerDay() {
        return maxTicketPerDay;
    }

    public void setMaxTicketPerDay(Integer maxTicketPerDay) {
        this.maxTicketPerDay = maxTicketPerDay;
    }

    public Contract emptyContract() {
        return new Contract.Builder()
                .setId(0)
                .setExCenterId(0)
                .setExhibitionId(0)
                .setDateFrom(null)
                .setDateTo(null)
                .setTicketPrice(new BigDecimal(0))
                .setWorkTime("null")
                .setMaxTicketPerDay(0)
                .build();
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", exhibitionId=" + exhibitionId +
                ", exCenterId=" + exCenterId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", ticketPrice=" + ticketPrice +
                ", workTime='" + workTime + '\'' +
                ", maxTicketPerDay=" + maxTicketPerDay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return Objects.equals(id, contract.id) &&
                Objects.equals(exhibitionId, contract.exhibitionId) &&
                Objects.equals(exCenterId, contract.exCenterId) &&
                Objects.equals(ticketPrice, contract.ticketPrice) &&
                Objects.equals(workTime, contract.workTime) &&
                Objects.equals(maxTicketPerDay, contract.maxTicketPerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exhibitionId, exCenterId, dateFrom, dateTo, ticketPrice, workTime, maxTicketPerDay);
    }

    public static class Builder {

        Contract contract;

        public Builder() {
            contract = new Contract();
        }

        public Builder setId(Integer id) {
            contract.setId(id);
            return this;
        }

        public Builder setExhibitionId(Integer exhibitionId) {
            contract.setExhibitionId(exhibitionId);
            return this;
        }

        public Builder setExhibitionTitle(String title) {
            contract.setExhibitionTitle(title);
            return this;
        }

        public Builder setExCenterId(Integer exCenterId) {
            contract.setExCenterId(exCenterId);
            return this;
        }

        public Builder setExhibitionCenterTitle(String title) {
            contract.setExhibitionCenterTitle(title);
            return this;
        }

        public Builder setDateFrom(Date dateFrom) {
            contract.setDateFrom(dateFrom);
            return this;
        }

        public Builder setDateTo(Date dateTo) {
            contract.setDateTo(dateTo);
            return this;
        }

        public Builder setTicketPrice(BigDecimal ticketPrice) {
            contract.setTicketPrice(ticketPrice);
            return this;
        }

        public Builder setTicketDescription(String description) {
            contract.setExhibitionDescription(description);
            return this;
        }

        public Builder setWorkTime(String workTime) {
            contract.setWorkTime(workTime);
            return this;
        }

        public Builder setMaxTicketPerDay(Integer maxTicketPerDay) {
            contract.setMaxTicketPerDay(maxTicketPerDay);
            return this;
        }

        public Contract build() {
            return contract;
        }
    }
}
