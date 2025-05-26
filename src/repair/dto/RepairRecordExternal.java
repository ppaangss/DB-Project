package repair.dto;

public class RepairRecordExternal {
    private String shopName;
    private String companyName;
    private String customerName;
    private String details;
    private String repairDate;
    private String repairCost;
    private String dueDate;
    private String extraDetails;

    public RepairRecordExternal(String shopName, String companyName, String customerName,
                                String details, String repairDate, String repairCost,
                                String dueDate, String extraDetails) {
        this.shopName = shopName;
        this.companyName = companyName;
        this.customerName = customerName;
        this.details = details;
        this.repairDate = repairDate;
        this.repairCost = repairCost;
        this.dueDate = dueDate;
        this.extraDetails = extraDetails;
    }

    public String getShopName() { return shopName; }
    public String getCompanyName() { return companyName; }
    public String getCustomerName() { return customerName; }
    public String getDetails() { return details; }
    public String getRepairDate() { return repairDate; }
    public String getRepairCost() { return repairCost; }
    public String getDueDate() { return dueDate; }
    public String getExtraDetails() { return extraDetails; }
}
