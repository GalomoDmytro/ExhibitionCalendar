package entities;

import java.sql.Date;
import java.util.Objects;

/**
 * @author Dmytro Galomko
 */
public class Ticket {

    private Integer id;
    private Date dateToApply;
    private Integer contractId;
    private Integer quantity;
    private Date dateTransaction;
    private String userEMail;
    private boolean hasChecked;
    private Integer userId;
    private Integer approvedById;

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

    public String getUserEMail() {
        return userEMail;
    }

    public void setUserEMail(String userName) {
        this.userEMail = userName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getApprovedById() {
        if(approvedById == null) {
            return  0;
        }
        return approvedById;
    }

    public void setApprovedById(Integer approvedById) {
        this.approvedById = approvedById;
    }

    public boolean getHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public Integer getUserId() {
        if(userId == null) return 1;     // 1 - GUEST user
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Ticket emptyTicket() {
        return new Ticket.Builder()
                .setId(0)
                .setDateToApply(java.sql.Date.valueOf("2000-01-01"))
                .setDateTransaction(java.sql.Date.valueOf("2000-01-01"))
                .setUserMail("null")
                .setContractId(0)
                .setQuantity(0)
                .setUserId(1)
                .setAppruvedBy(0)
                .build();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", dateToApply=" + dateToApply +
                ", contractId=" + contractId +
                ", dateTransaction=" + dateTransaction +
                ", userEMail='" + userEMail + '\'' +
                ", quantity='" + quantity + '\'' +
                ", hasChecked='" + hasChecked + '\'' +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) &&
                Objects.equals(dateToApply.toString(), ticket.dateToApply.toString()) &&
                Objects.equals(contractId, ticket.contractId) &&
                Objects.equals(dateTransaction.toString(), ticket.dateTransaction.toString()) &&
                Objects.equals(userEMail, ticket.userEMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateToApply, contractId, dateTransaction, userEMail);
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

        public Builder setUserMail(String email) {
            ticket.setUserEMail(email);
            return this;
        }

        public Builder setQuantity(int quantity) {
            ticket.setQuantity(quantity);
            return this;
        }

        public Builder setChecked(boolean isCh) {
            ticket.setHasChecked(isCh);
            return this;
        }

        public Builder setUserId(Integer id) {
            ticket.setUserId(id);
            return this;
        }

        public Builder setAppruvedBy(Integer id) {
            ticket.setApprovedById(id);
            return this;
        }

        public Ticket build() {
            return ticket;
        }
    }
}
