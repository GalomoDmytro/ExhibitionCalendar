package entities;

import java.util.Date;
import java.util.Objects;

public class Ticket {

    private Integer id;
    private Date dateToApply;
    private Integer contractId;
    private Date dateTransaction;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateToApply() {
        return dateToApply;
    }

    public void setDateToApply(Date dateToApply) {
        this.dateToApply = dateToApply;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Date getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Date dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", dateToApply=" + dateToApply +
                ", contractId=" + contractId +
                ", dateTransaction=" + dateTransaction +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) &&
                Objects.equals(dateToApply, ticket.dateToApply) &&
                Objects.equals(contractId, ticket.contractId) &&
                Objects.equals(dateTransaction, ticket.dateTransaction) &&
                Objects.equals(userName, ticket.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateToApply, contractId, dateTransaction, userName);
    }

    public static class Builder {

        private Ticket ticket;

        public Builder() {
            ticket = new Ticket();
        }

        public Builder setId(Integer id) {
            ticket.setId(id);
            return this;
        }

        public Builder setDateToApply(Date date) {
            ticket.setDateToApply(date);
            return this;
        }

        public Builder setContractId(Integer contractId) {
            ticket.setContractId(contractId);
            return this;
        }

        public Builder setDateTransaction(Date dateTransaction) {
            ticket.setDateTransaction(dateTransaction);
            return this;
        }

        public Builder setUserName(String userName) {
            ticket.setUserName(userName);
            return this;
        }

        public Ticket build() {
            return ticket;
        }
    }
}
